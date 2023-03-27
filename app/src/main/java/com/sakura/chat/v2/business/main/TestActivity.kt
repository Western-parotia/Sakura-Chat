package com.sakura.chat.v2.business.main

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.sakura.chat.databinding.ActTestBinding
import com.sakura.chat.v2.base.component.BaseActivityV2
import com.sakura.chat.v2.ext.animateTyping

class TestActivity : BaseActivityV2() {
    private val vb by lazyVB<ActTestBinding>()
    override fun getContentVB(): ViewBinding? {
        return vb
    }

    override fun bindData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        vb.btnTextAnim.setOnClickListener {
            vb.tat.setText("")
            vb.tat.animateTyping("正在录音...      ", loop = true)

        }
    }

}