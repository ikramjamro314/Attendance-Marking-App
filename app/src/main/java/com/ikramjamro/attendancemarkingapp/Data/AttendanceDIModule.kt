package com.ikramjamro.attendancemarkingapp.Data

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.ikramjamro.attendancemarkingapp.Data.database.StudentDao
import com.ikramjamro.attendancemarkingapp.Data.database.StudentDatabase
import com.ikramjamro.attendancemarkingapp.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AttendanceDIModule{
    @Provides
    @Singleton
    fun provideDataBase(application: Application):StudentDatabase{
       return Room.databaseBuilder(
            application,
            StudentDatabase::class.java,
            "student_db"
        ).build()
    }

    @Provides
    fun repository(db: StudentDatabase): Repository {
        return Repository(db.studentDao)
    }

}