package com.sakura.chat.v2.base.net

import com.sakura.chat.v2.business.main.data.ChatReq
import com.sakura.chat.v2.business.main.data.ChatRes
import com.sakura.chat.v2.business.main.data.VoiceRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface GPTAPIService {

    @Multipart
    @POST("/v1/audio/translations")
    suspend fun translationsVoice(
        @Part("file") filePart: MultipartBody.Part,
        @Part model: String = "whisper-1"
    ): Response<VoiceRes>

    @Headers("Content-Type: application/json")
    @POST("/v1/chat/completions")
    suspend fun goTalkToGPT(
        @Body req: ChatReq
    ): Response<ChatRes>
}