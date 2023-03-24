@file:JvmName("ImageViewExt")

package com.sakura.chart.v2.ext

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.foundation.widget.utils.ext.global.emptyThen
import com.foundation.widget.utils.ext.global.isHttp
import com.sakura.chart.R

/**
 *@Desc:
 *-
 *-ImageView 拓展
 *create by zhusw on 4/25/21 15:02
 */

// TODO:
const val IMG_URL = "111"

/**
 * 默认失败样式是1:1的那张图
 */
@JvmOverloads
fun ImageView.loadUrlDef1To1(url: String?, baseUrl: String? = IMG_URL) {
    loadUrl(url, R.drawable.ic_error_img_420_420, baseUrl)
}

/**
 * @param baseUrl 当不是http开头时加的前缀链接
 * @param url 只支持url
 */
@JvmOverloads
fun ImageView.loadUrl(
    url: String?,
    @DrawableRes resId: Int = R.drawable.default_pic,
    baseUrl: String? = IMG_URL,
) {
    if (url.isNullOrEmpty()) {
        setImageResource(resId)
        return
    }

    val fixedUrl = when {
        url.isHttp() || url.startsWith("/storage/") -> {
            url
        }
        else -> {
            (baseUrl emptyThen IMG_URL) + url
        }
    }
    postMainSmart {
        try {
            //解决第一次加载默认资源会变形的问题
            setImageResource(resId)
            Glide.with(context)
                .load(url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL) // 这个属性会引起第一次加载变形，后续再查询根本原因，已在上方自己设置图片了
                //                    .placeholder(res)
                .error(resId)
                .into(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}