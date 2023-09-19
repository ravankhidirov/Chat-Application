package com.example.chatapplication.messaging_with_contact

class DateManager {
    fun getMonthByNumber(number:Int):String{
        return when(number){
            1->"January"
            2->"February"
            3->"March"
            4->"April"
            5->"May"
            6->"June"
            7->"July"
            8->"August"
            9->"September"
            10->"October"
            11->"November"
            else->"December"
        }
    }

}