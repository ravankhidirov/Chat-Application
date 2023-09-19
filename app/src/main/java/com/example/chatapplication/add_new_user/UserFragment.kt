package com.example.chatapplication.add_new_user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.chatapplication.R
import com.example.chatapplication.messaging_with_contact.Constants.Companion.ADD_CHATROOM
import com.example.chatapplication.new_group.FirebaseManager
import com.example.chatapplication.registration.user.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserFragment(val myContext:Activity,val account: User,val groupUid:String?) : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val accountName = view.findViewById<TextView>(R.id.accountName)
        accountName.text = account.name.toString()
        val accountEmail = view.findViewById<TextView>(R.id.accountEmail)
        accountEmail.text = account.email.toString()


        val accountProfileImage = view.findViewById<ImageView>(R.id.accountProfileImage)
        val dbRef:DatabaseReference = FirebaseDatabase.getInstance().reference

        val firebaseDataManager = FirebaseManager()
        val addButton = view.findViewById<Button>(R.id.addUserButton)

        val auth = FirebaseAuth.getInstance()

        addButton.setOnClickListener {
            if (groupUid!=null){
                if (account.uid != null)
                {
                    firebaseDataManager.addPersonAndUpdateList(groupUid,account.uid!!)

                    firebaseDataManager.addGroupAndUpdateList(account.uid!!,groupUid)

                    myContext.finish()

                }
            }
        }
        dbRef.child("images").child(account.uid.toString()).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profileLink = snapshot.getValue(String::class.java)
                Glide.with(this@UserFragment)
                    .load(profileLink)
                    .into(accountProfileImage)
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}

