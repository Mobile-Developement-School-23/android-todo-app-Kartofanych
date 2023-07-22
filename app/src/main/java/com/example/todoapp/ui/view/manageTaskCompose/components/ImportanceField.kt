package com.example.todoapp.ui.view.manageTaskCompose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoapp.domain.model.Importance
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.theme.Typography
import com.example.todoapp.theme.YandexTodoTheme
import com.example.todoapp.utils.toName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImportanceField(
    coroutineScope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState,
    state: State<TodoItem>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                coroutineScope.launch {
                    bottomSheetState.show()
                }
            }
            .padding(vertical = 20.dp, horizontal = 16.dp)
    ) {
        Text(
            text = "Важность",
            modifier = Modifier.padding(bottom = 2.dp),
            style = Typography.body1,
            color = YandexTodoTheme.colors.labelPrimary
        )
        Text(
            text = state.value.importance.toName(),
            style = Typography.body2,
            color = if (state.value.importance == Importance.URGENT)
                YandexTodoTheme.colors.colorRed else YandexTodoTheme.colors.labelTertiary,
        )
    }
}
