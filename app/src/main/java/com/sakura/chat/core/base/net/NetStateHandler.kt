package com.sakura.chat.core.base.net

import androidx.annotation.CallSuper
import com.foundation.service.net.state.NetException
import com.foundation.service.net.state.NetStateListener
import com.foundation.widget.utils.ext.global.log
import com.sakura.chat.utils.ext.postMainSmart
import com.sakura.chat.utils.ext.toast

/**
 *
 * create by zhusw on 5/26/21 14:06
 */
open class NetStateHandler(private val showToast: Boolean = true) : NetStateListener {
    private val TAG = NetStateHandler::class.java.simpleName

    override fun onStart() {
    }

    override fun onSuccess() {
    }

    @CallSuper
    override fun onFailure(tagName: String, e: Throwable) {
        postMainSmart {
            when (e) {
                is NetException -> {
                    "ArchNetStateHandler-NetException:$e".log(TAG)
                    handlerException(
                        ResException(
                            (ResException.HTTP_ERROR.toInt() + e.netCode).toString(),
                            "${e.netCode}-${e.netMsg}",
                            e.netCode,
                            e
                        )
                    )
                }
                is ResException -> {
                    "ArchNetStateHandler-ResException:$e".log(TAG)
                    handlerException(e)
                }
                else -> {
                    "ArchNetStateHandler-other:$e".log(TAG)
                    handlerException(
                        ResException(
                            ResException.UNKNOW,
                            "未知网络层错误",
                            0,
                            e
                        )
                    )

                }
            }
        }
    }

    protected open fun showToast(e: ResException) {
        if ((!showToast) || e.code == ResException.RESPONSE_LIST_EMPTY || e.msg.isBlank()) {
            return
        }
        e.msg.toast()
    }

    private fun handlerException(e: ResException) {

        //子类未处理的错误，统一通过toast 提示
        if (!coverException(e)) {
            e.printStackTrace()
            showToast(e)
        }
    }

    @CallSuper
    open fun coverException(e: ResException): Boolean {
        //默认处理
        return false
    }
}