package com.example.chatapplication.messaging_in_chatroom.display_chat_messages.view_type

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.constants.MessageConstants.Companion.RECEIVED_IMAGE_MESSAGE_WITHOUT_TIME
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.constants.MessageConstants.Companion.RECEIVED_IMAGE_MESSAGE_WITH_TIME
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.constants.MessageConstants.Companion.RECEIVED_TEXT_MESSAGE_WITHOUT_TIME
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.constants.MessageConstants.Companion.RECEIVED_TEXT_MESSAGE_WITH_TIME
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.constants.MessageConstants.Companion.SENT_IMAGE_MESSAGE_WITHOUT_TIME
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.constants.MessageConstants.Companion.SENT_IMAGE_MESSAGE_WITH_TIME
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.constants.MessageConstants.Companion.SENT_TEXT_MESSAGE_WITHOUT_TIME
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.constants.MessageConstants.Companion.SENT_TEXT_MESSAGE_WITH_TIME
import com.example.chatapplication.messaging_with_contact.Constants
import com.example.chatapplication.messaging_with_contact.MessageModelForContacts
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDateTime

class ItemViewType {

    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        fun getItemViewType(messageList:ArrayList<MessageModelForContacts>,position:Int):Int{
            val currentMessage = messageList[position]
            val currentDateTime = LocalDateTime.now()
            var hour:Int? = currentDateTime.hour
            var minute:Int?  = currentDateTime.minute
            var month:Int?  = currentDateTime.monthValue
            var day:Int?  = currentDateTime.dayOfMonth
            var year:Int?  = currentDateTime.year

            var isThereTime:Boolean = false
            if (position == 0){
                isThereTime = true
            }else{
                var lastMessage = messageList[position-1]
                if (lastMessage.messageTime?.year != currentMessage.messageTime?.year || lastMessage.messageTime?.month != currentMessage.messageTime?.month || lastMessage.messageTime?.day != currentMessage.messageTime?.day){
                    isThereTime = true
                }
            }
            if (currentMessage.messageType == Constants.IMAGE_MESSAGE){
                return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderUid)){
                    if (isThereTime){
                        SENT_IMAGE_MESSAGE_WITH_TIME
                    }else{
                        SENT_IMAGE_MESSAGE_WITHOUT_TIME
                    }
                }else{
                    if (isThereTime){
                        RECEIVED_IMAGE_MESSAGE_WITH_TIME
                    }else{
                        RECEIVED_IMAGE_MESSAGE_WITHOUT_TIME
                    }
                }
            }else{
                return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderUid)){
                    if (isThereTime){
                        SENT_TEXT_MESSAGE_WITH_TIME
                    }else{
                        SENT_TEXT_MESSAGE_WITHOUT_TIME
                    }
                }else{
                    if (isThereTime){
                        RECEIVED_TEXT_MESSAGE_WITH_TIME
                    }else{
                        RECEIVED_TEXT_MESSAGE_WITHOUT_TIME
                    }
                }
            }
        }
    }
}