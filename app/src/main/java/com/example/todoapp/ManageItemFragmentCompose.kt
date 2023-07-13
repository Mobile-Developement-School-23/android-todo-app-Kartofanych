package com.example.todoapp

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.theme.Typography
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ManageTaskScreen() {
    @SuppressLint("SimpleDateFormat")
    val dataFormat = SimpleDateFormat("d MMMM y")

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var taskText by rememberSaveable { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(Date(1000000000)) }
    var checkedDate: Boolean by remember { mutableStateOf(false) }
    var deleteState: Boolean by remember { mutableStateOf(true) }

    var openDialog = remember { mutableStateOf(false) }
    val selectedImportance = remember { mutableStateOf("Нет") }

    // State for bottom sheet visibility
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState(true)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = "Manage fragment") },
                navigationIcon = {
                    IconButton(onClick = { /* Handle navigation click */ }) {
                        Icon(Icons.Default.Close, contentDescription = null)
                    }
                },
                actions = {
                    // RowScope here, so these icons will be placed horizontally
                    TextButton(onClick = { /* doSomething() */ }) {
                        Text(text = "Сохранить")
                    }

                }, scrollBehavior = scrollBehavior
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            ) {
                // Content of the fragment goes here
                OutlinedTextField(
                    value = taskText,
                    onValueChange = { taskText = it },
                    label = { Text("Что надо сделать...") },
                    singleLine = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .heightIn(min = 150.dp) // Adjust the maximum height as needed
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { openBottomSheet = true }
                        .padding(vertical = 20.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = "Важность",
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = selectedImportance.value.toLowerCase().capitalize(),
                        modifier = Modifier.alpha(0.5f),
                        color = if (selectedImportance.value == "Высокая") Color.Red else Color.Gray,
                    )
                }
                if (openBottomSheet) {
                    ModalBottomSheet(
                        sheetState = bottomSheetState,
                        onDismissRequest = { openBottomSheet = false },
                    ) {
                        ImportanceBottomSheetContent { importance ->
                            selectedImportance.value = importance
                            if (bottomSheetState.isVisible) {
                                openBottomSheet = false
                            }
                        }
                    }
                }
                Divider(
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(vertical = 20.dp, horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Сделать до",
                            modifier = Modifier
                                .weight(1f)
                                .wrapContentWidth(Alignment.Start)
                                .wrapContentHeight(Alignment.CenterVertically)
                        )
                        Switch(
                            checked = checkedDate,
                            onCheckedChange = { newChecked -> checkedDate = newChecked },
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                    if (checkedDate) {
//                        openDialog.value = true
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    openDialog.value = true
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = Color.Blue
                            )
                            Text(
                                text = dataFormat.format(selectedDate),
                                color = Color.Blue,
                            )
                        }
                        if (openDialog.value) {
                            DatePickerDialogSample(openDialog = openDialog
                            ) { time ->
                                selectedDate = time
                            }
                        }
                    }
                }
                Divider()
                // Delete Task button
                DeleteButton(onClick = {

                }, deleteState)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogSample(openDialog: MutableState<Boolean>, onTimeSelected: (Date) -> Unit) {
    if (openDialog.value) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled = datePickerState.selectedDateMillis != null
        DatePickerDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onTimeSelected(Date(datePickerState.selectedDateMillis!!))
                    },
                    enabled = confirmEnabled
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Удалить")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun DeleteButton(onClick: () -> Unit, state: Boolean) {
    Button(
        onClick = onClick,
        modifier = Modifier.clickable(enabled = state, onClick = onClick),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        elevation = ButtonDefaults.elevation(0.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Удалить",
                tint = if (state) Color.Red else Color.Gray
            )
            Text(
                text = "Удалить",
                modifier = Modifier.padding(start = 4.dp),
                color = if (state) Color.Red else Color.Gray
            )
        }
    }
}

@Composable
fun ImportanceBottomSheetContent(
    onImportanceSelected: (String) -> Unit
) {
    val importanceValues = listOf("Низкая", "Обычная", "Высокая")

    Column(modifier = Modifier.padding(bottom = 30.dp)) {
        importanceValues.forEach { importance ->
            Text(
                text = importance,
                color = if (importance == importanceValues[2]) Color.Red else Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onImportanceSelected(importance) }
                    .padding(horizontal = 20.dp, vertical = 15.dp),
                style = Typography.body1
            )
        }
    }
}

