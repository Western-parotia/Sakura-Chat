package com.sakura.chat.v2.base.net

import com.sakura.chat.v2.base.net.ResException.Companion.HTTP_ERROR

/**
 * create by zhusw on 5/24/21 20:20
 * @param code 见[ConstantsApi.CODE_1009]...或[HTTP_ERROR]
 */
open class ResException(
    val code: String,
    val msg: String = "",
    val netCode: Int = 200,
    cause: Throwable? = null
) :
    Throwable("chat:$msg:$code:$netCode", cause) {

    companion object {
        const val UNKNOW = "-10000"

        /**
         * http 响应报文响应 非200
         */
        const val HTTP_ERROR = "-10001"
        const val RESPONSE_NULL = "-10002"
        const val RESPONSE_DATA_NULL = "-10003"
        const val RESPONSE_LIST_EMPTY = "-10004"
    }

}