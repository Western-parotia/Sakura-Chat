package com.nmm.chart.v2.ext

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.nmm.chart.core.App
import com.xpx.arch.ArchConfig

/**
 * create by zhusw on 6/9/21 14:13
 */
private const val TAG = "xpxLog"

fun CharSequence?.toast() {
    Toast.makeText(App.get(), this, Toast.LENGTH_SHORT).show()
}
