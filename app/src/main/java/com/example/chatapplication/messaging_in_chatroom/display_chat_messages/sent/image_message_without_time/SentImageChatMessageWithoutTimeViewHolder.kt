package com.example.chatapplication.messaging_in_chatroom.display_chat_messages.sent.image_message_without_time

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication.R
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.Interface.ChatMessageInterface
import com.example.chatapplication.messaging_with_contact.MessageModelForContacts

class SentImageChatMessageWithoutTimeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),ChatMessageInterface{
    val sentImage = itemView.findViewById<ImageView>(R.id.sentImage)
    val exactTime = itemView.findViewById<TextView>(R.id.exactTime)



    override fun displayMessage(context: Context, currentMessage: MessageModelForContacts) {
        exactTime.text = currentMessage.messageTime?.hour.toString() + ":" +currentMessage.messageTime?.minute.toString()
        Glide.with(context)
            .load(currentMessage.messageOrUrl)
            .into(sentImage)
    }
}