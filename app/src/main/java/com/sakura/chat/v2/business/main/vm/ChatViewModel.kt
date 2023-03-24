package com.sakura.chat.v2.business.main.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sakura.chat.v2.Keys
import com.sakura.chat.v2.base.net.AllNetLoadingHandler
import com.sakura.chat.v2.base.net.BaseViewModel
import com.sakura.chat.v2.business.main.data.ChatMessage
import com.sakura.chat.v2.business.main.data.ChatReq
import com.sakura.chat.v2.ext.smartPost
import okhttp3.MediaType
import okhttp3.MultipartBody
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
            val filePart = MultipartBody.Part.createFormData("file", file.name, body)
            val model = MultipartBody.Part.createFormData("model", "whisper-1")
            val voiceText = withBusiness {
                gptApiService.translationsVoice(filePart, model)
            }.text
            talkToGPT(voiceText)
        }.start(handler)
    }

    fun sendMessageWithText(text: String) {
        netLaunch("sendMessageWithText") {
            _newMessage.smartPost(ChatMessage("user", text))
            talkToGPT(text)
        }.start(AllNetLoadingHandler())
    }

    fun getDefChatMessages() = Keys.SP.chatData.spValue

    private suspend fun talkToGPT(text: String) {
        val chatReq = ChatReq(packageNewMessageList(text))

        val chatRes = withBusiness {
            gptApiService.goTalkToGPT(chatReq)
        }
        _newMessage.smartPost(chatRes.choices[0].message)
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