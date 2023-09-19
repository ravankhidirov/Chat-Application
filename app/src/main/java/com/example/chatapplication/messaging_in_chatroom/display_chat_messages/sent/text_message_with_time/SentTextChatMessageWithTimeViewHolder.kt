package com.example.chatapplication.messaging_in_chatroom.display_chat_messages.sent.text_message_with_time

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.R
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.Interface.ChatMessageInterface
import com.example.chatapplication.messaging_with_contact.DateManager
import com.example.chatapplication.messaging_with_contact.MessageModelForContacts

class SentTextChatMessageWithTimeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),ChatMessageInterface{
    val messageTime = itemView.findViewById<TextView>(R.id.messagingTime)
    val leftChatView = itemView.findViewById<LinearLayout>(R.id.left_chat_view)
    val rightChatView = itemView.findViewById<LinearLayout>(R.id.right_chat_view)
    val rightTextView = itemView.findViewById<TextView>(R.id.right_chat_text_view)
    val exactTimeRight = itemView.findViewById<TextView>(R.id.exactTimeRight)

    val dateManager = DateManager()

    override fun displayMessage(context: Context, currentMessage: MessageModelForContacts) {
        messageTime.text = currentMessage.messageTime?.day.toString()+","+dateManager.getMonthByNumber(currentMessage.messageTime?.month!!)
        leftChatView.visibility = View.GONE
        rightChatView.visibility = View.VISIBLE
        rightTextView.text = currentMessage.messageOrUrl
        exactTimeRight.text = "${currentMessage.messageTime?.hour}:${currentMessage.messageTime?.minute}"
    }
}