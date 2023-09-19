package com.example.chatapplication.messaging_in_chatroom.display_chat_messages.received.image_message_without_time

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication.R
import com.example.chatapplication.messaging_in_chatroom.display_chat_messages.Interface.ChatMessageInterface
import com.example.chatapplication.messaging_with_contact.MessageModelForContacts
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReceivedImageChatMessageWithoutTimeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),ChatMessageInterface{
    val receivedImage = itemView.findViewById<ImageView>(R.id.receivedImage)
    val exactTime = itemView.findViewById<TextView>(R.id.exactTime)
    val profileImage = itemView.findViewById<ImageView>(R.id.profileImage)



    override fun displayMessage(context: Context, currentMessage: MessageModelForContacts) {
        exactTime.text = currentMessage.messageTime?.hour.toString() + ":" +currentMessage.messageTime?.minute.toString()
        Glide.with(context)
            .load(currentMessage.messageOrUrl)
            .into(receivedImage)
        val dbRef = FirebaseDatabase.getInstance().reference
        dbRef.child("images").child(currentMessage.senderUid!!).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profileLink = snapshot.getValue(String::class.java)
                Glide.with(context)
                    .load(profileLink)
                    .into(profileImage)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}