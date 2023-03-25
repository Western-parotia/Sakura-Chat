package com.sakura.chat.net

import com.sakura.chat.v2.key.Keys
import okhttp3.Interceptor
import okhttp3.Response
class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request =
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${Keys.Net.OPENAI_API_KEY.spValue}")
                .build()
        return chain.proceed(request)
    }
}