package com.nmm.chart.v2.ext

import androidx.lifecycle.LiveData
import com.foundation.widget.utils.ui.IUIContext
import com.nmm.chart.v2.base.loading.LoadingEventHelper
import com.nmm.chart.v2.base.loading.SimpleLoadingDialog
import com.nmm.chart.v2.base.loading.XpxLoadingProgress

/**
 * 每次都绑定一个新的
 * 如果想绑定多个，请先合并多个liveData：[LoadingEventHelper.mergeMultiLiveData]
 */
fun IUIContext.bindLoadingEvent(ld: LiveData<XpxLoadingProgress>, cancelable: Boolean = false) {
    SimpleLoadingDialog(this)
        .apply { setCancelable(cancelable) }
        .bindLoadingEvent(ld)
}
