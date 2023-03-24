package com.nmm.chart.v2.ext

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.xpx.arch.ArchConfig

val Int.toDrawable
    get() = this.toDrawable()

@JvmOverloads
fun Int.toDrawable(context: Context = ArchConfig.app) = ContextCompat.getDrawable(context, this)

fun Int.tintDrawable(@ColorInt colorInt: Int): Drawable? {
    val drawable = this.toDrawable
    drawable?.let { DrawableCompat.setTint(it, colorInt) }
    return drawable
}