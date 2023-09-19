package com.example.chatapplication.messaging_in_chatroom.display_chat_messages.oncreate_viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.R
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.constants.MessageConstants
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.received.image_message_with_time.ReceivedImageChatMessageWithTimeViewHolder
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.received.image_message_without_time.ReceivedImageChatMessageWithoutTimeViewHolder
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.received.text_message_with_time.ReceivedTextChatMessageWithTimeViewHolder
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.received.text_message_without_time.ReceivedTextChatMessageWithoutTimeViewHolder
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.sent.image_message_with_time.SentImageChatMessageWithTimeViewHolder
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.sent.image_message_without_time.SentImageChatMessageWithoutTimeViewHolder
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.sent.text_message_with_time.SentTextChatMessageWithTimeViewHolder
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.sent.text_message_without_time.SentTextChatMessageWithoutTimeViewHolder

class OnCreateViewHolder {



    companion object{
        fun getOnCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
            when (viewType) {
                MessageConstants.SENT_TEXT_MESSAGE_WITHOUT_TIME -> {
                    val view: View = LayoutInflater.from(context)
                        .inflate(R.layout.message_item_without_time, parent, false)
                    return SentTextChatMessageWithoutTimeViewHolder(view)
                }
                MessageConstants.SENT_TEXT_MESSAGE_WITH_TIME -> {
                    val view: View = LayoutInflater.from(context)
                        .inflate(R.layout.message_item_with_time, parent, false)
                    return SentTextChatMessageWithTimeViewHolder(view)
                }

                MessageConstants.RECEIVED_TEXT_MESSAGE_WITHOUT_TIME -> {
                    val view: View = LayoutInflater.from(context)
                        .inflate(R.layout.chatroom_received_message_item_without_time, parent, false)
                    return ReceivedTextChatMessageWithoutTimeViewHolder(view)
                }

                MessageConstants.RECEIVED_TEXT_MESSAGE_WITH_TIME -> {
                    val view: View = LayoutInflater.from(context)
                        .inflate(R.layout.chatroom_received_message_item_with_time, parent, false)
                    return ReceivedTextChatMessageWithTimeViewHolder(view)
                }

                MessageConstants.SENT_IMAGE_MESSAGE_WITHOUT_TIME -> {
                    val view: View = LayoutInflater.from(context)
                        .inflate(R.layout.sent_photo_recyclerview_item_without_time, parent, false)
                    return SentImageChatMessageWithoutTimeViewHolder(view)
                }

                MessageConstants.SENT_IMAGE_MESSAGE_WITH_TIME -> {
                    val view: View = LayoutInflater.from(context)
                        .inflate(R.layout.sent_photo_recyclerview_item_with_time, parent, false)
                    return SentImageChatMessageWithTimeViewHolder(view)
                }
                MessageConstants.RECEIVED_IMAGE_MESSAGE_WITHOUT_TIME -> {
                    val view: View = LayoutInflater.from(context).inflate(
                        R.layout.received_photo_recyclerview_item_without_time_with_profile,
                        parent,
                        false
                    )
                    return ReceivedImageChatMessageWithoutTimeViewHolder(view)
                }

                else -> {
                    val view: View = LayoutInflater.from(context).inflate(
                        R.layout.received_photo_recyclerview_item_with_time_and_profile,
                        parent,
                        false
                    )
                    return ReceivedImageChatMessageWithTimeViewHolder(view)
                }
            }
        }
    }



}