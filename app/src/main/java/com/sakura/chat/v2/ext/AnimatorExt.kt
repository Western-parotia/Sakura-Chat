package com.sakura.chat.v2.ext

import android.animation.*
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.*
import androidx.appcompat.widget.AppCompatTextView
import java.lang.Integer.min

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


class TypingAnimationTextView(context: Context, attr: AttributeSet) :
    AppCompatTextView(context, attr) {

    var stagingText: String = ""
    var textIndex = 0
        set(value) {
            setText(stagingText.subSequence(0, value))
        }
    var typingAnimation: ObjectAnimator? = null
}

fun TypingAnimationTextView.animateTyping(text: String) {
    typingAnimation?.let {
        if (it.isRunning) {
            it.cancel()
        }
    }
    stagingText = text
    val indexSize = stagingText.length - 1
    val evaluator = TypeEvaluator<Int> { fraction, startValue, endValue ->
        val result = startValue + (fraction * (endValue - startValue)).toInt()
        result
    }
    val animator = ObjectAnimator.ofObject(this, "textIndex", evaluator, 0, 100, indexSize)
    animator.interpolator = AccelerateInterpolator(2F)
    animator.duration = 10000L
    animator.start()
}