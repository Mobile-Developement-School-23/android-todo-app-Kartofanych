package com.example.todoapp.ui.view.manageTaskCompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.todoapp.domain.model.Importance
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.theme.YandexTodoTheme
import com.example.todoapp.ui.view.manageTaskCompose.components.DateDialog
import com.example.todoapp.ui.view.manageTaskCompose.components.DeleteButton
import com.example.todoapp.ui.view.manageTaskCompose.components.DoUntilField
import com.example.todoapp.ui.view.manageTaskCompose.components.EditField
import com.example.todoapp.ui.view.manageTaskCompose.components.ImportanceBottomSheetContent
import com.example.todoapp.ui.view.manageTaskCompose.components.ImportanceField
import com.example.todoapp.ui.view.manageTaskCompose.components.TimePickerSwitchable
import com.example.todoapp.ui.view.manageTaskCompose.components.Toolbar
import kotlinx.coroutines.launch
import java.sql.Date


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ManageTaskScreen(
    state: State<TodoItem> ,
    onEvent: (FragmentUIEvents) -> Unit ,
) {

    val checkedDate = remember { mutableStateOf(state.value.deadline != null) }
    val deleteState: Boolean by remember { mutableStateOf(state.value.id != "-1") }


    val deadlineDate by remember {
        derivedStateOf {
            state.value.deadlineToString()
        }
    }


    val openDatePicker = remember { mutableStateOf(false) }
    val openTimePicker = remember { mutableStateOf(false) }


    val datePickerState = rememberDatePickerState()

    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()


    val scrollState = rememberScrollState()

    ModalBottomSheetLayout(
        sheetState = bottomSheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            ImportanceBottomSheetContent { importance:Importance ->
                onEvent(FragmentUIEvents.ChangeUrgency(importance = importance))
                coroutineScope.launch {
                    bottomSheetState.hide()
                }
            }
        }
    ) {

        Scaffold(
            modifier = Modifier
                .nestedScroll(TopAppBarDefaults.pinnedScrollBehavior().nestedScrollConnection),
            topBar = { Toolbar(onEvent, scrollState ) },
            content = {

                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxHeight()
                        .verticalScroll(scrollState)
                        .background(YandexTodoTheme.colors.backPrimary)
                ) {

                    EditField(
                        text = state.value.text,
                        onChanged = { newText -> onEvent(FragmentUIEvents.ChangeTitle(newText)) }
                    )
                    ImportanceField(
                        bottomSheetState = bottomSheetState,
                        coroutineScope = coroutineScope,
                        state = state
                    )

                    Divider(color = YandexTodoTheme.colors.labelTertiary)

                    DoUntilField(
                        onCheckChange = {
                            checkedDate.value = true
                            openDatePicker.value = true
                        },
                        onSwitchChange = {
                            checkedDate.value = it
                            if (checkedDate.value) {
                                openDatePicker.value = true
                            } else {
                                onEvent(FragmentUIEvents.ChangeDeadline(null))
                            }
                        },
                        checkedDate.value,
                        deadlineDate
                    )

                    Divider(color = YandexTodoTheme.colors.labelTertiary)

                    DeleteButton(onClick = {
                        onEvent(FragmentUIEvents.DeleteTodo)
                    }, deleteState)
                }

                if (openDatePicker.value) {
                    DateDialog(
                        openDatePicker = openDatePicker,
                        openTimePicker = openTimePicker,
                        datePickerState = datePickerState,
                        dateChanged = checkedDate

                    ) { time:Date ->
                        state.value.deadline = time
                    }
                }

                if (openTimePicker.value) {
                    TimePickerSwitchable(
                        showTimePicker = openTimePicker,
                        selectedDate = state.value.deadline,
                        dateChanged = checkedDate
                    ) { time:Date  ->
                        onEvent(FragmentUIEvents.ChangeDeadline(time))
                    }
                }

            }
        )
    }

}



