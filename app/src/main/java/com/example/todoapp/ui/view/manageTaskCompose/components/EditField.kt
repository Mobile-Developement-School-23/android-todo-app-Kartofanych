package com.example.todoapp.ui.view.manageTaskCompose.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todoapp.theme.YandexTodoTheme

@Composable
fun EditField(
    text: String,
    onChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = { value: String ->
            onChanged(value)
        },
        label = { Text("Что надо сделать...") },
        singleLine = false,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 150.dp) // Adjust the maximum height as needed
            .padding(vertical = 8.dp, horizontal = 16.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color(0x00000000),
            focusedLabelColor = YandexTodoTheme.colors.colorBlue,
            unfocusedLabelColor = YandexTodoTheme.colors.labelTertiary,
            cursorColor = YandexTodoTheme.colors.colorBlue,
            placeholderColor = YandexTodoTheme.colors.colorBlue,
            textColor = YandexTodoTheme.colors.labelPrimary,
            focusedBorderColor = YandexTodoTheme.colors.colorBlue,
            unfocusedBorderColor = YandexTodoTheme.colors.labelTertiary

        ),
    )
}
