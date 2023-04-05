package com.sakura.chat.utils.ext

import android.widget.Toast
import com.sakura.chat.core.App

/**
 * create by zhusw on 6/9/21 14:13
 */

fun CharSequence?.toast() {
    Toast.makeText(App.get(), this, Toast.LENGTH_SHORT).show()
}
