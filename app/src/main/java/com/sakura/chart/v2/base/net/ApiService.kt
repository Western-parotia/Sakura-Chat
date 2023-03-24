package com.sakura.chart.v2.base.net

import com.sakura.chart.core.ApiManager
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        @JvmField
        val apiService: ApiService =
            ApiManager.getSingleton().getService(ApiService::class.java)
    }

    @GET("111")
    suspend fun getAuthCode(
        @Query("cmd") cmd: String,
        @Query("phone") phone: String,
        @Query("purpose") purpose: String,
        @Query("distinct_id") distinct_id: String
    ): Response<String>
}