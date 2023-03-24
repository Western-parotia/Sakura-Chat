package com.sakura.chart.v2.ext

import android.widget.Toast
import com.sakura.chart.core.App

/**
 * create by zhusw on 6/9/21 14:13
 */
private const val TAG = "xpxLog"

fun CharSequence?.toast() {
    Toast.makeText(App.get(), this, Toast.LENGTH_SHORT).show()
}
