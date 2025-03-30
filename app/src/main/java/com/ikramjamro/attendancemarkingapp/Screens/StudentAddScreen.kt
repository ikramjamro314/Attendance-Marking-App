@file:Suppress("PreviewAnnotationInFunctionWithParameters")

package com.ikramjamro.attendancemarkingapp.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ikramjamro.attendancemarkingapp.AppState
import com.ikramjamro.attendancemarkingapp.AppViewModel
import com.ikramjamro.attendancemarkingapp.DestinationScreens
import com.ikramjamro.attendancemarkingapp.commonLoader

@Preview(showSystemUi = true, showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addStudent(
    vm: AppViewModel = hiltViewModel(),
    appState: AppState = AppState(),
    onSave: () -> Unit = {},
    onDelete: () -> Unit={},
    nv: NavController
) {
    val context = LocalContext.current


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Add Students", fontSize = 25.sp, fontWeight = FontWeight.SemiBold)
                }
            )
        }
    ) {
        it

        val images by vm.imagesState.collectAsState()
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))
            val profile = getEveryImage(LocalContext.current, images, "profile")
            profile?.let {
                Image(
                    painter = rememberAsyncImagePainter(profile),
                    contentDescription = "profile",
                    modifier = Modifier
                        .height(200.dp)
                        .width(200.dp).padding(20.dp)
                )
            }
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = appState.name.value,
                onValueChange = { appState.name.value = it },
                placeholder = { Text("Your Name") },
                label = { Text("Name") },
                modifier = Modifier.padding(10.dp)
            )

            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = appState.rollNo.value,
                onValueChange = { appState.rollNo.value = it },
                placeholder = { Text("Your Roll No") },
                label = { Text("Roll No") },
                modifier = Modifier.padding(10.dp)
            )

            Spacer(Modifier.height(10.dp))
            Button(
                onClick = {
                    if(appState.name.value.isNotEmpty() && appState.rollNo.value.isNotEmpty()){
                        onSave.invoke()
                        nv.navigate(DestinationScreens.Attendance.route){
                            popUpTo(DestinationScreens.Attendance.route){inclusive = true}
                        }

                    }else{
                        Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .padding(horizontal = 60.dp)
            ) {

                Text("Save")
            }

            Spacer(Modifier.height(10.dp))
            if(appState.isDelete.value) {
                Button(
                    onClick = {
                        if (appState.name.value.isNotEmpty() && appState.rollNo.value.isNotEmpty()) {
                            onDelete.invoke()
                            nv.navigate(DestinationScreens.Attendance.route) {
                                popUpTo(DestinationScreens.Attendance.route) { inclusive = true }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Please fill all the fields",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .padding(horizontal = 60.dp)
                ) {

                    Text("Delete")
                }
            }

            if(appState.inProgress.value){
                commonLoader()
            }
        }
    }
}