package com.sakura.chat.net

import com.foundation.service.interceptor.LoggerInterceptor
import com.foundation.service.net.utils.addDynamicDomainSkill
import com.sakura.chat.core.ArchConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
    private val okHttpClientBuilder: OkHttpClient.Builder
        get() {
            return OkHttpClient.Builder()
        }

    fun create(): Retrofit {
        okHttpClientBuilder.addDynamicDomainSkill()
        okHttpClientBuilder.addInterceptor(LoggerInterceptor(ArchConfig.debug, true))
        val okHttpClient = okHttpClientBuilder.build()
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openai.com")
            .build()
    }

}