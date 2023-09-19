package com.example.chatapplication.new_group

import com.example.chatapplication.messaging_with_contact.Constants.Companion.REGULAR_ACCESS
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseManager {


    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    // Function to read the "people" list from Firebase
    fun readPeopleList(chatroomUid: String, onCompletion: (MutableList<GroupUser>) -> Unit) {
        val chatroomRef = databaseReference.child("group").child(chatroomUid)
        chatroomRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val peopleList = mutableListOf<GroupUser>()
                val peopleSnapshot = dataSnapshot.child("people")
                for (personSnapshot in peopleSnapshot.children) {
                    val personUid = personSnapshot.getValue(GroupUser::class.java)
                    personUid?.let {
                        peopleList.add(it)
                    }
                }
                onCompletion(peopleList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
            }
        })
    }

    // Function to add a new person to the "people" list and update it in Firebase
    fun addPersonAndUpdateList(chatroomUid: String, newPersonUid: String) {
        readPeopleList(chatroomUid) { currentPeopleList ->

            val uids = mutableListOf<String>()
            for (i in currentPeopleList){
                uids.add(i.uid!!)
            }

            if (newPersonUid !in uids){
                currentPeopleList.add(GroupUser(newPersonUid,REGULAR_ACCESS))
                val chatroomRef = databaseReference.child("group").child(chatroomUid)
                chatroomRef.child("people").setValue(currentPeopleList)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Successfully updated the "people" list
                        } else {
                            // Handle the error here
                        }
                    }
            }

        }
    }


    fun readGroupList(personUid: String, onCompletion: (MutableList<GroupUser>) -> Unit){
        val personRef = databaseReference.child("user").child(personUid)
        personRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val groupList = mutableListOf<GroupUser>()
                val groupsSnapshot = dataSnapshot.child("groups")
                for (groupSnapshot in groupsSnapshot.children) {
                    val groupUid = groupSnapshot.getValue(GroupUser::class.java)
                    groupUid?.let {
                        groupList.add(it)
                    }
                }
                onCompletion(groupList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
            }
        })
    }


    fun addGroupAndUpdateList(personUid: String, newGroupUid: String) {
        readGroupList(personUid) { currentGroupsList ->

            val uids = mutableListOf<String>()
            for (i in currentGroupsList){
                uids.add(i.uid!!)
            }

            if (newGroupUid !in uids){
                currentGroupsList.add(GroupUser(newGroupUid, REGULAR_ACCESS))
                val personRef = databaseReference.child("user").child(personUid)
                personRef.child("groups").setValue(currentGroupsList)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Successfully updated the "people" list
                        } else {
                            // Handle the error here
                        }
                    }
            }

        }
    }


    fun readContactsList(userUid: String, onCompletion: (MutableList<String>) -> Unit) {
        val chatroomRef = databaseReference.child("user").child(userUid)
        chatroomRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val peopleList = mutableListOf<String>()
                val peopleSnapshot = dataSnapshot.child("contacts")
                for (personSnapshot in peopleSnapshot.children) {
                    val personUid = personSnapshot.getValue(String::class.java)
                    personUid?.let {
                        peopleList.add(it)
                    }
                }
                onCompletion(peopleList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
            }
        })
    }


    fun addContactAndUpdateList(userUid: String, userToAdd: String) {
        readContactsList(userUid) { currentPeopleList ->

            if (userToAdd !in currentPeopleList){
                currentPeopleList.add(userToAdd)
                val chatroomRef = databaseReference.child("user").child(userUid)
                chatroomRef.child("contacts").setValue(currentPeopleList)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Successfully updated the "people" list
                        } else {
                            // Handle the error here
                        }
                    }
            }

        }
    }








}