package com.example.chatapplication.messaging_in_chatroom

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapplication.chatroom_activity.ChatroomActivity
import com.example.chatapplication.chatroom_activity.ChatroomMembersViewModel
import com.example.chatapplication.databinding.ActivityChatInChatroomBinding
import com.example.chatapplication.messaging_with_contact.Constants
import com.example.chatapplication.messaging_with_contact.Date
import com.example.chatapplication.messaging_with_contact.MessageModelForContacts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.time.LocalDateTime

class ChatInChatroomActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChatInChatroomBinding


    private lateinit var groupName: String
    private lateinit var chatroomUid:String

    private lateinit var viewModelForMessages: ChatChatroomViewModel
    private lateinit var viewModelFoProfiles: ChatroomMembersViewModel
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth:FirebaseAuth


    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_CAMERA_PERMISSION = 2
    private val REQUEST_IMAGE_CAPTURE = 3

    private lateinit var sentImageLink:String




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatInChatroomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        groupName = intent.getStringExtra("groupName")!!
        chatroomUid = intent.getStringExtra("chatroomUid")!!

        dbRef = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        binding.groupName.text = groupName
        binding.groupName.setOnClickListener {
            val intent = Intent(this, ChatroomActivity::class.java)
            intent.putExtra("groupName",groupName)
            intent.putExtra("chatroomUid",chatroomUid)
            startActivity(intent)
        }


        viewModelForMessages = ViewModelProvider(this)[ChatChatroomViewModel::class.java]
        viewModelForMessages.getMessages(this,chatroomUid!!)
        val messagesLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        viewModelForMessages.observeMessages().observe(this, Observer { messageList ->
            val messageAdapter = ChatMessageAdapter(this,messageList)
            binding.chatroomMessagesRecyclerView.layoutManager = messagesLayoutManager
            binding.chatroomMessagesRecyclerView.adapter = messageAdapter
            binding.chatroomMessagesRecyclerView.scrollToPosition(messageList.size - 1)
        })


        viewModelFoProfiles = ViewModelProvider(this)[ChatroomMembersViewModel::class.java]
        viewModelFoProfiles.getMembers(this,chatroomUid!!)
        val profilesManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        viewModelFoProfiles.observeMembers().observe(this, Observer { userList ->
            val usersAdapter = MemberProfilesAdapter(this,userList)
            binding.chatroomMembersProfileRecyclerView.layoutManager = profilesManager
            binding.chatroomMembersProfileRecyclerView.adapter = usersAdapter
        })


        binding.sendTextMessageImageView.setOnClickListener {
            val message = binding.message.text.toString()
            val currentDateTime = LocalDateTime.now()
            var hour:Int? = currentDateTime.hour
            var minute:Int?  = currentDateTime.minute
            var month:Int?  = currentDateTime.monthValue
            var day:Int?  = currentDateTime.dayOfMonth
            var year:Int?  = currentDateTime.year
            var date: Date? = Date(year,month,day,hour,minute)
            val messageObject = MessageModelForContacts(message,auth.currentUser?.uid!!,"group",
                Constants.TEXT_MESSAGE,
                date)
            dbRef.child("chatinchatroom").child(chatroomUid!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                }
            binding.message.setText("")
        }

        binding.sendPhotoMessageImageView.setOnClickListener {
            choosePhoto()
        }

    }



    private fun choosePhoto() {
        if (ContextCompat.checkSelfPermission(this@ChatInChatroomActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@ChatInChatroomActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PICK_IMAGE_REQUEST
            )
        } else {
            openGallery()
        }
    }



    fun capturePhoto() {
        if (ContextCompat.checkSelfPermission(this@ChatInChatroomActivity, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@ChatInChatroomActivity,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            openCamera()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            }
        } else if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val selectedImage = data?.data
                    if (selectedImage != null) {
                        uploadImage(selectedImage)
                    }else{
                        Toast.makeText(this@ChatInChatroomActivity,"Image resource null!", Toast.LENGTH_LONG).show()
                    }
                }
                REQUEST_IMAGE_CAPTURE -> {
                    val photo = data?.extras?.get("data") as Bitmap

                }
            }
        }
    }




    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadImage(file: Uri)
    {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val riversRef = storageRef.child("chatImages").child("${System.currentTimeMillis()}")
        var uploadTask = riversRef.putFile(file)
        uploadTask
            .addOnFailureListener {}
            .addOnSuccessListener {
                val urlTask = it.storage.downloadUrl
                while(!urlTask.isSuccessful){
                    continue
                }
                val currentDateTime = LocalDateTime.now()

                var hour:Int? = currentDateTime.hour
                var minute:Int?  = currentDateTime.minute
                var month:Int?  = currentDateTime.monthValue
                var day:Int?  = currentDateTime.dayOfMonth
                var year:Int?  = currentDateTime.year

                var date:Date? = Date(year,month,day,hour,minute)
                sentImageLink = urlTask.result.toString()
                val messageObject = MessageModelForContacts(sentImageLink,auth.currentUser?.uid!!,"group",
                    Constants.IMAGE_MESSAGE, date)
                dbRef.child("chatinchatroom").child(chatroomUid!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {

                    }
                Toast.makeText(this@ChatInChatroomActivity,"Image process successful!", Toast.LENGTH_LONG).show()
            }
    }



}