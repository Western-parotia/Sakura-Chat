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
     * 第一个：替换的index
     * 第二个：新数据
     */
    private val _replaceMessage = MutableLiveData<Pair<Int, ChatMessageHistory>>()
    val replaceMessage: LiveData<Pair<Int, ChatMessageHistory>> = _replaceMessage

    /**
     * 按index删除消息
     */
    private val _deleteMessages = MutableLiveData<IntRange>()
    val deleteMessages: LiveData<IntRange> = _deleteMessages

    /**
     * 消息过长，不允许修改
     */
    private val _isOutMessage = MutableLiveData<Boolean>()
    val isOutMessage: LiveData<Boolean> = _isOutMessage
    val handler = object : AllNetLoadingHandler() {
        override fun onFailure(tagName: String, e: Throwable) {
            super.onFailure(tagName, e)
//            _newMessage.observe()
        }
    }

    fun sendMessageWithVoice(chatId: Long, file: File) {


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
        val ms = Keys.CHAT.getChatMessages(chatId)
        _newMessage.smartPost(ms)
        //超出大小，不允许继续回复
        checkOutMessages(ms)
    }

    private suspend fun talkToGPT(chatId: Long, text: String) {
        val oldText = ChatMessageHistory(ChatMessage.ROLE_USER, text, false)
        _newMessage.smartPost(listOf(oldText))
        val chatMessages = Keys.CHAT.getChatMessages(chatId).toMutableList()
        chatMessages.add(oldText)
        val oldIndex = chatMessages.size - 1
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
        val result = ChatMessageHistory(msg.role, msg.content, false, isFresh = true)

        chatMessages.removeIfIterator { it === oldText }
        chatMessages.add(newText)
        chatMessages.add(result)
        //更新上一条消息的状态
        _replaceMessage.smartPost(oldIndex to newText)
        //返回新的消息
        _newMessage.smartPost(listOf(result))
        //超出大小，不允许继续回复
        checkOutMessages(chatMessages)
        //
        Keys.CHAT.saveChatMessages(chatId, chatMessages)
    }

    private fun checkOutMessages(ms: List<ChatMessageHistory>) {
        if (ms.size > 10) {
            val size = ms.sumOf { it.content.length }
            if (size > 50_000) {
                _isOutMessage.smartPost(true)
            }
        }
    }


    private fun packageNewMessageList(): List<ChatMessage> {
        return arrayListOf(
            ChatMessage(ChatMessage.ROLE_USER, "你好，我是Sakura,你喜欢蓝色吗"),
            ChatMessage(ChatMessage.ROLE_ASSISTANT, "Hi Sakura,我喜欢蓝色"),
            ChatMessage(ChatMessage.ROLE_USER, "那你喜欢绿色吗？"),
            ChatMessage(ChatMessage.ROLE_ASSISTANT, "我不喜欢"),
        )
    }

    fun testSendMessage(role: String) {
        _newMessage.smartPost(
            listOf(
                ChatMessageHistory(
                    role, "。。。",
                    true, isFresh = false
                )
            )
        )

    }

}