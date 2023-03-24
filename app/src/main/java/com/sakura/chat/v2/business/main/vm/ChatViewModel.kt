package com.sakura.chat.v2.business.main.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sakura.chat.v2.base.net.AllNetLoadingHandler
import com.sakura.chat.v2.base.net.BaseViewModel
import com.sakura.chat.v2.business.main.data.ChatMessage
import com.sakura.chat.v2.business.main.data.ChatReq
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

class ChatViewModel : BaseViewModel() {
    private val _newMessage = MutableLiveData<ChatMessage>()
    val newMessage: LiveData<ChatMessage> = _newMessage


    fun sendMessageWithVoice(file: File) {
        val handler = AllNetLoadingHandler()
        if (!file.exists() || file.length() <= 0) {
            return handler.onFailure(
                "badFile",
                Throwable("(!file.exists() || file.length() <= 0) = true:$file")
            )
        }
        netLaunch("sendMessageWithVoice") {
            val body = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val voiceText = withBusiness {
                gptApiService.translationsVoice(body)
            }.text
            talkToGPT(voiceText)
        }.start(handler)
    }

    fun sendMessageWithText(text: String) {
        netLaunch("sendMessageWithText") {
            talkToGPT(text)
        }.start(AllNetLoadingHandler())
    }

    private suspend fun talkToGPT(text: String) {
        val chatReq = ChatReq(packageNewMessageList(text))

        val chatRes = withBusiness {
            gptApiService.goTalkToGPT(chatReq)
        }
        _newMessage.value = chatRes.choices[0].message
    }


    private fun packageNewMessageList(newMessage: String): List<ChatMessage> {
        return arrayListOf(
            ChatMessage("user", "你好，我是Sakura,你喜欢蓝色吗"),
            ChatMessage("assistant", "Hi Sakura,我喜欢蓝色"),
            ChatMessage("user", "那你喜欢绿色吗？"),
            ChatMessage("assistant", "我不喜欢"),
            ChatMessage("user", newMessage),
        )
    }

}