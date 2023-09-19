package com.example.chatapplication.chatroom_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapplication.add_new_user.NewUserActivity
import com.example.chatapplication.databinding.ActivityChatroomBinding
import com.example.chatapplication.new_group.FirebaseManager

class ChatroomActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChatroomBinding
    private lateinit var viewModelForMembers:ChatroomMembersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatroomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var groupName = intent.getStringExtra("groupName")
        var chatroomUid = intent.getStringExtra("chatroomUid")

        val firebaseManager = FirebaseManager()

        binding.addNewUser.setOnClickListener {
            val intent = Intent(this,NewUserActivity::class.java)
            intent.putExtra("groupName",groupName)
            intent.putExtra("chatroomUid",chatroomUid)
            startActivity(intent)
        }



        viewModelForMembers = ViewModelProvider(this)[ChatroomMembersViewModel::class.java]
        viewModelForMembers.getMembers(this,chatroomUid!!)
        val contactsLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        viewModelForMembers.observeMembers().observe(this, Observer { contactsList ->
            val contactsAdapter = MembersRecyclerViewAdapter(this,contactsList)
            binding.chatroomMembersRecyclerView.layoutManager = contactsLayoutManager
            binding.chatroomMembersRecyclerView.adapter = contactsAdapter
        })


    }
}