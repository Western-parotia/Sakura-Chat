package com.nmm.chart.v2.business.main

import android.os.Bundle
import com.nmm.chart.databinding.ActMainBinding
import com.nmm.chart.v2.base.component.BaseActivityV2
import com.nmm.chart.v2.ext.jumpToActivity

class MainActivity : BaseActivityV2() {

    private val vb by lazyAndSetRoot<ActMainBinding>()

    override fun bindData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        jumpToActivity<ChatActivity>()
    }
}