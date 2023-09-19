package com.example.chatapplication.messaging_in_chatroom

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication.messaging_with_contact.MessageModelForContacts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatChatroomViewModel : ViewModel(){

    private var messageList = MutableLiveData<ArrayList<MessageModelForContacts>>()
    private var mDbRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun getMessages(context: Context,messageRoom:String){
        mDbRef.child("chatinchatroom").child(messageRoom).child("messages").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var messageList:ArrayList<MessageModelForContacts> = ArrayList()
                for (postSnapshot in snapshot.children){
                    val currentMessage = postSnapshot.getValue(MessageModelForContacts::class.java)
                    if (currentMessage!=null) {
                        messageList.add(currentMessage)
                    }
                }
                this@ChatChatroomViewModel.messageList.postValue(messageList)

            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    fun observeMessages() : MutableLiveData<ArrayList<MessageModelForContacts>> {
        return messageList
    }

}