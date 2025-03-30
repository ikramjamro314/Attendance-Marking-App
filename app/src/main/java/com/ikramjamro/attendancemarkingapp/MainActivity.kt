package com.ikramjamro.attendancemarkingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ikramjamro.attendancemarkingapp.Screens.AttendanceScreen
import com.ikramjamro.attendancemarkingapp.Screens.SplashScreen
import com.ikramjamro.attendancemarkingapp.Screens.addStudent
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreens(var route: String) {

    object Attendance : DestinationScreens("attendance")
    object Splash : DestinationScreens("splash")
    object StudentAdd : DestinationScreens("studentAdd")

}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val dataStoreManager = DataStoreManager(this)
            AttendanceAppNavigation(dataStoreManager)
        }

    }
}

@Composable
fun AttendanceAppNavigation(
    dataStoreManager: DataStoreManager,
    vm: AppViewModel = hiltViewModel()
) {
    var navController = rememberNavController()
    val appState = vm.state.collectAsState()
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = DestinationScreens.Splash.route) {
        composable(DestinationScreens.Splash.route) {
            SplashScreen(navController = navController, vm = vm)
        }
        composable(DestinationScreens.Attendance.route) {
            AttendanceScreen(state = appState.value, navController, vm)
        }
        composable(DestinationScreens.StudentAdd.route) {
            addStudent(onSave = { vm.insertStudent(context) },
                nv = navController,
                appState = appState.value,
                onDelete = { vm.deleteStudent(context) })
        }

    }

}



