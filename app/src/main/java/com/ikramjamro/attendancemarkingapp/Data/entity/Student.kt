package com.ikramjamro.attendancemarkingapp.Data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Student(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name:String,
    var rollNo: String,
    var isPresent: Boolean=false
)

