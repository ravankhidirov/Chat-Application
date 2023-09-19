package com.example.chatapplication.messaging_in_chatroom

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication.R
import com.example.chatapplication.messaging_with_contact.Constants
import com.example.chatapplication.messaging_with_contact.MessageModelForContacts
import com.example.chatapplication.new_group.GroupUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime

class MemberProfilesAdapter(val context: Context,val userList:ArrayList<GroupUser>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class DownViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val DownProfileImage = itemView.findViewById<ImageView>(R.id.profileImage)
    }

    class UpperViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val UpProfileImage = itemView.findViewById<ImageView>(R.id.profileImage)
    }

    val UP = 1
    val DOWN = 0
    override fun getItemViewType(position: Int): Int {
        return if (position%2==0){
            UP
        }else{
            DOWN
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        return if (viewType == UP){
            val view: View = LayoutInflater.from(context).inflate(R.layout.chatroom_member_profile_photo_upper,parent,false)
            UpperViewHolder(view)
        }else {
            val view: View = LayoutInflater.from(context)
                .inflate(R.layout.chatroom_member_profile_photo_down, parent, false)
            DownViewHolder(view)
        }
    }


    override fun getItemCount(): Int {
        return userList.size
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentUser = userList[position]

        val dbRef = FirebaseDatabase.getInstance().reference

        when(holder){
            is DownViewHolder->{

                dbRef.child("images").child(currentUser.uid!!).addValueEventListener(object:
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val profileLink = snapshot.getValue(String::class.java)
                        Glide.with(context)
                            .load(profileLink)
                            .into(holder.DownProfileImage)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

            }
            is UpperViewHolder->{

                dbRef.child("images").child(currentUser.uid!!).addValueEventListener(object:
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val profileLink = snapshot.getValue(String::class.java)
                        Glide.with(context)
                            .load(profileLink)
                            .into(holder.UpProfileImage)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

            }
        }


    }
    }
