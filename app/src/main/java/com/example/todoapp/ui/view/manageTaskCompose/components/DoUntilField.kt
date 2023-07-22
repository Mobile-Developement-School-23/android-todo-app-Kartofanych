package com.example.todoapp.ui.view.manageTaskCompose.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todoapp.theme.Typography
import com.example.todoapp.theme.YandexTodoTheme

@Composable
fun DoUntilField(
    onCheckChange: () -> Unit,
    onSwitchChange: (state: Boolean) -> Unit,
    checkedDate: Boolean,
    formattedDate: String?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCheckChange()
            }
            .padding(vertical = 20.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = "Сделать до",
                style = Typography.body1,
                color = YandexTodoTheme.colors.labelPrimary
            )
            if (checkedDate) {
                Text(
                    text = formattedDate ?: "",
                    style = Typography.body2,
                    color = YandexTodoTheme.colors.colorBlue,
                )
            }
        }
        Switch(
            checked = checkedDate,
            onCheckedChange = { newChecked ->
                onSwitchChange(newChecked)
            },
            modifier = Modifier.padding(end = 16.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = YandexTodoTheme.colors.colorBlue,
                checkedTrackColor = Color(0x4D007AFF),
                uncheckedThumbColor = YandexTodoTheme.colors.backElevated,
                uncheckedTrackColor = YandexTodoTheme.colors.supportOverlay
            )
        )
    }
}
