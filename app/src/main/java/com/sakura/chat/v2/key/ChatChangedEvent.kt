package com.sakura.chat.v2.key

class ChatChangedEvent(val chatId: Long, val state: ChatChangedState)
enum class ChatChangedState {
    ADD, REMOVE, UPDATE
}