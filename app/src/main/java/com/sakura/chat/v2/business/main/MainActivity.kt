package com.sakura.chat.v2.business.main

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding
import com.foundation.widget.crvadapter.viewbinding.ViewBindingQuickAdapter
import com.foundation.widget.crvadapter.viewbinding.ViewBindingViewHolder
import com.foundation.widget.utils.ext.view.setOnItemShakeLessClickListener
import com.foundation.widget.utils.ext.view.setOnShakeLessClickListener
import com.foundation.widget.utils.ext.view.toUIContext
import com.sakura.chat.databinding.ActMainBinding
import com.sakura.chat.databinding.AdapterChatListBinding
import com.sakura.chat.v2.base.component.BaseActivityV2
import com.sakura.chat.v2.base.dialog.SimpleInputTwoButtonDialog
import com.sakura.chat.v2.business.main.res.ChatListItemRes
import com.sakura.chat.v2.ext.toast
import com.sakura.chat.v2.key.Keys
import java.security.Key

class MainActivity : BaseActivityV2() {

    private val vb by lazyVB<ActMainBinding>()

    private val adapter = MyAdapter()

    override fun getContentVB(): ViewBinding = vb

    override fun bindData() {
        Keys.MessageBusKey.CHAT_CHANGED.getObserver().observeOnActive(this) {
            updateList()
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        vb.tvKey.setOnShakeLessClickListener {
            showAPIKeyInputDialog()
        }

        adapter.setOnItemShakeLessClickListener { _, position ->
            ChatActivity.jump(toUIContext(), adapter.data[position].chatId)
        }
        updateList()
        vb.rvList.adapter = adapter

        vb.ivAdd.setOnShakeLessClickListener {
            if (Keys.Net.OPENAI_API_KEY.spValue.isEmpty()) {
                showAPIKeyInputDialog {
                    ChatActivity.jump(toUIContext(), Keys.CHAT.newChatId())
                }
            } else {
                ChatActivity.jump(toUIContext(), Keys.CHAT.newChatId())
            }

        }
    }

    private fun showAPIKeyInputDialog(onSetNewKey: (() -> Unit)? = null) {
        SimpleInputTwoButtonDialog.createWithConfirm(this, "长安粘贴 OPEN AI 的 API KEY") {
            Keys.Net.OPENAI_API_KEY.spValue = it
            "API KEY 更新成功".toast()
            onSetNewKey?.invoke()
        }.show()
    }

    private fun updateList() {
        val list = Keys.CHAT.chatDataIds.spValue.map {
            ChatListItemRes(it, Keys.CHAT.getChatMessages(it).firstOrNull()?.content ?: "无消息")
        }
        adapter.setNewData(list)
        val listEmpty = list.isEmpty()
        vb.rvList.isVisible = !listEmpty
        vb.tvAbout.isVisible = listEmpty
    }

    private class MyAdapter : ViewBindingQuickAdapter<AdapterChatListBinding, ChatListItemRes>() {
        override fun convertVB(
            holder: ViewBindingViewHolder<AdapterChatListBinding>,
            vb: AdapterChatListBinding,
            item: ChatListItemRes
        ) {
            super.convertVB(holder, vb, item)
            vb.tvFirstMsg.text = item.firstMsg
        }
    }
}