package com.example.chatapplication.registration.user

import com.example.chatapplication.new_group.GroupUser

class User(var name:String? = null,
           var surname:String? = null,
           var email: String? = null,
           var uid: String? = null,
           var contacts:List<String>? = null,
           var groups:List<GroupUser>? = null)
{
    class UserBuilder()
    {
        var name:String? = null
        var surname:String? = null
        var email:String? = null
        var uid:String? = null
        var contacts:List<String>? = null
        var groups:List<GroupUser>? = null

        fun setName(name:String):UserBuilder{
            this.name = name
            return this
        }
        fun setSurname(surname:String):UserBuilder{
            this.surname = surname
            return this
        }
        fun setEmail(email:String):UserBuilder{
            this.email = email
            return this
        }

        fun setUid(uid:String):UserBuilder{
            this.uid = uid
            return this
        }

        fun setContacts(contacts:List<String>?):UserBuilder{
            this.contacts = contacts
            return this
        }
        fun setGroups(groups:List<GroupUser>?):UserBuilder{
            this.groups = groups
            return this
        }

        fun build():User{
            return User(name,surname,email,uid,contacts,groups)
        }
    }
}