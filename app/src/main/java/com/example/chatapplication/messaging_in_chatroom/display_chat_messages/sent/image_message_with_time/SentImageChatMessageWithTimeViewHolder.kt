package com.example.chatapplication.messaging_in_chatroom.display_chat_messages.sent.image_message_with_time

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication.R
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.Interface.ChatMessageInterface
import com.example.chatapplication.messaging_with_contact.DateManager
import com.example.chatapplication.messaging_with_contact.MessageModelForContacts

class SentImageChatMessageWithTimeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),ChatMessageInterface{
    val messageTime = itemView.findViewById<TextView>(R.id.messagingTime)
    val sentImage = itemView.findViewById<ImageView>(R.id.sentImage)
    val exactTime = itemView.findViewById<TextView>(R.id.exactTime)

    val dateManager = DateManager()

    override fun displayMessage(context: Context, currentMessage: MessageModelForContacts) {
        exactTime.text = currentMessage.messageTime?.hour.toString() + ":" +currentMessage.messageTime?.minute.toString()
        messageTime.text = currentMessage.messageTime?.day.toString()+","+dateManager.getMonthByNumber(currentMessage.messageTime?.month!!)
        Glide.with(context)
            .load(currentMessage.messageOrUrl)
            .into(sentImage)
    }
}