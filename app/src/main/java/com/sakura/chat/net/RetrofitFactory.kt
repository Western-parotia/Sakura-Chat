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
        /*这样实现如果再Bearer 中出现异常字符 会导致Retrofit 抛Crash
        所以还是用拦截器替换比较安全，这样出异常网络层可统一拦截到
        **/
//        okHttpClientBuilder.authenticator { _, response ->
//            val request = response.request().newBuilder()
//            request.addHeader("Authorization", "Bearer ${Keys.Net.OPENAI_API_KEY.spValue}")
//            return@authenticator request.build()
//        }
        okHttpClientBuilder.addInterceptor(TokenInterceptor())
        okHttpClientBuilder.addInterceptor(LoggerInterceptor(ArchConfig.debug, true))
        val okHttpClient = okHttpClientBuilder.build()
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openai.com")
            .build()
    }

}