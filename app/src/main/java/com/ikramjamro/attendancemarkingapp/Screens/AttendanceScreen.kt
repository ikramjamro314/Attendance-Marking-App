package com.ikramjamro.attendancemarkingapp.Screens

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ikramjamro.attendancemarkingapp.AppState
import com.ikramjamro.attendancemarkingapp.AppViewModel
import com.ikramjamro.attendancemarkingapp.DestinationScreens
import com.ikramjamro.attendancemarkingapp.commonDivider
import com.ikramjamro.attendancemarkingapp.commonLoader
import com.ikramjamro.attendancemarkingapp.commonRow
import java.io.File
import java.time.LocalDate


@Composable
fun AttendanceScreen(state: AppState = AppState(), nv: NavController, vm: AppViewModel) {
    var context = LocalContext.current
    var show by remember { mutableStateOf(false) }
    val images by vm.imagesState.collectAsState()
    var inProgress by remember { state.inProgress }


    if (inProgress) {
        commonLoader()
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(if (show) Modifier.blur(2.dp) else Modifier),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Attendance",
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2A8BEB),
                        fontSize = 25.sp,
                        modifier = Modifier.padding(6.dp)
                    )

                    IconButton(
                        onClick = {
                            if (state.students.isEmpty() || state.students.all { !it.isPresent }) {
                                Toast.makeText(
                                    context,
                                    "Please mark the attendance first!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                show = true
                            }
                        },
                    )
                    {
                        val share = getEveryImage(context, images, "share")
                        share?.let {
                            Image(
                                painter = rememberAsyncImagePainter(share),
                                contentDescription = "Share Attendance",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10))
                                    .size(30.dp)
                                    .background(color = Color(0x2A8BEB)),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                    if (show) {
                        showDialog(
                            show = show,
                            onDismiss = { show = false },
                            onPresentClick = {
                                vm.shareAttendance(context, true)
                            },
                            onAbsentClick = {
                                vm.shareAttendance(context, false)
                            }
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Student",
                        color = Color(0xFF797777),
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                    Text(
                        text = "Attendance",
                        color = Color(0xFF797777),
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )

                }

                var isSelectedAll by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Text(
                        text = "Total Students: ${state.total}", color = Color(0xFF2A8BEB),
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 19.dp).padding(start = 4.dp)
                    )
                    Row() {
                        Text(
                            text = "Select All", color = Color(0xFF2A8BEB),
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Checkbox(
                            checked = isSelectedAll,
                            onCheckedChange = {
                                isSelectedAll = !isSelectedAll
                                if (isSelectedAll) {
                                    state.students.forEach { student ->
                                        vm.updateAttendance(student.rollNo, newIsPresent = true)
                                    }
                                } else {
                                    state.students.forEach { student ->
                                        vm.updateAttendance(student.rollNo, newIsPresent = false)
                                    }
                                }
                            }
                        )
                    }
                }
                commonDivider()

                if (state.students.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 26.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No Student Added",
                            style = Typography().titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    LazyColumn(
                        Modifier
                            .padding(bottom = 56.dp)
                    ) {
                        items(state.students) { student ->
                            commonRow(
                                name = student.name,
                                rollNo = student.rollNo,
                                initialPresent = student.isPresent,
                                context = context,
                                images = images,
                                onEvent = {
                                    state.id.value = student.id
                                    state.name.value=student.name
                                    state.rollNo.value=student.rollNo
                                    state.isPresent.value=student.isPresent
                                    state.isDelete.value = true
                                    nv.navigate(DestinationScreens.StudentAdd.route)
                                },
                            ) { newIsPresent ->
                                vm.updateAttendance(student.rollNo, newIsPresent)
                            }
                        }
                    }
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.CreateDocument("text/plain")
            ) { uri ->
                uri?.let {
                    vm.saveAttendance(context, uri)
                    Toast.makeText(context, "Attendance Saved SuccessFully", Toast.LENGTH_SHORT)
                        .show()
                } ?: run {
                    Toast.makeText(context, "File not saved", Toast.LENGTH_SHORT).show()
                }
            }

            FloatingActionButton(
                onClick = {
                    nv.navigate(DestinationScreens.StudentAdd.route)
                },
                modifier = Modifier
                    .padding(10.dp)
                    .padding(bottom = 40.dp).alpha(0.8f),
                shape = RoundedCornerShape(20.dp),
                containerColor = Color(0xFF2A8BEB),
                elevation = FloatingActionButtonDefaults.elevation(20.dp)
            ) {
                val add = getEveryImage(context, images, "add")
                Log.d("Add", "$add")
                add?.let {
                    Icon(
                        painter = rememberAsyncImagePainter(add),
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color(0xFF2A8BEB))
                    .clickable {
                        if (state.students.isEmpty() || state.students.all { !it.isPresent }) {
                            Toast.makeText(
                                context,
                                "Please mark the attendance first!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            launcher.launch("${LocalDate.now()}--Attendance.txt")
                        }
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,

                ) {
                val save = getEveryImage(context, images, "save")
                save?.let {
                    Image(
                        painter = rememberAsyncImagePainter(save),
                        contentDescription = null
                    )
                }

                Text(
                    text = "Save",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun showDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onPresentClick: () -> Unit,
    onAbsentClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onDismiss.invoke() },
        contentAlignment = Alignment.Center
    ) {
        if (show) {
            AlertDialog(
                onDismissRequest = { onDismiss.invoke() },
                title = { Text("Share Attendance!") },
                confirmButton = {
                    TextButton(onClick = {
                        onAbsentClick.invoke()
                        onDismiss.invoke()
                    }) {
                        Text("Absents", modifier = Modifier.padding(4.dp), fontSize = 20.sp)
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        onPresentClick.invoke()
                        onDismiss.invoke()
                    }) {
                        Text("Presents", modifier = Modifier.padding(4.dp), fontSize = 20.sp)
                    }
                }
            )
        }
    }
}

fun getEveryImage(context: Context, images: List<File>, imageName: String): Uri? {
    return images.find { it.nameWithoutExtension == imageName }?.let { file ->
        Uri.fromFile(file)
    }
}

