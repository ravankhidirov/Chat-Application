package com.example.chatapplication.profile_edit.fragments

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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.chatapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CurrentProfileFragment : Fragment() {


    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_CAMERA_PERMISSION = 2
    private val REQUEST_IMAGE_CAPTURE = 3
    var profileLink:String? = null

    private lateinit var profileImage:ImageView


    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    private lateinit var progressBarLayout: ConstraintLayout
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_current_profile, container, false)
    }


    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameEdit = view.findViewById<ImageView>(R.id.nameEdit)
        val surnameEdit = view.findViewById<ImageView>(R.id.surnameEdit)


        progressBar = view.findViewById(R.id.progressBar)
        progressBarLayout = view.findViewById(R.id.progressbarConstraintLayout)


        val name = view.findViewById<TextView>(R.id.name)
        val surname = view.findViewById<TextView>(R.id.surname)
        val dbRef = FirebaseDatabase.getInstance().reference

        val auth = FirebaseAuth.getInstance()

        val changeImage = view.findViewById<ImageView>(R.id.profileEdit)
        profileImage = view.findViewById(R.id.profileImage)


        changeImage.setOnClickListener {
            choosePhoto()
        }

        dbRef.child("images").child(auth.currentUser?.uid!!).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val profileLink = snapshot.getValue(String::class.java)
                Glide.with(this@CurrentProfileFragment.requireActivity())
                    .load(profileLink)
                    .into(profileImage)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val progressBarLayout: ConstraintLayout = view.findViewById(R.id.progressbarConstraintLayout)


        progressBar.visibility = View.VISIBLE
        progressBarLayout.setBackgroundColor(R.color.progressbar_background)


        dbRef.child("user").child(auth.currentUser?.uid!!).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressBar.visibility = View.GONE
                progressBarLayout.setBackgroundColor(0)
                val nameDb = snapshot.child("name").getValue(String::class.java)
                val surnameDb = snapshot.child("surname").getValue(String::class.java)

                name.text = nameDb
                surname.text = surnameDb
                val NAME = 0
                val SURNAME = 1


                if (nameDb != null ){
                    nameEdit.setOnClickListener {
                        val action = CurrentProfileFragmentDirections.actionFromCurrentToEdit(nameDb,NAME)
                        findNavController().navigate(action)
                    }
                }


                if (surnameDb!=null){
                    surnameEdit.setOnClickListener {
                        val action = CurrentProfileFragmentDirections.actionFromCurrentToEdit(surnameDb,SURNAME)
                        findNavController().navigate(action)
                    }
                }


            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = View.GONE
                progressBarLayout.setBackgroundColor(0)
                Toast.makeText(this@CurrentProfileFragment.requireContext(),"Couldn't get name and surname!",Toast.LENGTH_LONG).show()
            }

        })

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