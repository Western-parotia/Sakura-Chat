package com.mj.debug

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.xpx.arch.ArchConfig
import kotlin.system.measureNanoTime

//自测时建议使用此类数字，防止打包出去
//此类为开发专用数字，打debugProguard和release将直接报错

const val DEBUG_INT: Int = 88888888
const val DEBUG_LONG: Long = 88888888
const val DEBUG_DOUBLE: Double = 88888888.88888888
const val DEBUG_STRING = "开发测试的字符串"
const val DEBUG_LONG_STRING = "开发测试的字符串开发测试的字符串开发测试的字符串开发测试的字符串开发测试的字符串开发测试的字符串" +
        "开发测试的字符串开发测试的字符串开发测试的字符串开发测试的字符串开发测试的字符串开发测试的字符串"
const val DEBUG_STRING_NUMBER = "8888888888"

val isDebug = ArchConfig.debug

/**
 * debug下发handler
 * @param delayMillis <0表示立即执行不发handler
 */
fun runDebug(delayMillis: Long = -1, run: () -> Unit) {
    if (delayMillis < 0) {
        run.invoke()
    } else {
        Handler(Looper.getMainLooper()).postDelayed(run, delayMillis)
    }
}

fun String.printDebug() {
    this.logDebug()
}

fun String.logDebug() {
    Log.e(DEBUG_STRING, "测试测试测试:$this ")
}

inline fun <T> String.logDebug(f: () -> T): T {
    val res: T
    val t = measureNanoTime {
        res = f.invoke()
    }
    "${this}，耗时：${t.toDouble() / 1_000_000}".logDebug()
    return res
}
