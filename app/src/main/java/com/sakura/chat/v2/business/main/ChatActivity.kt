package com.sakura.chat.v2.business.main

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.foundation.app.arc.utils.param.BundleParams
import com.foundation.service.permission.PermissionBox
import com.foundation.widget.utils.ext.view.hideKeyboard
import com.foundation.widget.utils.ext.view.jumpToActivity
import com.foundation.widget.utils.ext.view.setOnShakeLessClickListener
import com.foundation.widget.utils.ext.view.toUIContext
import com.foundation.widget.utils.ui.IUIContext
import com.sakura.chat.R
import com.sakura.chat.databinding.ActChatBinding
import com.sakura.chat.databinding.AdapterChatLoadingBinding
import com.sakura.chat.utils.AudioRecorder
import com.sakura.chat.v2.base.component.BaseActivityV2
import com.sakura.chat.v2.business.main.adapter.ChatDetailAdapter
import com.sakura.chat.v2.business.main.data.ChatMessage
import com.sakura.chat.v2.business.main.data.ChatMessageHistory
import com.sakura.chat.v2.business.main.vm.ChatViewModel
import com.sakura.chat.v2.ext.animateRotateLoop
import com.sakura.chat.v2.ext.postMainDelayedLifecycle
import com.sakura.chat.v2.ext.removeGlobalRunnable
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
    private val adapter = ChatDetailAdapter(lifecycle)

    private val vm by lazyActivityVM<ChatViewModel>()

    private var recorder: AudioRecorder? = null

    private var isEdit = true

    private val recordingRun = object : Runnable {
        private var index = 0
        private val list = listOf(".", "..", "...")
        override fun run() {
            vb.tvPoint.text = list[(index++) % list.size]
            postMainDelayedLifecycle(this@ChatActivity, 300, run = this)
        }
    }

    @BundleParams("chatId")
    private val chatId = -0L

    override fun bindData() {
        vm.newMessage.observe(this) {
            adapter.addData(it)
            vb.rvList.scrollToPosition(adapter.itemCount - 1)
        }
        vm.isOutMessage.observe(this) {
            "总聊天字符书已超出50000，请开启新的聊天会话".toast()
        }
        vm.loadingMessage.observe(this) {
            adapter.removeAllFooterView()
            when (it.role) {
                ChatMessage.ERROR -> {
                    adapter.addData(it)
                }
                ChatMessage.LOADING -> {
                    adapter.setFooterView(createFooterView(it))
                }
            }
        }
    }

    private fun createFooterView(data: ChatMessageHistory): View {
        val vb = AdapterChatLoadingBinding.inflate(layoutInflater)
        vb.ivCycle.animateRotateLoop(duration = 800)
        vb.tvContent.text = data.content
        return vb.root
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
                }
        }

        vb.ivSend.setOnShakeLessClickListener {
            if (vm.isOutMessage.value == true) {
                "累计字数超5w，GPT都麻了，重新建立一个新的聊天吧".toast()
                return@setOnShakeLessClickListener
            }
            if (vm.loadingMessage.value?.role == ChatMessage.LOADING) {
                "不要着急，GPT还在想上一个问题呢...".toast()
                return@setOnShakeLessClickListener
            }
            if (isEdit) {
                val st = vb.etText.text?.toString()?.trim()
                if (st.isNullOrEmpty()) {
                    "好歹输入个？吧".toast()
                    return@setOnShakeLessClickListener
                }
                vb.etText.setText("")
                vm.sendMessageWithText(chatId, st)
                toUIContext().hideKeyboard()
            } else {
                val r = recorder
                changeBottomUI()
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
     * 切换底部样式
     */
    private fun changeBottomUI() {
        isEdit = !isEdit
        stopRecorder()
        if (isEdit) {
            vb.ivChangeState.setImageResource(R.drawable.ic_voice_light)
            vb.etText.isVisible = true
            vb.clRecording.isVisible = false
        } else {
            vb.ivChangeState.setImageResource(R.drawable.ic_close_pink_128)
            vb.etText.isVisible = false
            vb.clRecording.isVisible = true
            recorder = AudioRecorder().also { it.start() }
            postMainDelayedLifecycle(this, 300, run = recordingRun)
        }
    }

    override fun onPause() {
        super.onPause()
        if (!isEdit) {
            changeBottomUI()
        }
    }

    private fun stopRecorder() {
        removeGlobalRunnable(recordingRun)
        recorder?.stop()
        recorder = null
    }

    override fun onDestroy() {
        stopRecorder()
        super.onDestroy()
    }
}