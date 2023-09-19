package com.example.chatapplication.messaging_with_contact

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
import com.bumptech.glide.Glide
import com.example.chatapplication.databinding.ActivityChatWithContactsBinding
import com.example.chatapplication.messaging_with_contact.Constants.Companion.IMAGE_MESSAGE
import com.example.chatapplication.messaging_with_contact.Constants.Companion.TEXT_MESSAGE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.time.LocalDateTime

class ChatWithContactsActivity : AppCompatActivity() {

    private lateinit var binding:ActivityChatWithContactsBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var dbRef:DatabaseReference

    private lateinit var myName:String


    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_CAMERA_PERMISSION = 2
    private val REQUEST_IMAGE_CAPTURE = 3

    private lateinit var sentImageLink:String

    private lateinit var viewModelForMessages:ChatContactViewModel

    var receiverRoom:String? = null
    var senderRoom:String? = null




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatWithContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().reference



        val contactName = intent.getStringExtra("contactName")
        val contactSurname = intent.getStringExtra("contactSurname")
        val contactUid = intent.getStringExtra("contactUid")
        myName = intent.getStringExtra("myName").toString()
        val mySurname = intent.getStringExtra("mySurname")

        val myProfileLink = intent.getStringExtra("myProfileLink")
        val contactProfileLink = intent.getStringExtra("contactProfileLink")

        senderRoom = contactUid + auth.currentUser?.uid!!
        receiverRoom = auth.currentUser?.uid!! + contactUid

        binding.myProfileName.text = "$myName $mySurname"
        binding.contactProfileName.text = "$contactName $contactSurname"

        Glide.with(this)
            .load(myProfileLink)
            .into(binding.myProfileImage)
        Glide.with(this)
            .load(contactProfileLink)
            .into(binding.contactProfileImage)



        viewModelForMessages = ViewModelProvider(this)[ChatContactViewModel::class.java]
        viewModelForMessages.getMessages(senderRoom!!)
        val messagesLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        viewModelForMessages.observeMessages().observe(this, Observer { messageList ->
            val messageAdapter = MessageAdapter(this,messageList)
            binding.messagesRecyclerView.layoutManager = messagesLayoutManager
            binding.messagesRecyclerView.adapter = messageAdapter
            binding.messagesRecyclerView.scrollToPosition(messageList.size - 1)
        })




        binding.sendTextMessageImageView.setOnClickListener {
            val message = binding.message.text.toString()
            val currentDateTime = LocalDateTime.now()

            var hour:Int? = currentDateTime.hour
            var minute:Int?  = currentDateTime.minute
            var month:Int?  = currentDateTime.monthValue
            var day:Int?  = currentDateTime.dayOfMonth
            var year:Int?  = currentDateTime.year

            var date:Date? = Date(year,month,day,hour,minute)

            val messageObject = MessageModelForContacts(message,auth.currentUser?.uid!!,myName,TEXT_MESSAGE,
                date)
            dbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    dbRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            binding.message.setText("")
        }


        binding.sendPhotoMessageImageView.setOnClickListener {
            choosePhoto()
        }




    }




    private fun choosePhoto() {
        if (ContextCompat.checkSelfPermission(this@ChatWithContactsActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@ChatWithContactsActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PICK_IMAGE_REQUEST
            )
        } else {
            openGallery()
        }
    }



    fun capturePhoto() {
        if (ContextCompat.checkSelfPermission(this@ChatWithContactsActivity, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@ChatWithContactsActivity,
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
                        Toast.makeText(this@ChatWithContactsActivity,"Image resource null!", Toast.LENGTH_LONG).show()
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
                val messageObject = MessageModelForContacts(sentImageLink,auth.currentUser?.uid!!,myName,
                    IMAGE_MESSAGE, date)
                dbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        dbRef.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObject)
                    }
                Toast.makeText(this@ChatWithContactsActivity,"Image process successful!", Toast.LENGTH_LONG).show()
            }
    }
}
