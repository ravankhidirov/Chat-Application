package com.example.chatapplication.messaging_with_contact.display_contact_messages.Interface

import android.content.Context
import com.example.chatapplication.messaging_with_contact.MessageModelForContacts

interface ContactMessageInterface {
    fun displayMessage(context: Context, currentMessage: MessageModelForContacts)
}