package com.example.todoapp.ui.view.manageTaskCompose.components

import androidx.compose.foundation.ScrollState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.withSave
import com.example.todoapp.R
import com.example.todoapp.theme.YandexTodoTheme
import com.example.todoapp.ui.view.manageTaskCompose.FragmentUIEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    onEvent: (FragmentUIEvents) -> Unit,
    scrollState: ScrollState
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = {
                onEvent(FragmentUIEvents.Close)
            }) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = null,
                    tint = YandexTodoTheme.colors.labelPrimary
                )
            }
        },
        actions = {
            TextButton(onClick = {
                onEvent(FragmentUIEvents.SaveTodo)
            }) {
                Text(
                    text = "Сохранить",
                    style = TextStyle(
                        color = YandexTodoTheme.colors.colorBlue,
                        fontFamily = FontFamily(Font(R.font.medium)),
                        fontSize = 18.sp
                    )
                )
            }

        }, scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = YandexTodoTheme.colors.backPrimary
        ),
        modifier = Modifier
            .shadow(elevation = calculateElevation(scrollValue = scrollState.value.toFloat()))
    )
}

private fun calculateElevation(scrollValue: Float): Dp {
    return if (scrollValue > 0.dp.value) 4.dp else 0.dp
}
