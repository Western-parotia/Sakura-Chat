package com.sakura.chat.v2.ext

import android.animation.*
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.view.animation.*
import android.widget.TextView
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

//fun TextView.animateTyping(text: CharSequence, duration: Long = 2000L) {
//    var progress = 0
//    val animator = ValueAnimator.ofInt(0, text.length).apply {
//        this.duration = duration
//
//        addUpdateListener { valueAnimator ->
//            val value = valueAnimator.animatedValue as Int
//            progress = value
//            setText(text.subSequence(0, value))
//        }
//        addListener(object : AnimatorListenerAdapter() {
//            override fun onAnimationEnd(animation: Animator?) {
//
//                Handler(Looper.getMainLooper()).postDelayed({
//                    start()
//                }, 800L)
//            }
//        })
//    }
//    animator.start()
//}


class TypingTextView(context: Context, attr: AttributeSet) : AppCompatTextView(context, attr) {
    var myText = "1"
        set(value) {
            setText(value)
        }
}

fun TypingTextView.animateTyping(text: String) {
    val array: Array<String> = text.map {
        it.toString()
    }.toTypedArray()

    val evaluator = TypeEvaluator<String> { fraction, startValue, endValue ->
        val starIndex = getText().length
        val endIndex = min((starIndex + 1), (text.length))
        text.substring(0, endIndex)
    }
    val animator = ObjectAnimator.ofObject(this, "myText", evaluator, *array)
    animator.interpolator = AccelerateDecelerateInterpolator()
    animator.duration = 2000L
    animator.start()
}
