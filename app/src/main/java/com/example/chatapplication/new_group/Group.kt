package com.example.chatapplication.new_group


class Group {
    var groupName:String? = null
    var creatorEmail:String? = null
    var creatorUid:String? = null
    var people:List<GroupUser>? = null
    var chatroomUid:String? = null

    constructor(){}
    constructor(groupName:String?,creatorEmail:String?,creatorUid:String?,people:List<GroupUser>?,chatroomUid:String?){
        this.groupName = groupName
        this.creatorEmail = creatorEmail
        this.creatorUid = creatorUid
        this.people = people
        this.chatroomUid = chatroomUid
    }
}


class GroupUser{
    var uid:String? = null
    var access:Int? = null
    constructor(){}
    constructor(uid:String?,access:Int?){
        this.uid = uid
        this.access = access
    }
}