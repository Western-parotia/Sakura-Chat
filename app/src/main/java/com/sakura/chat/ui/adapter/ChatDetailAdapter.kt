package com.sakura.chat.ui.adapter

import androidx.lifecycle.Lifecycle
import com.foundation.widget.crvadapter.viewbinding.ViewBindingMultiItemAdapter
import com.sakura.chat.R
import com.sakura.chat.databinding.AdapterChatBinding
import com.sakura.chat.ui.data.ChatMessage
import com.sakura.chat.ui.data.ChatMessageHistory
import com.sakura.chat.utils.ext.typingAnimation

class ChatDetailAdapter(val lifecycle: Lifecycle) :
    ViewBindingMultiItemAdapter<ChatMessageHistory>() {
    init {
        //如果是最新的回复 isFresh=true，且是最后一条，且是GPT的 则播放逐字动画
        addMultipleItemBuild<AdapterChatBinding>().setIsThisTypeCallback { adapter, position, item ->
            return@setIsThisTypeCallback item.isFresh
                    && position == adapter.data.lastIndex
                    && item.role == ChatMessage.ROLE_ASSISTANT
        }.setOnBindListViewHolderCallback { _, _, vb, item ->
            vb.ivAvatar.setImageResource(R.drawable.ic_chat_gpt)
            vb.llRoot.setBackgroundResource(R.drawable.sp_chat_item_item_bg_bot)
            vb.tvMsg.typingAnimation(lifecycle, item.content)
            item.isFresh = false
        }.build()

        addDefaultMultipleItem<AdapterChatBinding> { _, _, vb, item ->
            vb.tvMsg.text = item.content
            if (item.role == ChatMessage.ROLE_USER) {
                vb.ivAvatar.setImageResource(R.drawable.ic_chat_sakura_flower)
                vb.llRoot.setBackgroundResource(R.drawable.sp_chat_item_item_bg_user)
            } else {
                vb.ivAvatar.setImageResource(R.drawable.ic_chat_gpt)
                vb.llRoot.setBackgroundResource(R.drawable.sp_chat_item_item_bg_bot)
            }
        }

    }


}