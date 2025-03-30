package com.ikramjamro.attendancemarkingapp.Data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ikramjamro.attendancemarkingapp.Data.entity.Student

@Database(entities = [Student::class], version = 1, exportSchema = false)
abstract class StudentDatabase: RoomDatabase() {
    abstract val studentDao: StudentDao
}