package com.sakura.chat.v2.business.main

import android.os.Bundle
import androidx.core.view.isVisible
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
import com.sakura.chat.v2.business.main.vm.MainViewModel
import com.sakura.chat.v2.ext.animateScaleLoop
import com.sakura.chat.v2.ext.toast
import com.sakura.chat.v2.key.ChatChangedState
import com.sakura.chat.v2.key.Keys

class MainActivity : BaseActivityV2() {

    private val vb by lazyAndSetRoot<ActMainBinding>()
    private val vm by lazyActivityVM<MainViewModel>()

    private val adapter = MyAdapter()

    override fun bindData() {
        Keys.MessageBusKey.CHAT_CHANGED.getObserver().observeOnActive(this) { events ->
            //除了数据变更，其他的都刷新列表
            val shouldRefresh = events.any { it.state != ChatChangedState.UPDATE }
            if (shouldRefresh) {
                vm.loadNewList()
            }
        }

        vm.chatListRes.observe(this) {
            adapter.setNewData(it)
            val listEmpty = it.isEmpty()
            vb.rvList.isVisible = !listEmpty
            vb.tvAbout.isVisible = listEmpty
        }

        vm.loadNewList()
    }

    override fun init(savedInstanceState: Bundle?) {
        vb.toolBar.setOnShakeLessClickListener {
            AboutDialog(this).show()
        }

        vb.tvKey.setOnShakeLessClickListener {
            showAPIKeyInputDialog()
        }

        adapter.setOnItemShakeLessClickListener { _, position ->
            ChatActivity.jump(toUIContext(), adapter.data[position].chatId)
        }
        adapter.setOnItemLongClickListener { _, _, position ->
            val data = adapter.data[position]
            val tip = data.firstMsg.let { if (it.length > 10) it.substring(0, 10) else it }
            SimpleTwoButtonDialog(this,
                content = "是否要删除id:${data.chatId},标题:${tip},这条聊天？",
                confirm = "删除",
                onConfirm = {
                    Keys.CHAT.removeChatMessages(data.chatId)
                }).show()
            true
        }
        vb.rvList.adapter = adapter

        vb.btnAdd.setOnShakeLessClickListener {
            if (Keys.Net.OPENAI_API_KEY.spValue.isEmpty()) {
                showAPIKeyInputDialog {
                    ChatActivity.jump(toUIContext(), Keys.CHAT.newChatId())
                }
                return@setOnShakeLessClickListener
            }
            if (adapter.data.size > 1000) {
                "会话超1000了，删几个吧不然手机容易扛不住啊".toast()
                return@setOnShakeLessClickListener
            }
            ChatActivity.jump(toUIContext(), Keys.CHAT.newChatId())
        }
        vb.btnAdd.animateScaleLoop(0.8F, 1200)

    }


    private fun showAPIKeyInputDialog(onSetNewKey: (() -> Unit)? = null) {
        SimpleInputTwoButtonDialog.createWithConfirm(this, "长安粘贴 OPEN AI 的 API KEY") {
            Keys.Net.OPENAI_API_KEY.spValue = it
            "API KEY 更新成功".toast()
            onSetNewKey?.invoke()
        }.show()
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