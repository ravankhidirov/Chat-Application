package com.example.chatapplication.groups_main_activity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.messaging_in_chatroom.ChatInChatroomActivity
import com.example.chatapplication.R
import com.example.chatapplication.new_group.Group


class MainActivityGroupsRecyclerViewAdapter(val context: Context, val groupList:ArrayList<Group>):
    RecyclerView.Adapter<MainActivityGroupsRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val chatroomName = itemView.findViewById<TextView>(R.id.chatroomName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chatroom_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = groupList[position]

        holder.chatroomName.text = data.groupName
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatInChatroomActivity::class.java)
            intent.putExtra("groupName",data.groupName)
            intent.putExtra("creatorEmail",data.creatorEmail)
            intent.putExtra("creatorUid",data.creatorUid)
            intent.putExtra("chatroomUid",data.chatroomUid)
            context.startActivity(intent)
        }

    }
}