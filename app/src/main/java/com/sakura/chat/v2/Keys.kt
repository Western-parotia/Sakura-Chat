package com.sakura.chat.v2

import com.foundation.service.sp.SpList
import com.sakura.chat.v2.business.main.data.ChatMessage

object Keys {
    object SP {
        /**
         * chat数据
         */
        val chatData get() = SpList("chat_data", ChatMessage::class.java)
    }
}