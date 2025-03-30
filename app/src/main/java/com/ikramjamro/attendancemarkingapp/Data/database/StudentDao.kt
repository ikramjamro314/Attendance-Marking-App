package com.ikramjamro.attendancemarkingapp.Data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ikramjamro.attendancemarkingapp.Data.entity.Student
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Upsert
    suspend fun upsertStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student): Int

    @Query("SELECT * FROM student ORDER BY rollNo ASC")
    fun getAll(): Flow<List<Student>>

    @Query("SELECT COUNT(*) FROM student")
     fun totalStudents(): Flow<Int>
}