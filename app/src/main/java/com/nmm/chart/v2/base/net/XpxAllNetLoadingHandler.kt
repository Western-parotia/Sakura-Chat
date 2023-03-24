package com.nmm.chart.v2.base.net

import com.nmm.chart.v2.base.loading.LoadingControl

/**
 * 利用LoadingControl 处理加载状态通知的处理器
 * create by zhusw on 5/26/21 14:06
 *
 * @param onCoverInterceptor 异常拦截回调，和[coverException]一样，true则拦截此异常不走默认处理
 *                           注意：true表示完全处理，[loadingControl]也不会发送事件
 */
open class XpxAllNetLoadingHandler(
    private val loadingControl: LoadingControl? = null,
    toastAble: Boolean = true,
    private val onCoverInterceptor: ((e: XpxResException, loadingControl: LoadingControl?) -> Boolean)? = null
) : XpxNetStateHandler(toastAble) {
    override fun onStart() {
        super.onStart()
        loadingControl?.doStart()
    }

    override fun onSuccess() {
        super.onSuccess()
        loadingControl?.doSuccess()
    }

    override fun coverException(e: XpxResException): Boolean {
        if (onCoverInterceptor?.invoke(e, loadingControl) == true) {
            return true
        }
        if (e.code == XpxResException.RESPONSE_DATA_NULL || e.code == XpxResException.RESPONSE_LIST_EMPTY) {
            loadingControl?.doEmpty()
            return true
        }
        if (super.coverException(e)) {
            return true
        }
        loadingControl?.doFail(e.code, e.msg)
        return false
    }
}