package com.sakura.chat.v2

import com.foundation.service.sp.SpList

object Keys {
    object SP {
        /**
         * chat数据
         */
        val chatData get() = SpList("chat_data", String::class.java)
    }
}