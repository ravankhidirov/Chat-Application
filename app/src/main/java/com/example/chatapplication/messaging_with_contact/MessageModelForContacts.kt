package com.example.chatapplication.messaging_with_contact

class MessageModelForContacts {

    var messageOrUrl: String? = null
    var senderUid: String? = null
    var senderName:String? = null
    var messageType:Int? = null
    var messageTime:Date? = null

    constructor() {}

    constructor(message: String?, senderUid: String?,senderName:String?,messageType:Int?,messageTime:Date?) {
        this.messageOrUrl = message
        this.senderUid = senderUid
        this.senderName = senderName
        this.messageType = messageType
        this.messageTime = messageTime
    }
}

class Date {

    var year: Int? = null
    var month: Int? = null
    var day:Int? = null
    var hour:Int? = null
    var minute:Int? = null

    constructor() {}

    constructor(year: Int?,month: Int?,day: Int?,hour: Int?,minute: Int?) {
        this.year = year
        this.month = month
        this.day = day
        this.hour = hour
        this.minute = minute
    }
}
