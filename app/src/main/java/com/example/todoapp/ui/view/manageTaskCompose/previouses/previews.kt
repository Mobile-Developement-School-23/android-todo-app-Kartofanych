package com.example.todoapp.ui.view.manageTaskCompose.previouses

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.theme.Typography
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

@Preview("Light Theme")
@Preview("Dark Theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppThemePreview() {
    YandexTodoTheme {
        Surface {
            Column {
                ItemPreview(
                    background = YandexTodoTheme.colors.backPrimary,
                    textColor = YandexTodoTheme.colors.labelPrimary,
                    textStyle = Typography.h1,
                    text = "Primary",
                )
                ItemPreview(
                    background = YandexTodoTheme.colors.backSecondary,
                    textColor = YandexTodoTheme.colors.labelSecondary,
                    textStyle = Typography.h2,
                    text = "Secondary",
                )
                ItemPreview(
                    background = YandexTodoTheme.colors.backSecondary,
                    textColor = YandexTodoTheme.colors.labelPrimary,
                    textStyle = Typography.body1,
                    text = "Background",
                )
                ItemPreview(
                    background = YandexTodoTheme.colors.colorRed,
                    textColor = YandexTodoTheme.colors.labelPrimary,
                    textStyle = Typography.body1,
                    text = "Red",
                )
                ItemPreview(
                    background = YandexTodoTheme.colors.colorGreen,
                    textColor = YandexTodoTheme.colors.labelPrimary,
                    textStyle = Typography.body1,
                    text = "Green",
                )
                ItemPreview(
                    background = YandexTodoTheme.colors.colorBlue,
                    textColor = YandexTodoTheme.colors.labelPrimary,
                    textStyle = Typography.body1,
                    text = "Blue",
                )
                ItemPreview(
                    background = YandexTodoTheme.colors.colorGray,
                    textColor = YandexTodoTheme.colors.labelPrimary,
                    textStyle = Typography.body1,
                    text = "Separator",
                )
                ItemPreview(
                    background = YandexTodoTheme.colors.colorGrayLight,
                    textColor = YandexTodoTheme.colors.labelPrimary,
                    textStyle = Typography.body1,
                    text = "Gray",
                )
                ItemPreview(
                    background = YandexTodoTheme.colors.colorGray,
                    textColor = YandexTodoTheme.colors.labelPrimary,
                    textStyle = Typography.body1,
                    text = "Disabled",
                )
                ItemPreview(
                    background = YandexTodoTheme.colors.labelTertiary,
                    textColor = YandexTodoTheme.colors.labelPrimary,
                    textStyle = Typography.body1,
                    text = "Tertiary",
                )
            }
        }
    }
}

@Composable
fun ItemPreview(
    background: Color,
    textColor: Color,
    textStyle: TextStyle,
    text: String,
) {
    Box(
        modifier = Modifier
            .background(background)
            .fillMaxWidth()
            .border(0.5.dp, Color.Black)
            .height(50.dp)
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = textColor,
            style = textStyle,
        )
    }
}