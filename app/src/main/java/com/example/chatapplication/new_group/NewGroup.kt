package com.example.chatapplication.new_group

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.chatapplication.MainActivity
import com.example.chatapplication.R
import com.example.chatapplication.databinding.ActivityNewGroupBinding
import com.example.chatapplication.messaging_with_contact.Constants.Companion.FULL_ACCESS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NewGroup : AppCompatActivity() {
    private lateinit var binding:ActivityNewGroupBinding
    private lateinit var mDbRef: DatabaseReference

    private var peopleList = mutableListOf<GroupUser>()
    private lateinit var auth: FirebaseAuth




    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference
        binding.newGroupButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.progressbarConstraintLayout.setBackgroundColor(R.color.progressbar_background)
            binding.newGroupButton.isClickable = true
            createChatroom(binding.groupName.text.toString(),auth.currentUser?.email!!,auth.currentUser?.uid!!)
        }
    }


    private fun createChatroom(groupName:String,creatorEmail:String,creatorUid:String){
        peopleList.add(GroupUser(creatorUid,FULL_ACCESS))
        val timeForId = System.currentTimeMillis()
        mDbRef.child("group").child("$creatorUid" + "chatroom" + "$timeForId")
            .setValue(Group(groupName,creatorEmail,creatorUid,peopleList,"$creatorUid" + "chatroom" + "$timeForId")).addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                binding.progressbarConstraintLayout.setBackgroundColor(0)
                binding.newGroupButton.isClickable = true
                val firebaseDataManager = FirebaseManager()
                firebaseDataManager.addGroupAndUpdateList(creatorUid,"$creatorUid" + "chatroom" + "$timeForId")
                val intent = Intent(this@NewGroup, MainActivity::class.java)
                Toast.makeText(this@NewGroup,"Successfully created!",Toast.LENGTH_LONG).show()
                finish()
                startActivity(intent)
            }
    }

}