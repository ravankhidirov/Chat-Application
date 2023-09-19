package com.example.chatapplication.contacts_main_activity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication.messaging_with_contact.ChatWithContactsActivity
import com.example.chatapplication.R
import com.example.chatapplication.messaging_with_contact.MessageModelForContacts
import com.example.chatapplication.registration.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivityContactsRecyclerViewAdapter(val context: Context, val userList:ArrayList<User>):
    RecyclerView.Adapter<MainActivityContactsRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val contactProfileImage = itemView.findViewById<ImageView>(R.id.contactProfileImage)
        val contactName = itemView.findViewById<TextView>(R.id.contactName)
        val contactLastMessage = itemView.findViewById<TextView>(R.id.contactLastMessage)
        val contactLastMessageDate = itemView.findViewById<TextView>(R.id.lastMesssageDate)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = userList[position]

        val auth = FirebaseAuth.getInstance()


        val dbRef = FirebaseDatabase.getInstance().reference
        dbRef.child("images").child(data.uid!!).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profileLink = snapshot.getValue(String::class.java)
                dbRef.child("chats").child(data.uid!! + auth.currentUser?.uid!!).child("messages").addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var messageList:ArrayList<MessageModelForContacts> = ArrayList()
                        for (postSnapshot in snapshot.children){
                            val currentMessage = postSnapshot.getValue(MessageModelForContacts::class.java)
                            if (currentMessage!=null) {
                                messageList.add(currentMessage)
                            }
                        }
                        Glide.with(context)
                            .load(profileLink)
                            .into(holder.contactProfileImage)
                        holder.contactName.text = data.name + " " + data.surname
                        if (messageList.size != 0){
                            holder.contactLastMessage.text = messageList[messageList.size - 1].messageOrUrl
                            holder.contactLastMessageDate.text = messageList[messageList.size - 1].messageTime?.hour.toString() + ":" + messageList[messageList.size - 1].messageTime?.minute.toString()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        holder.itemView.setOnClickListener {

            val dbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
            val auth:FirebaseAuth = FirebaseAuth.getInstance()
            dbRef.child("user").child(auth.currentUser?.uid!!).addValueEventListener(object:
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val name = snapshot.child("name").getValue(String::class.java)
                    val surname = snapshot.child("surname").getValue(String::class.java)

                    dbRef.child("images").child(auth.currentUser?.uid!!).addValueEventListener(object:
                        ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val myProfileLink = snapshot.getValue(String::class.java)


                            dbRef.child("images").child(data.uid!!).addValueEventListener(object :ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val contactProfileLink = snapshot.getValue(String::class.java)
                                    val intent = Intent(context, ChatWithContactsActivity::class.java)
                                    intent.putExtra("contactName",data.name)
                                    intent.putExtra("contactSurname",data.surname)
                                    intent.putExtra("contactUid",data.uid)
                                    intent.putExtra("myProfileLink",myProfileLink)
                                    intent.putExtra("contactProfileLink",contactProfileLink)
                                    intent.putExtra("myName",name)
                                    intent.putExtra("mySurname",surname)
                                    context.startActivity(intent)
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })


                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    }
}