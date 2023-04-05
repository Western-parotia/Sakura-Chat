package com.sakura.chat.ui.data

data class ChatRes(
    val choices: List<Choice> = arrayListOf(),
    val created: Int = 0,
    val id: String = "",
    val model: String = "",
    val `object`: String = "",
    val usage: Usage = Usage(0, 0, 0)
)

data class Choice(
    val finish_reason: String = "",
    val index: Int = 0,
    val message: ChatMessage = ChatMessage("", "")
)

data class Usage(
    val completion_tokens: Int = 0,
    val prompt_tokens: Int = 0,
    val total_tokens: Int = 0
)
