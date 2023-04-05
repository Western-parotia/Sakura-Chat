package com.sakura.chat.core.base.net

import com.sakura.chat.ui.data.ChatReq
import com.sakura.chat.ui.data.ChatRes
import com.sakura.chat.ui.data.VoiceRes
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface GPTAPIService {

    //prompt 可帮助修改返回的文吧语言类型，未查到相关API，但是通过Bing 聊天可以快速了解
    @Multipart
    @POST("/v1/audio/translations")
    suspend fun translationsVoice(
        @Part filePart: MultipartBody.Part,
        @Part model: MultipartBody.Part,
        @Part("prompt") prompt: String = "请转录以下中文语音"
    ): Response<VoiceRes>

    @Headers("Content-Type: application/json")
    @POST("/v1/chat/completions")
    suspend fun goTalkToGPT(
        @Body req: ChatReq
    ): Response<ChatRes>
}