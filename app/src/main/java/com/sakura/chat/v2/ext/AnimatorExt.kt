package com.sakura.chat.v2.ext

import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation

fun View.animateScaleLoop(
    scale: Float,
    duration: Long,
    start: Boolean = true
): Animation {
    val animation = ScaleAnimation(
        1F, scale, 1F, scale, Animation.RELATIVE_TO_SELF,
        0.5F, Animation.RELATIVE_TO_SELF, 0.5F
    )
    animation.repeatCount = ScaleAnimation.INFINITE
    animation.duration = duration
    animation.repeatMode = ScaleAnimation.REVERSE
    animation.interpolator = LinearInterpolator()
    if (start) {
        this.startAnimation(animation)
    }
    return animation
}

fun View.animateRotateLoop(
    fromDegrees: Float = 0F,
    toDegrees: Float = 361F,
    duration: Long,
    start: Boolean = true
): Animation {
    val animation = RotateAnimation(
        fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF,
        0.5F, Animation.RELATIVE_TO_SELF, 0.5F
    )
    animation.repeatCount = ScaleAnimation.INFINITE
    animation.duration = duration
    animation.repeatMode = ScaleAnimation.INFINITE
    animation.interpolator = LinearInterpolator()
    if (start) {
        this.startAnimation(animation)
    }
    return animation
}