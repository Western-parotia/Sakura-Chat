package com.sakura.chat.v2.ext

import android.content.Intent
import android.net.Uri

/**
 * 打电话
 */
fun Intent.toDial(phone: String): Intent {
    action = Intent.ACTION_DIAL
    data = Uri.parse("tel:${phone}")
    return this
}