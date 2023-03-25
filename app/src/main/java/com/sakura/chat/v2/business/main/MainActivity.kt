package com.sakura.chat.v2.business.main

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.sakura.chat.databinding.ActMainBinding
import com.sakura.chat.v2.base.component.BaseActivityV2
import com.sakura.chat.v2.ext.jumpToActivity

class MainActivity : BaseActivityV2() {

    private val vb by lazyVB<ActMainBinding>()

//    override fun getContentVB(): ViewBinding = vb

    override fun bindData() {

    }

    override fun init(savedInstanceState: Bundle?) {
//        vb.send.setOnClickListener {
//            vm.sendMessageWithText("问了你几个问题了？")
//
//
//        }
        jumpToActivity<ChatActivity> {
            putExtra("id", /*Keys.SP.newChatId()*/1L)
        }
        finish()
    }
}