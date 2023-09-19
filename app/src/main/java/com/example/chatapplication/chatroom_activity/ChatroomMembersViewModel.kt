package com.example.chatapplication.chatroom_activity

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapplication.new_group.Group
import com.example.chatapplication.new_group.GroupUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatroomMembersViewModel : ViewModel(){

    private var membersList = MutableLiveData<ArrayList<GroupUser>>()
    private var mDbRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun getMembers(context: Context,groupUid:String){
        mDbRef.child("group").child(groupUid).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var membersList:ArrayList<GroupUser> = ArrayList()

                var membersSnapshot = snapshot.child("people")
                for (i in membersSnapshot.children){
                    var temp = i.getValue(GroupUser::class.java)
                    if (temp !=null){
                        membersList.add(temp)
                    }
                }
                this@ChatroomMembersViewModel.membersList.postValue(membersList)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

    fun observeMembers() : MutableLiveData<ArrayList<GroupUser>> {
        return membersList
    }
}