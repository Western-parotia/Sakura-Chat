package com.sakura.chat.v2.base.net

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("/v1/audio/translations")
    suspend fun translationsVoice(
        @Part model: String,
        @Part voiceFile: MultipartBody.Part
    ): Response<String>
}