package com.example.todoapp.ui.view.manageTaskCompose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.domain.model.Importance
import com.example.todoapp.theme.Typography
import com.example.todoapp.theme.YandexTodoTheme
import com.example.todoapp.utils.toName

@Composable
fun ImportanceBottomSheetContent(
    onImportanceSelected: (Importance) -> Unit
) {
    val importanceValues = Importance.values()

    Surface(
        shape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp),
        color = YandexTodoTheme.colors.backPrimary
    ) {
        Column(modifier = Modifier.padding(bottom = 10.dp)) {
            importanceValues.forEach { importance ->
                Text(
                    text = importance.toName(),
                    color = if (importance == Importance.URGENT)
                        YandexTodoTheme.colors.colorRed else YandexTodoTheme.colors.labelPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onImportanceSelected(importance) }
                        .padding(horizontal = 20.dp, vertical = 15.dp),
                    style = Typography.body1
                )
            }
        }
    }
}
