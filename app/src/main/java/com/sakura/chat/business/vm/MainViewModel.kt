package com.sakura.chat.business.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.foundation.widget.utils.ext.smartPost
import com.sakura.chat.core.net.BaseViewModel
import com.sakura.chat.business.res.ChatListItemRes
import com.sakura.chat.key.Keys

class MainViewModel : BaseViewModel() {
    private val _chatListRes = MutableLiveData<List<ChatListItemRes>>()
    val chatListRes: LiveData<List<ChatListItemRes>> = _chatListRes

    fun loadNewList() {
        val list = Keys.CHAT.chatDataIds.spValue.map {
            ChatListItemRes(it, Keys.CHAT.getChatMessages(it).firstOrNull()?.content ?: "无消息")
        }
        _chatListRes.smartPost(list)
    }
}