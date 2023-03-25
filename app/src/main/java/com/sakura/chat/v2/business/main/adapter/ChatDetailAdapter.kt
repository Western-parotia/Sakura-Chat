package com.sakura.chat.v2.business.main.adapter

import com.foundation.widget.crvadapter.viewbinding.ViewBindingMultiItemAdapter
import com.foundation.widget.crvadapter.viewbinding.ViewBindingViewHolder
import com.sakura.chat.R
import com.sakura.chat.databinding.AdapterChatBinding
import com.sakura.chat.databinding.AdapterChatLoadingBinding
import com.sakura.chat.v2.business.main.data.ChatMessage
import com.sakura.chat.v2.business.main.data.ChatMessageHistory
import com.sakura.chat.v2.ext.animateRotateLoop

class ChatDetailAdapter : ViewBindingMultiItemAdapter<ChatMessageHistory>() {
    init {
        addMultipleItemBuild<AdapterChatLoadingBinding>()
            .setIsThisTypeCallback { _, _, item ->
                return@setIsThisTypeCallback item.role == ChatMessage.LOADING
            }.setOnBindListViewHolderCallback { _, _, vb, item ->
                vb.ivCycle.animateRotateLoop(duration = 800)
                vb.tvContent.text = item.content
            }.build()

        addDefaultMultipleItem<AdapterChatBinding> { adapter, holder, vb, item ->
            vb.tvMsg.text = item.content
            if (item.role == ChatMessage.ROLE_USER) {
                vb.ivAvatar.setImageResource(R.drawable.ic_chat_sakura_flower)
                vb.llRoot.setBackgroundResource(R.color.colorListItemBackgroundSub)
            } else {
                vb.ivAvatar.setImageResource(R.drawable.ic_chat_gpt)
                vb.llRoot.setBackgroundResource(R.color.colorListItemBackground)
            }
            //如果是最新的回复 isFresh=true，且是最后一条，且是GPT的 则播放逐字动画
            if (item.isFresh && holder.adapterLayoutPosition == adapter.data.lastIndex && item.role == ChatMessage.ROLE_ASSISTANT) {

            }
        }

    }


}