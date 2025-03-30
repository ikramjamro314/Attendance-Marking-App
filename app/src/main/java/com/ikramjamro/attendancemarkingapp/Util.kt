package com.ikramjamro.attendancemarkingapp

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ikramjamro.attendancemarkingapp.Screens.getEveryImage
import java.io.File

@Composable
fun commonLoader() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.5f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        CircularProgressIndicator()
    }
}

@Composable
fun commonDivider() {
    Divider(
        color = Color.LightGray,
        thickness = 1.dp,
        modifier = Modifier
            .alpha(0.3f)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun commonRow(
    name: String,
    rollNo: String,
    initialPresent: Boolean,
    context: Context,
    images: List<File>,
    onEvent: () -> Unit,
    onAttendanceChange: (Boolean) -> Unit
) {

    var isPresent by remember(initialPresent) { mutableStateOf(initialPresent) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(8.dp).combinedClickable(
                onClick = {},
                onLongClick = {
                    onEvent.invoke()
                }
            )
    ) {
        val profile = getEveryImage(context, images, "profile")
        profile?.let {
            Image(
                painter= rememberAsyncImagePainter(profile),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFFF4F4F4))
                    .size(50.dp)
                    .padding(6.dp),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(4.dp)
        ) {
            Text(
                text = name,
                color = Color(0xFF797777),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
            )
            Text(
                text = rollNo,
                color = Color(0xFF797777),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
            )
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp), horizontalArrangement = Arrangement.End
        ) {

            IconButton(
                onClick = {
                    isPresent = !isPresent
                    onAttendanceChange(isPresent)
                },
            )
            {
                val tick = getEveryImage(context, images, "tick")
                val cross = getEveryImage(context, images, "cross")
                tick?.let {
                    when {
                        isPresent -> tick
                        else -> cross
                    }?.let { img ->
                        Image(
                            painter= rememberAsyncImagePainter(img),
                            contentDescription = "Mark Attendance",
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(35.dp)
                                .background(color = Color(0x2A8BEB)),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Icon(Icons.Rounded.Delete , contentDescription = "Delete")
    }


    commonDivider()
}