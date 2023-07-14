package com.example.todoapp.ui.view.manageTaskCompose.previouses

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.theme.YandexTodoTheme
import com.example.todoapp.ui.view.manageTaskCompose.ManageTaskScreen
import com.example.todoapp.ui.view.manageTaskCompose.components.DoUntilField
import com.example.todoapp.ui.view.manageTaskCompose.components.EditField

@Composable
@Preview
private fun MainScreenPreviewLight() {
    YandexTodoTheme(
        darkTheme = false
    ) {
        ManageTaskScreen(
            state = remember { mutableStateOf(TodoItem())},
            onEvent = {},
        )
    }
}
@Composable
@Preview
private fun MainScreenPreviewDark() {
    YandexTodoTheme(
        darkTheme = true
    ) {
        ManageTaskScreen(
            state = remember { mutableStateOf(TodoItem())},
            onEvent = {},
        )
    }
}

@Composable
@Preview(backgroundColor = 0xFFFFFFFF)
private fun EditFieldPrLight(){
    YandexTodoTheme(
        darkTheme = false
    ){
        EditField(
            "",
            {}
        )
    }
}
@Composable
@Preview
private fun EditFieldPrDark(){
    YandexTodoTheme(
        darkTheme = true
    ){
        EditField(
            "Пум пум",
            {}
        )
    }
}


@Composable
@Preview(backgroundColor = 0xFFFFFFFF)
private fun DoUntilPrLight(){
    YandexTodoTheme(
        darkTheme = false
    ){
        DoUntilField(
            {},
            {},
            true,
            "22:16, 15 июня 2023"
        )
    }
}
@Composable
@Preview
private fun DoUntilPrDark(){
    YandexTodoTheme(
        darkTheme = true
    ){
        DoUntilField(
            {},
            {},
            true,
            "22:16, 15 июня 2023"
        )
    }
}