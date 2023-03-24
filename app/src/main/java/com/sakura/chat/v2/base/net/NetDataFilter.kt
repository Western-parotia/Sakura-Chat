package com.sakura.chat.v2.base.net

import com.foundation.service.net.NetRC.withResponse
import retrofit2.Response

/**
 * create by zhusw on 7/14/21 14:29
 */
object NetDataFilter {

    /**
     * 业务层处理
     * 无根数据结构
     * 底层无法识别具体的响应数据类型，需要上层调用者主动判空
     * */
    suspend fun <E> withBusiness(block: suspend () -> Response<E>): E {
        return withResponse(block)
            ?: throw ResException(ResException.RESPONSE_NULL)
    }

}