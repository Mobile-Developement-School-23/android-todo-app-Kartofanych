package com.example.todoapp.ui.view.manageTaskCompose.components

import androidx.compose.material.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import com.example.todoapp.theme.Typography
import com.example.todoapp.theme.YandexTodoTheme
import java.sql.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDialog(
    openDatePicker: MutableState<Boolean>,
    dateChanged: MutableState<Boolean>,
    openTimePicker: MutableState<Boolean>,
    datePickerState: DatePickerState,
    onTimeSelected: (Date) -> Unit
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            surface = YandexTodoTheme.colors.backPrimary,
            secondary = YandexTodoTheme.colors.labelPrimary,
            primary = Color(0x4D007AFF),
            onPrimary = YandexTodoTheme.colors.colorBlue,
            surfaceTint = YandexTodoTheme.colors.labelPrimary,
            tertiary = YandexTodoTheme.colors.labelPrimary,
            onSecondary = YandexTodoTheme.colors.labelPrimary,
            )
    ) {
        DatePickerDialog(
            onDismissRequest = {
                openDatePicker.value = false
                dateChanged.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (datePickerState.selectedDateMillis == null) {
                            onTimeSelected(Date(System.currentTimeMillis()))
                        } else {
                            onTimeSelected(Date(datePickerState.selectedDateMillis!!))
                        }
                        openDatePicker.value = false
                        openTimePicker.value = true
                    },
                ) {
                    Text(
                        "Далее",
                        style = Typography.body1,
                        color = YandexTodoTheme.colors.colorBlue
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        dateChanged.value = false
                        openDatePicker.value = false
                    }
                ) {
                    Text(
                        "Отмена",
                        style = Typography.body1,
                        color = YandexTodoTheme.colors.labelTertiary
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    titleContentColor = YandexTodoTheme.colors.labelPrimary,
                    currentYearContentColor = YandexTodoTheme.colors.labelPrimary,
                    dayContentColor = YandexTodoTheme.colors.labelPrimary,
                    disabledDayContentColor = YandexTodoTheme.colors.labelPrimary,
                    headlineContentColor = YandexTodoTheme.colors.labelPrimary,
                    subheadContentColor = YandexTodoTheme.colors.labelPrimary,
                    selectedDayContentColor = YandexTodoTheme.colors.colorWhite,
                    selectedDayContainerColor = YandexTodoTheme.colors.colorBlue,
                    weekdayContentColor = YandexTodoTheme.colors.labelPrimary,
                    yearContentColor = YandexTodoTheme.colors.labelPrimary,
                    disabledSelectedDayContentColor = YandexTodoTheme.colors.labelTertiary,
                    dayInSelectionRangeContentColor = YandexTodoTheme.colors.labelPrimary,
                    selectedYearContentColor = YandexTodoTheme.colors.labelPrimary,
                    todayContentColor = YandexTodoTheme.colors.colorBlue,
                    containerColor = YandexTodoTheme.colors.backSecondary,
                    dayInSelectionRangeContainerColor = YandexTodoTheme.colors.labelPrimary,
                    disabledSelectedDayContainerColor = YandexTodoTheme.colors.labelPrimary
                )
            )
        }
    }
}
