package com.sakura.chat.net

import com.foundation.service.interceptor.LoggerInterceptor
import com.foundation.service.net.utils.addDynamicDomainSkill
import com.sakura.chat.core.ArchConfig
import com.sakura.chat.v2.key.Keys
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {

    fun create(): Retrofit {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        okHttpClientBuilder.addDynamicDomainSkill()
        okHttpClientBuilder.authenticator { _, response ->
            val request = response.request().newBuilder()
            val token = Keys.Net.OPENAI_API_KEY.spValue
            request.addHeader("Authorization", "Bearer $token")
            return@authenticator request.build()
        }
        okHttpClientBuilder.addInterceptor(LoggerInterceptor(ArchConfig.debug, true))
        val okHttpClient = okHttpClientBuilder.build()
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openai.com")
            .build()
    }

}