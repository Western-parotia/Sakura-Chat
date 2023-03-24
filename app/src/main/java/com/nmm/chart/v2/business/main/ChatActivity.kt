package com.nmm.chart.v2.business.main

import android.os.Bundle
import com.foundation.widget.crvadapter.viewbinding.ViewBindingQuickAdapter
import com.foundation.widget.crvadapter.viewbinding.ViewBindingViewHolder
import com.nmm.chart.databinding.ActChatBinding
import com.nmm.chart.databinding.AdapterChatBinding
import com.nmm.chart.v2.base.component.BaseActivityV2

class ChatActivity : BaseActivityV2() {

    private val vb by lazyAndSetRoot<ActChatBinding>()
    private val adapter = MyAdapter()

    override fun bindData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        vb.rvList.adapter = adapter
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