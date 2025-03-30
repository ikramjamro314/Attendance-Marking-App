package com.ikramjamro.attendancemarkingapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikramjamro.attendancemarkingapp.Data.entity.Student
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val repository: Repository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    init {
        viewModelScope.launch {
            val path = dataStoreManager.ImagesPath?.firstOrNull() ?: ""
            println("Debug: Assigning path to _imageDir -> $path")
            _imageDir.value = path?:""
            getImages(context)
        }
    }

    private val _state = MutableStateFlow(AppState())

    private val allStudents = repository.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList<Student>()
    )

    private val totalStudent = repository.totalStudents().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = 0
    )

        var state = combine(_state , allStudents , totalStudent) { state, students, total->
            state.copy(students = students , total = total)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppState()
        )

    private val _images = MutableStateFlow<List<File>>(emptyList())
    val imagesState: StateFlow<List<File>> = _images

    private val _imageDir = mutableStateOf("")
    val imageDir: String get() = _imageDir.value

//    var students = mutableListOf(
//        Student("Marukh", "22SW002"),
//        Student("Jam Yousuf", "22SW005"),
//        Student("Tanveer", "22SW008"),
//        Student("Mehtab Ali", "22SW011"),
//        Student("Ghazanfar Ali", "22SW014"),
//        Student("Aisha Khan", "22SW017"),
//        Student("Hafsa", "22SW020"),
//        Student("Haris", "22SW026"),
//        Student("Uzair", "22SW029"),
//        Student("Muzammil Khushik", "22SW032"),
//        Student("Varun Kumar", "22SW035"),
//        Student("Fatima Zubair", "22SW038"),
//        Student("Asfand Ali", "22SW041"),
//        Student("Jai kumar", "22SW044"),
//        Student("Abdul Waseo", "22SW047"),
//        Student("Sanjay Kumar", "22SW050"),
//        Student("Muhammad Sajid", "22SW053"),
//        Student("Neeraj Kumar", "22SW056"),
//        Student("Ikram Jamro", "22SW059"),
//        Student("Syed Ghazanfar", "22SW062"),
//        Student("Asif Khoso", "22SW065"),
//        Student("Muhammad Khan", "22SW068"),
//        Student("Tufail", "22SW071"),
//        Student("Muzammil Dars", "22SW074"),
//        Student("Zaheer", "22SW077"),
//        Student("Safiullah", "22SW080"),
//        Student("Adeel", "22SW102"),
//        Student("Aliza Preet", "22SW105"),
//        Student("Hassan", "22SW108"),
//        Student("Haseeb", "22SW117"),
//        Student("Zarawar", "22SW120"),
//        Student("Attiya", "22SW123"),
//        Student("Shoaib", "22SW126"),
//        Student("Saif", "22SW129"),
//        Student("Fuzail", "22SW132"),
//        Student("Nashraah Rauf", "22SW135"),
//        Student("Hassan Patoli", "22SW141"),
//        Student("Hassnain Ali", "22SW144"),
//        Student("Raheem", "22SW147"),
//        Student("Dayyan", "22SW150"),
//        Student("Rafay", "22SW156"),
//        Student("Mohid", "22SW159"),
//        Student("Ahsan", "22SW162"),
//        Student("Gotam Kumar", "22-21SW132"),
//        Student("Hannan Rehman", "22-21SW139"),
//    )




    fun insertStudent(context: Context) {
        state.value.inProgress.value=true
        val student = Student(
            id = state.value.id.value,
            name = state.value.name.value,
            rollNo = state.value.rollNo.value,
            isPresent = state.value.isPresent.value
        )
        viewModelScope.launch {
            repository.insertStudent(student)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Student Added!", Toast.LENGTH_SHORT).show()
            }
        }

        state.value.name.value = ""
        state.value.rollNo.value = ""
        state.value.id.value=0
        state.value.isPresent.value=false
        state.value.inProgress.value=false
    }

    fun deleteStudent(context: Context) {
        state.value.inProgress.value=true
        val student = Student(
            id = state.value.id.value,
            name = state.value.name.value,
            rollNo = state.value.rollNo.value,
            isPresent = state.value.isPresent.value
        )
        viewModelScope.launch(Dispatchers.IO) {
            val deletedRows = repository.deleteStudent(student)
            Log.d("RoomDB", "Deleted rows: $deletedRows")

            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Student Deleted!", Toast.LENGTH_SHORT).show()
            }
        }

        state.value.name.value = ""
        state.value.rollNo.value = ""
        state.value.id.value=0
        state.value.isPresent.value=false
        state.value.isDelete.value=false
        state.value.inProgress.value=false
    }



    fun updateAttendance(rollNo: String, newIsPresent: Boolean) {
        state.value.inProgress.value=true
        state.value.students = state.value.students.map { student ->
            if (student.rollNo == rollNo) {
                student.copy(isPresent = newIsPresent)
            } else student
        }.toMutableList()
        state.value.inProgress.value=false
    }

    fun attendanceData(allPresents: Boolean): String {
        state.value.inProgress.value=true
        var countPresent = 0
        var countAbsent = 0
        var attendanceString = buildString {
            if (allPresents) {
                append("Attendance List-- ${LocalDate.now()} \n----------------------------------------\nAll Presents----\n")
            } else {
                append("Attendance List-- ${LocalDate.now()} \n----------------------------------------\nAll Absents----\n")
            }
           state.value.students.forEach { student ->
                if (allPresents) {
                    if (student.isPresent) {
                        countPresent++
                        append("${student.rollNo}\n")
                    }
                } else
                    if (!student.isPresent) {
                        countAbsent++
                        append("${student.rollNo}\n")
                    }
            }
            if (allPresents) append("----------------------------------------\nTotal Presents: $countPresent")
            else append("----------------------------------------\nTotal Absents: $countAbsent")
        }
        state.value.inProgress.value=false
        return attendanceString
    }


    fun shareAttendance(context: Context, allPresents: Boolean) {
        state.value.inProgress.value=true

        var attendanceString = attendanceData(allPresents)

        var attendanceIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, attendanceString)
            type = "text/plain"
        }

        context.startActivity(Intent.createChooser(attendanceIntent, "shareAttendance"))
        state.value.inProgress.value=false
    }

    fun saveAttendance(context: Context, uri: Uri) {
        state.value.inProgress.value=true
        var attendanceString = attendanceData(true)

        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(attendanceString.toByteArray()) // Write the data
                outputStream.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Some Data in the file loses!!", Toast.LENGTH_SHORT).show()
            state.value.inProgress.value=false
        }
        state.value.inProgress.value=false
    }

    val imagesList = listOf(
        R.drawable.tick to "tick",
        R.drawable.cross to "cross",
        R.drawable.share to "share",
        R.drawable.profile to "profile",
        R.drawable.add to "add",
        R.drawable.save_foreground to "save"
    )

    suspend fun addImages(context: Context): String {
        state.value.inProgress.value=true

        val directory = File(context.filesDir, "All_Images")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        imagesList.forEach { (resId, imageName) ->

            val file = File(directory, "$imageName.png")
            val drawable = AppCompatResources.getDrawable(context, resId)
            val bitmap = drawable?.toBitmap()

            if (bitmap == null) {
                Toast.makeText(context, "Img is null", Toast.LENGTH_SHORT).show()
                state.value.inProgress.value=false
                return@forEach
            }
            try {
                if (!file.exists()) {
                    file.outputStream().use {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
                state.value.inProgress.value=false
            }
        }
        dataStoreManager.addPathToDataStore(directory.absolutePath)
        return directory.absolutePath
    }

    suspend fun getImages(context: Context): List<File> {
        state.value.inProgress.value=true
        var images = mutableListOf<File>()

        viewModelScope.launch(Dispatchers.IO) {
            var dirPath = imageDir.ifEmpty { addImages(context) }
            val directory = File(dirPath)
            if (!directory.exists() || !directory.isDirectory()) {
                state.value.inProgress.value=false
                Toast.makeText(context, "Images Directory Not Exists", Toast.LENGTH_SHORT)
                    .show()
                return@launch
            }
            images = (directory.listFiles()?.filter {
                it.extension == "png"
            } ?: emptyList()).toMutableList()

            _images.value = images

            images.forEach { image ->
                ImageCache.getCachedBitmap(image)
            }
            state.value.inProgress.value=false
        }
        return images ?: emptyList()
    }

    object ImageCache {
        private val imageCache = mutableMapOf<String, Bitmap>() // Cache for images
        fun getCachedBitmap(file: File): Bitmap? {
            return imageCache.getOrPut(file.absolutePath) {
                BitmapFactory.decodeFile(file.absolutePath)
                    ?: return null// Decode only if not cached
            }
        }
    }
}

data class AppState(
    val inProgress: MutableState<Boolean> = mutableStateOf(false),
    var students: List<Student> = emptyList<Student>(),
    val name: MutableState<String> = mutableStateOf(""),
    val id: MutableIntState = mutableIntStateOf(0),
    val rollNo: MutableState<String> = mutableStateOf(""),
    val isPresent: MutableState<Boolean> = mutableStateOf(false),
    var total: Int = 0,
    var isDelete: MutableState<Boolean> = mutableStateOf(false)
)

