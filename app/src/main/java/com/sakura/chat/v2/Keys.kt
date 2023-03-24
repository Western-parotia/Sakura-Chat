package com.sakura.chat.v2

import com.foundation.service.sp.MjSpUtils
import com.foundation.service.sp.SpList
import com.sakura.chat.v2.business.main.data.ChatMessageHistory

object Keys {
    object SP {
        private val chatDataIds get() = SpList("chatDataIds", Long::class.javaObjectType)

        /**
         * 新聊天的id
         */
        fun newChatId(): Long {
            val ids = chatDataIds.spValue.toMutableList()
            val last = ids.lastOrNull() ?: 0L
            val new = last + 1L
            ids.add(new)
            chatDataIds.spValue = ids
            return new
        }

        private fun getChatKey(chatId: Long) = "chatData:$chatId"

        /**
         * 获取聊天消息
         */
        fun getChatMessages(chatId: Long) = MjSpUtils.getList(
            getChatKey(chatId),
            ChatMessageHistory::class.java
        )

        /**
         * 保存聊天消息
         */
        fun saveChatMessages(chatId: Long, chatMessages: MutableList<ChatMessageHistory>) {
            val ids = chatDataIds.spValue
            if (!ids.contains(chatId)) {
                val newIds = ids.toMutableList()
                newIds.add(chatId)
                newIds.sort()
                chatDataIds.spValue = newIds
            }
            MjSpUtils.put(getChatKey(chatId), chatMessages)
        }

        /**
         * 删除聊天消息
         */
        fun removeChatMessages(chatId: Long) {
            val ids = chatDataIds.spValue
            if (ids.contains(chatId)) {
                ids.toMutableList().remove(chatId)
                chatDataIds.spValue = ids
            }
            MjSpUtils.removeKey(getChatKey(chatId))
        }
    }
}