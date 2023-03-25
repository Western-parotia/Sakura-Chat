package com.sakura.chat.v2.business.main.data

/**
 * {
"model": "gpt-3.5-turbo",
"messages": [
{ "role": "user", "content": "我说A,请重复" },
{ "role": "system", "content": "A" },
{ "role": "user", "content": "我刚才让你重复什么？" }
]
}
 */
data class ChatReq(val messages: List<ChatMessage>, val model: String = "gpt-3.5-turbo")

data class ChatMessage(val role: String = "", val content: String = "") {
    companion object {
        const val ROLE_USER = "user"
    }
}

/**
 * 保存的消息记录
 * @param isSuccess 是否发送成功
 */
data class ChatMessageHistory(val role: String, val content: String, val isSuccess: Boolean)