package com.example.chatapplication.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "contacts") val contacts: ArrayList<String>?,
    @ColumnInfo(name = "last_name") val lastName: String?
)