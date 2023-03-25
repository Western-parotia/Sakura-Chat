package com.sakura.chat.v2.business.main

import android.Manifest
import android.os.Bundle
import androidx.core.view.isVisible
import com.foundation.app.arc.utils.param.BundleParams
import com.foundation.service.permission.PermissionBox
import com.foundation.widget.crvadapter.viewbinding.ViewBindingQuickAdapter
import com.foundation.widget.crvadapter.viewbinding.ViewBindingViewHolder
import com.foundation.widget.utils.ext.view.hideKeyboard
import com.foundation.widget.utils.ext.view.setOnShakeLessClickListener
import com.foundation.widget.utils.ext.view.toUIContext
import com.sakura.chat.R
import com.sakura.chat.databinding.ActChatBinding
import com.sakura.chat.databinding.AdapterChatBinding
import com.sakura.chat.utils.AudioRecorder
import com.sakura.chat.v2.base.component.BaseActivityV2
import com.sakura.chat.v2.business.main.data.ChatMessageHistory
import com.sakura.chat.v2.business.main.vm.ChatViewModel
import com.sakura.chat.v2.ext.notifyListItemChanged
import com.sakura.chat.v2.ext.toast
import java.io.File

class ChatActivity : BaseActivityV2() {

    private val vb by lazyVB<ActChatBinding>()
    private val adapter = MyAdapter()

    private val vm by lazyActivityVM<ChatViewModel>()

    private var recorder: AudioRecorder? = null

    private var isEdit = true

    @BundleParams("chatId")
    private val id = 0L

    override fun bindData() {
        vm.newMessage.observe(this) {
            adapter.addData(it)
        }

        vm.replaceMessage.observe(this) {
            val old = it.first
            val new = it.second
            adapter.data.forEachIndexed { index, data ->
                if (data == old) {
                    adapter.data[index] = new
                    adapter.notifyListItemChanged(index)
                    return@observe
                }
            }
        }
    }

    override fun getContentVB() = vb

    override fun init(savedInstanceState: Bundle?) {
        vm.initDefMessages(id)
        vb.rvList.adapter = adapter

        vb.ivChangeState.setOnShakeLessClickListener {
            PermissionBox.with(this)
                .setMajorPermission(Manifest.permission.RECORD_AUDIO)
                .startRequest {
                    changeState()
                }
        }

        vb.tvStartEnd.setOnShakeLessClickListener {
            val r = recorder ?: AudioRecorder()
            recorder = r
            when (r.state) {
                -1, 0 -> {
                    r.start()
                    vb.tvStartEnd.text = "点击暂停"
                }
                1 -> {
                    r.pause()
                    vb.tvStartEnd.text = "点击继续"
                }
                else -> {
                    throw RuntimeException("未知录音错误")
                }
            }
        }

        vb.tvCancelAudio.setOnShakeLessClickListener {
            stopRecorder()
        }

        vb.tvSend.setOnShakeLessClickListener {
            if (isEdit) {
                val st = vb.etText.text?.toString()?.trim()
                if (st.isNullOrEmpty()) {
                    "请输入文本".toast()
                    return@setOnShakeLessClickListener
                }
                vb.etText.setText("")
                vm.sendMessageWithText(id, st)
                toUIContext().hideKeyboard()
            } else {
                val r = recorder
                stopRecorder()
                if (r?.state == 2) {
                    val file = r.fileDir
                    vm.sendMessageWithVoice(id, File(file))
                } else {
                    "请先开始录音".toast()
                }
                toUIContext().hideKeyboard()
            }
        }
    }

    private fun changeState() {
        isEdit = !isEdit
        if (isEdit) {
            vb.ivChangeState.setImageResource(R.drawable.ic_voice_dark)
            vb.etText.isVisible = true
            vb.tvStartEnd.isVisible = false
            vb.tvCancelAudio.isVisible = false
            stopRecorder()
        } else {
            vb.ivChangeState.setImageResource(R.drawable.ic_voice_light)
            vb.etText.isVisible = false
            vb.tvStartEnd.isVisible = true
            vb.tvCancelAudio.isVisible = true
        }
    }

    class MyAdapter : ViewBindingQuickAdapter<AdapterChatBinding, ChatMessageHistory>() {
        override fun convertVB(
            holder: ViewBindingViewHolder<AdapterChatBinding>,
            vb: AdapterChatBinding,
            item: ChatMessageHistory
        ) {
            super.convertVB(holder, vb, item)
            vb.tvMsg.text = item.content
            if (item.role == "user") {
                vb.llRoot.setBackgroundResource(R.color.colorListItemBackground)
                vb.ivAvatar.setImageResource(R.drawable.ic_head)
            } else {
                vb.llRoot.setBackgroundResource(R.color.colorListItemBackgroundSub)
                vb.ivAvatar.setImageResource(R.drawable.ic_openai_dark)
            }
        }
    }

    private fun stopRecorder() {
        vb.tvStartEnd.text = "开始录音"
        recorder?.stop()
        recorder = null
    }

    override fun onDestroy() {
        stopRecorder()
        super.onDestroy()
    }
}