package com.sakura.chat.v2.business.main

import android.Manifest
import android.os.Bundle
import androidx.core.view.isVisible
import com.foundation.app.arc.utils.param.BundleParams
import com.foundation.service.permission.PermissionBox
import com.foundation.widget.crvadapter.viewbinding.ViewBindingQuickAdapter
import com.foundation.widget.crvadapter.viewbinding.ViewBindingViewHolder
import com.foundation.widget.utils.ext.view.hideKeyboard
import com.foundation.widget.utils.ext.view.jumpToActivity
import com.foundation.widget.utils.ext.view.setOnShakeLessClickListener
import com.foundation.widget.utils.ext.view.toUIContext
import com.foundation.widget.utils.ui.IUIContext
import com.sakura.chat.R
import com.sakura.chat.databinding.ActChatBinding
import com.sakura.chat.databinding.AdapterChatBinding
import com.sakura.chat.utils.AudioRecorder
import com.sakura.chat.v2.base.component.BaseActivityV2
import com.sakura.chat.v2.business.main.data.ChatMessage
import com.sakura.chat.v2.business.main.data.ChatMessageHistory
import com.sakura.chat.v2.business.main.vm.ChatViewModel
import com.sakura.chat.v2.ext.notifyListItemChanged
import com.sakura.chat.v2.ext.toast
import java.io.File

class ChatActivity : BaseActivityV2() {

    companion object {
        fun jump(ui: IUIContext, chatId: Long) {
            ui.jumpToActivity<ChatActivity> {
                putExtra("chatId", chatId)
            }
        }
    }

    private val vb by lazyVB<ActChatBinding>()
    private val adapter = MyAdapter()

    private val vm by lazyActivityVM<ChatViewModel>()

    private var recorder: AudioRecorder? = null

    private var isEdit = true

    @BundleParams("chatId")
    private val chatId = -0L

    override fun bindData() {
        vm.newMessage.observe(this) {
            adapter.addData(it)
            vb.rvList.scrollToPosition(adapter.itemCount - 1)
        }

        vm.replaceMessage.observe(this) {
            val index = it.first
            val new = it.second
            adapter.data[index] = new
            adapter.notifyListItemChanged(index)
        }

        vm.deleteMessages.observe(this) {
            val size = it.count()
            adapter.data.subList(it.first, it.first + size).clear()
            adapter.notifyItemRangeRemoved(it.first, size)
        }

        vm.isOutMessage.observe(this) {

        }
    }

    override fun getContentVB() = vb

    override fun init(savedInstanceState: Bundle?) {
        vb.toolBar.text = "聊天（Id:$chatId）"

        vm.initDefMessages(chatId)
        vb.rvList.adapter = adapter

        vb.ivChangeState.setOnShakeLessClickListener {
            PermissionBox.with(this)
                .setMajorPermission(Manifest.permission.RECORD_AUDIO)
                .startRequest {
                    changeBottomUI()
                    if (!isEdit) {
                        //立即开始录音
                        changeRecorder()
                    }
                }
        }

        vb.tvStartEnd.setOnShakeLessClickListener {
            changeRecorder()
        }

        vb.ivSend.setOnShakeLessClickListener {
            if (vm.isOutMessage.value == true) {
                "当前聊天消息过多，请开启新的聊天".toast()
                return@setOnShakeLessClickListener
            }
            if (isEdit) {
                val st = vb.etText.text?.toString()?.trim()
                if (st.isNullOrEmpty()) {
                    "请输入文本".toast()
                    return@setOnShakeLessClickListener
                }
                vb.etText.setText("")
                vm.sendMessageWithText(chatId, st)
                toUIContext().hideKeyboard()
            } else {
                val r = recorder
                stopRecorder()
                if (r?.state == 2) {
                    val file = r.fileDir
                    vm.sendMessageWithVoice(chatId, File(file))
                } else {
                    "请先开始录音".toast()
                }
                toUIContext().hideKeyboard()
            }
        }
    }

    /**
     * 切换录音状态
     */
    private fun changeRecorder() {
        if (recorder == null) {
            recorder = AudioRecorder().also { it.start() }
            vb.tvStartEnd.text = "点击取消"
        } else {
            stopRecorder()
        }
    }

    /**
     * 切换底部样式
     */
    private fun changeBottomUI() {
        isEdit = !isEdit
        if (isEdit) {
            vb.ivChangeState.setImageResource(R.drawable.ic_voice_dark)
            vb.etText.isVisible = true
            vb.tvStartEnd.isVisible = false
            stopRecorder()
        } else {
            vb.ivChangeState.setImageResource(R.drawable.ic_voice_light)
            vb.etText.isVisible = false
            vb.tvStartEnd.isVisible = true
        }
    }

    private class MyAdapter : ViewBindingQuickAdapter<AdapterChatBinding, ChatMessageHistory>() {
        override fun convertVB(
            holder: ViewBindingViewHolder<AdapterChatBinding>,
            vb: AdapterChatBinding,
            item: ChatMessageHistory
        ) {
            super.convertVB(holder, vb, item)
            vb.tvMsg.text = item.content
            if (item.role == ChatMessage.ROLE_USER) {
                vb.llRoot.setBackgroundResource(R.color.colorListItemBackgroundSub)
                vb.ivAvatar.setImageResource(R.drawable.ic_chat_sakura_flower)
            } else {
                vb.llRoot.setBackgroundResource(R.color.colorListItemBackground)
                vb.ivAvatar.setImageResource(R.drawable.ic_chat_gpt)
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