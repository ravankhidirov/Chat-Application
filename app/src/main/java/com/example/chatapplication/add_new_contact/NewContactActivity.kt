package com.example.chatapplication.add_new_contact

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.chatapplication.R
import com.example.chatapplication.databinding.ActivityNewContactBinding
import com.example.chatapplication.registration.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NewContactActivity : AppCompatActivity() {
    private lateinit var binding:ActivityNewContactBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var bottomSheetFragment: ContactFragment
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().reference


        binding.searchUserButton.setOnClickListener {




            binding.progressBar.visibility = View.VISIBLE
            binding.progressbarConstraintLayout.setBackgroundColor(R.color.progressbar_background)
            binding.searchUserButton.isClickable = false






            dbRef.child("user").addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {


                    binding.progressBar.visibility = View.GONE
                    binding.progressbarConstraintLayout.setBackgroundColor(0)
                    binding.searchUserButton.isClickable = true




                    var searchedAccount:User? = null
                    for (postSnapshot in snapshot.children){
                        var currentUser = postSnapshot.getValue(User::class.java)
                        if (currentUser?.email == binding.emailForSearch.text.toString()){
                            searchedAccount = currentUser
                        }
                    }
                    if (searchedAccount!=null)
                    {
                        bottomSheetFragment = ContactFragment(this@NewContactActivity,searchedAccount)
                        bottomSheetFragment.show(supportFragmentManager,"BottomSheetDialog")

                    }
                }
                override fun onCancelled(error: DatabaseError) {

                    binding.progressBar.visibility = View.GONE
                    binding.progressbarConstraintLayout.setBackgroundColor(0)
                    binding.searchUserButton.isClickable = true
                    Toast.makeText(this@NewContactActivity,"User does not exist!",Toast.LENGTH_LONG).show()
                }
            })

        }
    }
}