package com.sakura.chat.key

class ChatChangedEvent(val chatId: Long, val state: ChatChangedState)
enum class ChatChangedState {
    ADD, REMOVE, UPDATE
}