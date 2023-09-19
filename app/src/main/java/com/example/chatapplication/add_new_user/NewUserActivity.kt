package com.example.chatapplication.add_new_user

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.chatapplication.R
import com.example.chatapplication.databinding.ActivityNewUserBinding
import com.example.chatapplication.registration.user.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NewUserActivity : AppCompatActivity() {
    private lateinit var binding:ActivityNewUserBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var bottomSheetFragment: UserFragment
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().reference

        val groupUid = intent.getStringExtra("chatroomUid")

        binding.searchUserButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            binding.progressbarConstraintLayout.setBackgroundColor(R.color.progressbar_background)
            binding.searchUserButton.isClickable = false

            dbRef.child("user").addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    binding.progressBar.visibility = View.VISIBLE
                    binding.progressbarConstraintLayout.setBackgroundColor(R.color.progressbar_background)
                    binding.searchUserButton.isClickable = false
                    var searchedAccount:User? = null
                    for (postSnapshot in snapshot.children){
                        var currentUser = postSnapshot.getValue(User::class.java)
                        if (currentUser?.email == binding.emailForSearch.text.toString()){
                            searchedAccount = currentUser
                        }
                    }
                    if (searchedAccount!=null)
                    {
                        bottomSheetFragment = UserFragment(this@NewUserActivity,searchedAccount, groupUid)
                        bottomSheetFragment.show(supportFragmentManager,"BottomSheetDialog")

                    }
                }
                override fun onCancelled(error: DatabaseError) {

                    binding.progressBar.visibility = View.GONE
                    binding.progressbarConstraintLayout.setBackgroundColor(0)
                    binding.searchUserButton.isClickable = true
                    Toast.makeText(this@NewUserActivity,"User does not exist!",Toast.LENGTH_LONG).show()

                }
            })

        }
    }
}