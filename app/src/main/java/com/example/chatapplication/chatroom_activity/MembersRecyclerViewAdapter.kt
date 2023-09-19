package com.example.chatapplication.chatroom_activity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication.R
import com.example.chatapplication.messaging_with_contact.Constants.Companion.FULL_ACCESS
import com.example.chatapplication.new_group.GroupUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MembersRecyclerViewAdapter(val context: Context, val userList:ArrayList<GroupUser>):
    RecyclerView.Adapter<MembersRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val profileImage = itemView.findViewById<ImageView>(R.id.profileImage)
        val contactName = itemView.findViewById<TextView>(R.id.contactName)
        val fullAccess = itemView.findViewById<ImageButton>(R.id.fullAccess)
        val regularAccess = itemView.findViewById<ImageButton>(R.id.regularAccess)
        val removeAccount = itemView.findViewById<ImageView>(R.id.removeAccount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chatroom_contact_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = userList[position]



        if (data.access == FULL_ACCESS)
        {
            holder.fullAccess.setBackgroundResource(R.drawable.full_access_clicked)
            holder.regularAccess.setBackgroundResource(R.drawable.regular_access_not_clicked)
        }else{
            holder.fullAccess.setBackgroundResource(R.drawable.full_access_not_clicked)
            holder.regularAccess.setBackgroundResource(R.drawable.regular_access_clicked)
        }



        val dbRef = FirebaseDatabase.getInstance().reference
        dbRef.child("images").child(data.uid!!).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profileLink = snapshot.getValue(String::class.java)


                Glide.with(context)
                    .load(profileLink)
                    .into(holder.profileImage)



                dbRef.child("user").child(data.uid!!).addValueEventListener(object:
                    ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val name = snapshot.child("name").getValue(String::class.java)
                        val surname = snapshot.child("surname").getValue(String::class.java)
                        holder.contactName.text = name + " " + surname
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
