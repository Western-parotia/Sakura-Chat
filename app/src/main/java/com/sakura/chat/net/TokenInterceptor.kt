package com.sakura.chat.net

import okhttp3.Interceptor
import okhttp3.Response

private val token = "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"

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request =
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        return chain.proceed(request)
    }
}