package com.ikramjamro.attendancemarkingapp

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.File

private val Context.dataStore by preferencesDataStore("AttendanceAppDataStore")

class AppDataStore(private val context: Context, private val viewModel: AppViewModel) {

//val key = stringPreferencesKey("Image_Path")
//
//
//    suspend fun addImage(path: String){
//        context.dataStore.edit(){
//           it[key] = viewModel.addImageToInternalStorage(context)
//        }
//    }
//
//    suspend fun getImageFiles(): List<File> {
//        val directoryPath = context.dataStore.data.map { it[key]  }.first()
//        val directory = File(directoryPath.toString())
//        return directory.listFiles()?.filter {
//            it.extension == "png"
//        }?: emptyList()
//    }

}