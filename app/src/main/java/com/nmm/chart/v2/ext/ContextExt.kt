package com.nmm.chart.v2.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.xpx.arch.ArchConfig

/**
 * @author LJF
 * @Email lijiefeng@51xpx.com
 * @Time  2021/10/29 0029
 * @Description Context扩展
 */


fun Context.safetyStartActivity(intent: Intent) {
    try {
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
        if (ArchConfig.debug) {
            throw  e
        }
    }
}

fun Context.notSafetyStartActivity(intent: Intent) {
    startActivity(intent)
}


fun Activity.safetyStartActivityForResult(intent: Intent, requestCode: Int) {
    try {
        startActivityForResult(intent, requestCode)
    } catch (e: Exception) {
        e.printStackTrace()
        if (ArchConfig.debug) {
            throw  e
        }
    }
}

