package com.ikramjamro.attendancemarkingapp.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ikramjamro.attendancemarkingapp.AppViewModel
import com.ikramjamro.attendancemarkingapp.DestinationScreens
import com.ikramjamro.attendancemarkingapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@SuppressLint("SuspiciousIndentation")
@Composable
fun SplashScreen(navController: NavController, vm: AppViewModel) {

    LaunchedEffect(Unit) {
        delay(5000)
        withContext(Dispatchers.Main) {  // Ensure navigation runs on the main thread
            navController.navigate(DestinationScreens.Attendance.route) {
                popUpTo(0){
                    inclusive= true
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(Modifier.height(10.dp))

        Text(
            text = "Smart Attendance System",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            fontSize = 25.sp,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(Modifier.height(10.dp))

        Image(
            painter = painterResource(R.drawable.splash),
            contentDescription = null,
            modifier = Modifier.size(200.dp).padding(20.dp)
        )

    }

    Box(Modifier
        .fillMaxSize()
        .padding(16.dp), contentAlignment = Alignment.BottomCenter)
    {
        Text(
            text = "Developed by Ikram Jamro",
            color = Color.Black,
            fontFamily = FontFamily.Cursive,
            fontSize = 20.sp
        )
    }

}