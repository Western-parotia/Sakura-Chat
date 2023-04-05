package com.sakura.chat.key

import com.foundation.service.messagebus.MessageBusData
import com.foundation.service.sp.MjSpUtils
import com.foundation.service.sp.SpList
import com.foundation.service.sp.SpString
import com.sakura.chat.ui.data.ChatMessageHistory

object Keys {
    object MessageBusKey {
        /**
         * 新聊天创建
         */
        val CHAT_CHANGED get() = MessageBusData<ChatChangedEvent>("chat_changed")
    }

    object Net {
        val OPENAI_API_KEY: SpString = SpString("OPENAI_API_KEY")
    }

    object CHAT {
        private var lastChatId = -1L
            get() {
                if (field < 0) {
                    field = chatDataIds.spValue.lastOrNull() ?: 0L
                }
                return field
            }

        val chatDataIds get() = SpList("chatDataIds", Long::class.javaObjectType)

        /**
         * 新聊天的id
         */
        fun newChatId(): Long {
            return ++lastChatId
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
                MjSpUtils.put(getChatKey(chatId), chatMessages)
                MessageBusKey.CHAT_CHANGED.send(
                    ChatChangedEvent(chatId, ChatChangedState.ADD)
                )
            } else {
                MjSpUtils.put(getChatKey(chatId), chatMessages)
                MessageBusKey.CHAT_CHANGED.send(
                    ChatChangedEvent(chatId, ChatChangedState.UPDATE)
                )
            }
        }

        /**
         * 删除聊天消息
         */
        fun removeChatMessages(chatId: Long) {
            val ids = chatDataIds.spValue
            if (ids.contains(chatId)) {
                val newIds = ids.toMutableList()
                newIds.remove(chatId)
                chatDataIds.spValue = newIds
            }
            MjSpUtils.removeKey(getChatKey(chatId))
            MessageBusKey.CHAT_CHANGED.send(
                ChatChangedEvent(chatId, ChatChangedState.REMOVE)
            )
        }
    }
}