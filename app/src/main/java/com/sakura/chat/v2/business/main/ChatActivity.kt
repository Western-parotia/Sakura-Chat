package com.sakura.chat.v2.business.main

import android.os.Bundle
import com.foundation.widget.crvadapter.viewbinding.ViewBindingQuickAdapter
import com.foundation.widget.crvadapter.viewbinding.ViewBindingViewHolder
import com.sakura.chat.databinding.ActChatBinding
import com.sakura.chat.databinding.AdapterChatBinding
import com.sakura.chat.v2.Keys
import com.sakura.chat.v2.base.component.BaseActivityV2

class ChatActivity : BaseActivityV2() {

    private val vb by lazyVB<ActChatBinding>()
    private val adapter = MyAdapter()

    override fun bindData() {
    }

    override fun getContentVB() = vb

    override fun init(savedInstanceState: Bundle?) {
        vb.rvList.adapter = adapter
        adapter.setNewData(Keys.SP.chatData.spValue)
    }

    class MyAdapter : ViewBindingQuickAdapter<AdapterChatBinding, String>() {
        override fun convertVB(
            holder: ViewBindingViewHolder<AdapterChatBinding>,
            vb: AdapterChatBinding,
            item: String
        ) {
            super.convertVB(holder, vb, item)
        }
    }
}