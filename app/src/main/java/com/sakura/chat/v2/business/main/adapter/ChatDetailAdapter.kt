package com.sakura.chat.v2.business.main.adapter

import com.foundation.widget.crvadapter.viewbinding.ViewBindingMultiItemAdapter
import com.sakura.chat.R
import com.sakura.chat.databinding.AdapterChatBinding
import com.sakura.chat.v2.business.main.data.ChatMessage
import com.sakura.chat.v2.business.main.data.ChatMessageHistory
import com.sakura.chat.v2.ext.animateTyping

class ChatDetailAdapter : ViewBindingMultiItemAdapter<ChatMessageHistory>() {
    init {
        //如果是最新的回复 isFresh=true，且是最后一条，且是GPT的 则播放逐字动画
        addMultipleItemBuild<AdapterChatBinding>().setIsThisTypeCallback { adapter, position, item ->
            return@setIsThisTypeCallback item.isFresh
                    && position == adapter.data.lastIndex
                    && item.role == ChatMessage.ROLE_ASSISTANT
        }.setOnBindListViewHolderCallback { _, _, vb, item ->
            vb.ivAvatar.setImageResource(R.drawable.ic_chat_gpt)
            vb.llRoot.setBackgroundResource(R.color.colorListItemBackground)
            vb.tvMsg.animateTyping(item.content)
        }.build()

        addDefaultMultipleItem<AdapterChatBinding> { _, _, vb, item ->
            vb.tvMsg.text = item.content
            if (item.role == ChatMessage.ROLE_USER) {
                vb.ivAvatar.setImageResource(R.drawable.ic_chat_sakura_flower)
                vb.llRoot.setBackgroundResource(R.color.colorListItemBackgroundSub)
            } else {
                vb.ivAvatar.setImageResource(R.drawable.ic_chat_gpt)
                vb.llRoot.setBackgroundResource(R.color.colorListItemBackground)
            }
        }

    }


}