package com.ikramjamro.attendancemarkingapp.Data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object: Migration(1,2){
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("Alter student table Delete Column isPresent")
    }

}