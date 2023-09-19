package com.example.chatapplication.messaging_in_chatroom.display_chat_messages.Interface

import android.content.Context
import com.example.chatapplication.messaging_with_contact.MessageModelForContacts

interface ChatMessageInterface {
    fun displayMessage(context: Context, currentMessage:MessageModelForContacts)
}