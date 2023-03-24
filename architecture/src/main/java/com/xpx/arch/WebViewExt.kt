package com.xpx.arch

import android.util.Log
import android.webkit.WebView
import androidx.core.view.doOnDetach

fun WebView.refreshTitle(callback: (title: String) -> Unit) {
    val action = Runnable {
        val tagTitle: String = title ?: ""
        val urlStr = url ?: ""
        val pageTitle = if (tagTitle.isNotEmpty() && !urlStr.contains(tagTitle)) {
            tagTitle
        } else {
            null
        }
        "title:$title  pageTitle:$pageTitle url:$url".log("getWebTitle")
        pageTitle?.let {
            callback(it)
        }
    }
    postDelayed(action, 500L)
    doOnDetach {
        removeCallbacks(action)
    }
}

private fun String.log(secondTag: String) {
    if (BuildConfig.DEBUG) {
        Log.i("xpxLog", "$secondTag $this")
    }
}