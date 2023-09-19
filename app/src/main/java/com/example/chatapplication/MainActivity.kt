package com.example.chatapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication.add_new_contact.NewContactActivity
import com.example.chatapplication.databinding.ActivityMainBinding
import com.example.chatapplication.contacts_main_activity.MainActivityContactsRecyclerViewAdapter
import com.example.chatapplication.contacts_main_activity.MainActivityViewModelForContacts
import com.example.chatapplication.groups_main_activity.MainActivityGroupsRecyclerViewAdapter
import com.example.chatapplication.groups_main_activity.MainActivityViewModelForGroups
import com.example.chatapplication.messaging_with_contact.ChatContactViewModel
import com.example.chatapplication.new_group.FirebaseManager
import com.example.chatapplication.new_group.NewGroup
import com.example.chatapplication.profile_edit.ProfileEditActivity
import com.example.chatapplication.registration.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var storage:FirebaseStorage
    private lateinit var auth: FirebaseAuth

    private lateinit var imageView: ImageView
    private lateinit var storageRef: StorageReference
    private lateinit var viewModelForContacts : MainActivityViewModelForContacts
    private lateinit var viewModelForGroups: MainActivityViewModelForGroups

    private lateinit var viewModelChat:ChatContactViewModel

    private lateinit var contactsRecyclerView: RecyclerView
    private lateinit var userList:ArrayList<User>
    private lateinit var dbRef: DatabaseReference

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        dbRef = FirebaseDatabase.getInstance().getReference()
        imageView = findViewById(R.id.profileImage)
        storageRef = Firebase.storage.reference

        val profileLink = intent.getStringExtra("profileLink")



        binding.currentUserName.setOnClickListener {
            val intent = Intent(this@MainActivity,ProfileEditActivity::class.java)
            startActivity(intent)
        }

        binding.currentUserName.text = "Hi, there!"





        dbRef.child("user").child(auth.currentUser?.uid!!).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").getValue(String::class.java)
                val surname = snapshot.child("surname").getValue(String::class.java)
                binding.currentUserName.text = "$name $surname"
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        dbRef.child("images").child(auth.currentUser?.uid!!).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val profileLink = snapshot.getValue(String::class.java)
                Glide.with(this@MainActivity)
                    .load(profileLink)
                    .into(binding.profileImage)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.addNewChatroom.setOnClickListener {
            val intent = Intent(this,NewGroup::class.java)
            startActivity(intent)
        }


        binding.addNewContact.setOnClickListener {
            val intent = Intent(this,NewContactActivity::class.java)
            startActivity(intent)
        }



        viewModelForGroups = ViewModelProvider(this)[MainActivityViewModelForGroups::class.java]
        viewModelForGroups.getMyGroups(this)
        val groupsLayoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        viewModelForGroups.observeGroups().observe(this, Observer {groupsList->
            val groupsAdapter = MainActivityGroupsRecyclerViewAdapter(this,groupsList)
            binding.chatroomsRecyclerView.layoutManager = groupsLayoutManager
            binding.chatroomsRecyclerView.adapter = groupsAdapter
        })




        viewModelForContacts = ViewModelProvider(this)[MainActivityViewModelForContacts::class.java]
        viewModelForContacts.getContacts(this)
        val contactsLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        viewModelForContacts.observeContacts().observe(this, Observer { contactsList ->
            val contactsAdapter = MainActivityContactsRecyclerViewAdapter(this,contactsList)
            binding.contactsRecyclerView.layoutManager = contactsLayoutManager
            binding.contactsRecyclerView.adapter = contactsAdapter
        })


        binding.refreshLayout.setOnRefreshListener {
            viewModelForContacts.getContacts(this)
            viewModelForGroups.getMyGroups(this)
            binding.refreshLayout.isRefreshing = false
        }
    }




    private fun getContacts():ArrayList<User>{
        var userList:ArrayList<User> = ArrayList()
        dbRef.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var userList:ArrayList<User> = ArrayList()
                val firebaseManager = FirebaseManager()
                firebaseManager.readContactsList(auth.currentUser?.uid!!){contactsList->
                    for (postSnapshot in snapshot.children){
                        val currentUser = postSnapshot.getValue(User::class.java)
                        if (currentUser!=null && currentUser?.uid!=auth.currentUser?.uid && currentUser.uid in contactsList)
                        {
                            userList.add(currentUser)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        return userList
    }




}