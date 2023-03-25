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
import com.sakura.chat.v2.base.dialog.SimpleTwoButtonDialog
import com.sakura.chat.v2.business.main.res.ChatListItemRes
import com.sakura.chat.v2.ext.toast
import com.sakura.chat.v2.key.Keys

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
        adapter.setOnItemLongClickListener { _, _, position ->
            val data = adapter.data[position]
            val tip = data.firstMsg.let { if (it.length > 10) it.substring(0, 10) else it }
            SimpleTwoButtonDialog(
                this,
                content = "是否要删除id:${data.chatId},标题:${tip},这条聊天？",
                confirm = "删除",
                onConfirm = {
                    Keys.CHAT.removeChatMessages(data.chatId)
                })
                .show()
            true
        }
        updateList()
        vb.rvList.adapter = adapter

        vb.ivAdd.setOnShakeLessClickListener {
            if (Keys.Net.OPENAI_API_KEY.spValue.isEmpty()) {
                showAPIKeyInputDialog {
                    ChatActivity.jump(toUIContext(), Keys.CHAT.newChatId())
                }
                return@setOnShakeLessClickListener
            }
            if (adapter.data.size > 100) {
                "本地缓存条数过多，请先删除无用的记录再开启新聊天吧".toast()
                return@setOnShakeLessClickListener
            }
            ChatActivity.jump(toUIContext(), Keys.CHAT.newChatId())

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
            vb.tvTitle.text = "Id:${item.chatId}"
            vb.tvFirstMsg.text = item.firstMsg
        }
    }
}