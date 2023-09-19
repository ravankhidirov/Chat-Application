package com.example.chatapplication.registration_new

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.chatapplication.MainActivity
import com.example.chatapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProfileFragment : Fragment() {

    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_CAMERA_PERMISSION = 2
    private val REQUEST_IMAGE_CAPTURE = 3
    var profileLink:String? = null

    private lateinit var profileImage:ImageView


    private lateinit var progressBarLayout: ConstraintLayout
    private lateinit var progressBar: ProgressBar

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val dbRef:DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = view.findViewById<AppCompatButton>(R.id.finishButton)
        val changeImage = view.findViewById<ImageView>(R.id.changeImage)
        val dbRef:DatabaseReference = FirebaseDatabase.getInstance().reference
        val auth = FirebaseAuth.getInstance()



        progressBar = view.findViewById(R.id.progressBar)
        progressBarLayout = view.findViewById(R.id.progressbarConstraintLayout)
        profileImage = view.findViewById(R.id.profileImage)

        changeImage.setOnClickListener {
            choosePhoto()
        }



        button.setOnClickListener {
            progressBarLayout.setBackgroundColor(R.color.progressbar_background)
            progressBar.visibility = View.VISIBLE
            if (profileLink == null){
                profileLink = "https://firebasestorage.googleapis.com/v0/b/chatapplication-822f2.appspot.com/o/elizabet.jpg?alt=media&token=e461a76f-ce3c-46b5-a806-9995bb7c1a52"
            }
            dbRef.child("images").child(auth.currentUser?.uid!!).setValue(profileLink)
            val intent = Intent(this.requireContext(),MainActivity::class.java)
            intent.putExtra("profileLink",profileLink)
            startActivity(intent)
        }
    }








    private fun choosePhoto() {
        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PICK_IMAGE_REQUEST
            )
        } else {
            openGallery()
        }
    }



    fun capturePhoto() {
        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val selectedImage = data?.data
                    if (selectedImage != null) {
                        uploadImage(selectedImage)
                    }else{
                        Toast.makeText(this.requireContext(),"Image resource null!", Toast.LENGTH_LONG).show()
                    }
                    profileImage.setImageURI(selectedImage)
                }
                REQUEST_IMAGE_CAPTURE -> {
                    val photo = data?.extras?.get("data") as Bitmap
                    profileImage.setImageBitmap(photo)
                }
            }
        }
    }




    @SuppressLint("ResourceAsColor")
    fun uploadImage(file: Uri)
    {
        progressBarLayout.setBackgroundColor(R.color.progressbar_background)
        progressBar.visibility = View.VISIBLE
        val storage = Firebase.storage
        val storageRef = storage.reference
        val riversRef = storageRef.child("images").child(auth.currentUser?.uid!!)
        var uploadTask = riversRef.putFile(file)
        uploadTask
            .addOnFailureListener {

                progressBarLayout.setBackgroundColor(0)
                progressBar.visibility = View.GONE
                Toast.makeText(this.requireContext(),"Couldn't be uploaded! Try again!", Toast.LENGTH_LONG).show()

            }
            .addOnSuccessListener {
                progressBarLayout.setBackgroundColor(0)
                progressBar.visibility = View.GONE
                val urlTask = it.storage.downloadUrl
                while(!urlTask.isSuccessful){
                    continue
                }
                profileLink = urlTask.result.toString()
                dbRef.child("images").child(auth.currentUser?.uid!!).setValue(profileLink)
                Toast.makeText(this.requireContext(),"Image process successful!", Toast.LENGTH_LONG).show()
            }
    }

}