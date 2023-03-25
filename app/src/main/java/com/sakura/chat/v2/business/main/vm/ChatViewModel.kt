package com.sakura.chat.v2.business.main.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sakura.chat.v2.base.net.AllNetLoadingHandler
import com.sakura.chat.v2.base.net.BaseViewModel
import com.sakura.chat.v2.business.main.data.ChatMessage
import com.sakura.chat.v2.business.main.data.ChatMessageHistory
import com.sakura.chat.v2.business.main.data.ChatReq
import com.sakura.chat.v2.ext.removeIfIterator
import com.sakura.chat.v2.ext.smartPost
import com.sakura.chat.v2.key.Keys
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ChatViewModel : BaseViewModel() {
    private val _newMessage = MutableLiveData<List<ChatMessageHistory>>()
    val newMessage: LiveData<List<ChatMessageHistory>> = _newMessage

    /**
     * 数据发生变更
     * 第一个：源数据
     * 第二个：新数据
     */
    private val _replaceMessage = MutableLiveData<Pair<ChatMessageHistory, ChatMessageHistory>>()
    val replaceMessage: LiveData<Pair<ChatMessageHistory, ChatMessageHistory>> = _replaceMessage


    fun sendMessageWithVoice(chatId: Long, file: File) {

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
            talkToGPT(chatId, voiceText)
        }.start(handler)
    }

    fun sendMessageWithText(chatId: Long, text: String) {
        netLaunch("sendMessageWithText") {
            talkToGPT(chatId, text)
        }.start(AllNetLoadingHandler())
    }

    fun initDefMessages(chatId: Long) {
        _newMessage.smartPost(Keys.CHAT.getChatMessages(chatId))
    }

    private suspend fun talkToGPT(chatId: Long, text: String) {
        val oldText = ChatMessageHistory("user", text, false)
        _newMessage.smartPost(listOf(oldText))
        val chatMessages = Keys.CHAT.getChatMessages(chatId).toMutableList()
        chatMessages.add(oldText)
        val chatReq = ChatReq(
            packageNewMessageList() + chatMessages.mapNotNull {
                if (it.isSuccess) ChatMessage(
                    it.role,
                    it.content
                ) else null
            }
        )
        Keys.CHAT.saveChatMessages(chatId, chatMessages)

        val chatRes = withBusiness {
            gptApiService.goTalkToGPT(chatReq)
        }
        val msg = chatRes.choices[0].message
        val newText = oldText.copy(isSuccess = true)
        val result = ChatMessageHistory(msg.role, msg.content, false)
        _replaceMessage.smartPost(oldText to newText)
        _newMessage.smartPost(listOf(result))

        chatMessages.removeIfIterator { it === oldText }
        chatMessages.add(newText)
        chatMessages.add(result)
        Keys.CHAT.saveChatMessages(chatId, chatMessages)
    }


    private fun packageNewMessageList(): List<ChatMessage> {
        return arrayListOf(
            ChatMessage("user", "你好，我是Sakura,你喜欢蓝色吗"),
            ChatMessage("assistant", "Hi Sakura,我喜欢蓝色"),
            ChatMessage("user", "那你喜欢绿色吗？"),
            ChatMessage("assistant", "我不喜欢"),
        )
    }

}