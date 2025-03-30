package com.ikramjamro.attendancemarkingapp

import android.content.Context
import androidx.compose.runtime.key
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.contracts.contract

@Singleton
private val Context.DataStore by preferencesDataStore("Images_DataStore")
class DataStoreManager @Inject constructor( @ApplicationContext private val context: Context) {

    val Key = stringPreferencesKey("Images_Key")

   suspend fun addPathToDataStore(path: String){
        context.DataStore.edit { preference->
            preference[Key] = path
        }
    }

    val ImagesPath: Flow<String?> = context.DataStore.data.map { preference->
        preference[Key]?:""
    }

}