package com.ikramjamro.attendancemarkingapp

import com.ikramjamro.attendancemarkingapp.Data.database.StudentDao
import com.ikramjamro.attendancemarkingapp.Data.entity.Student
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class Repository(private val studentDao : StudentDao) {

    suspend fun insertStudent(student: Student) = studentDao.upsertStudent(student)
    suspend fun deleteStudent(student: Student) = studentDao.deleteStudent(student)
    fun getAll(): Flow<List<Student>> = studentDao.getAll().onEach { student-> }
     fun totalStudents() = studentDao.totalStudents()

}