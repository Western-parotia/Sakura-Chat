package com.sakura.chart.v2.base.loading

import androidx.lifecycle.MutableLiveData
import com.sakura.chart.v2.ext.smartPost

/**
 * create by zhusw on 7/28/21 13:59
 */
class LoadingControl(private val liveData: MutableLiveData<XpxLoadingProgress>?) {

    fun doStart() {
        liveData?.smartPost(XpxLoadingProgress(DataLoadingEvent.LOADING))
    }

    fun doSuccess() {
        liveData?.smartPost(XpxLoadingProgress(DataLoadingEvent.SUCCESS))
    }

    fun doEmpty() {
        liveData?.smartPost(XpxLoadingProgress(DataLoadingEvent.EMPTY))
    }

    fun doFail(code: String, msg: String) {
        liveData?.smartPost(XpxLoadingProgress(DataLoadingEvent.FAILURE, code, msg))
    }

}