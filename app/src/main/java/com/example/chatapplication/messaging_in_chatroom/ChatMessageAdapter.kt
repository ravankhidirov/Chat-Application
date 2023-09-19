package com.example.chatapplication.messaging_in_chatroom

import android.content.Context
import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.Interface.ChatMessageInterface
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.oncreate_viewholder.OnCreateViewHolder
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.view_type.ItemViewType
import com.example.chatapplication.messaging_with_contact.MessageModelForContacts

class ChatMessageAdapter(val context: Context, val messageList:ArrayList<MessageModelForContacts>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItemViewType(position: Int): Int {
        return ItemViewType.getItemViewType(messageList,position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return OnCreateViewHolder.getOnCreateViewHolder(context,parent,viewType)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if (holder is ChatMessageInterface){
            holder.displayMessage(context,currentMessage)
        }
    }
}