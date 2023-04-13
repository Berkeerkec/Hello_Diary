package com.berkeerkec.hellodiary.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
import java.sql.Time

@Entity("diary")
data class Diary(
    val title : String,
    val text : String,
    val image : ByteArray,
    val date : String,
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
)