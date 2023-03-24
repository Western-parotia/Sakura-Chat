package com.sakura.chat.v2.ext

import android.view.LayoutInflater
import com.chad.library.adapter.base.BaseViewHolder

val BaseViewHolder.layoutInflater: LayoutInflater get() = LayoutInflater.from(itemView.context)