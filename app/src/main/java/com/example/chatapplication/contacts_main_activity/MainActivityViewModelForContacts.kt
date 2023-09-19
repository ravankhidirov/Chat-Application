package com.example.chatapplication.contacts_main_activity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication.new_group.FirebaseManager
import com.example.chatapplication.registration.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivityViewModelForContacts :ViewModel(){

    private var userList = MutableLiveData<ArrayList<User>>()
    private var mDbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private var mAuth:FirebaseAuth = FirebaseAuth.getInstance()

    fun getContacts(context: Context){
        mDbRef.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var userList:ArrayList<User> = ArrayList()
                val firebaseManager = FirebaseManager()
                firebaseManager.readContactsList(mAuth.currentUser?.uid!!){contactsList->
                    for (postSnapshot in snapshot.children){
                        val currentUser = postSnapshot.getValue(User::class.java)
                        if (currentUser!=null && currentUser?.uid!=mAuth.currentUser?.uid && currentUser.uid in contactsList)
                        {
                            userList.add(currentUser)
                        }
                    }
                }
                this@MainActivityViewModelForContacts.userList.postValue(userList)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    fun observeContacts() : MutableLiveData<ArrayList<User>> {
        return userList
    }
}

