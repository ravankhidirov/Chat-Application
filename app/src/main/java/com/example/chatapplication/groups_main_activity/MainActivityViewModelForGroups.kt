package com.example.chatapplication.groups_main_activity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication.new_group.FirebaseManager
import com.example.chatapplication.new_group.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivityViewModelForGroups : ViewModel(){

    private var groupsList = MutableLiveData<ArrayList<Group>>()
    private var mDbRef: DatabaseReference = FirebaseDatabase.getInstance().reference


    fun getMyGroups(context: Context){
        val firebaseManager = FirebaseManager()
        val auth = FirebaseAuth.getInstance()
        var groupsList:ArrayList<Group> = ArrayList()
        firebaseManager.readGroupList(auth.currentUser?.uid!!){groups->
            var myGroups = ArrayList<String>()
            for (group in groups) {
                if (group.uid != null) {
                    myGroups.add(group.uid!!)
                }
            }

            for (group in groups){
                mDbRef.child("group").child(group.uid!!).addValueEventListener(object:ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var currentGroup = snapshot.getValue(Group::class.java)
                        if (currentGroup!=null){
                            groupsList.add(currentGroup)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        //
                    }

                })
            }
            this@MainActivityViewModelForGroups.groupsList.postValue(groupsList)
        }
    }





    fun getGroups(context: Context){
        mDbRef.child("group").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var groupsList:ArrayList<Group> = ArrayList()
                val firebaseManager = FirebaseManager()
                val auth = FirebaseAuth.getInstance()
                firebaseManager.readGroupList(auth.currentUser?.uid!!){groups->
                    var myGroups = ArrayList<String>()
                    for (group in groups){
                        if (group.uid != null){
                            myGroups.add(group.uid!!)
                        }
                    }
                    for (postSnapshot in snapshot.children){
                        val currentGroup = postSnapshot.getValue(Group::class.java)
                        if (currentGroup!=null && currentGroup.chatroomUid in myGroups){
                            groupsList.add(currentGroup)
                        }
                    }
                }

                this@MainActivityViewModelForGroups.groupsList.postValue(groupsList)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    fun observeGroups() : MutableLiveData<ArrayList<Group>> {
        return groupsList
    }
}