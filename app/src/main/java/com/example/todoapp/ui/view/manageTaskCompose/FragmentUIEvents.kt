package com.example.todoapp.ui.view.manageTaskCompose

import com.example.todoapp.domain.model.Importance
import java.sql.Date

sealed interface FragmentUIEvents {
    data class ChangeTitle(val text: String) : FragmentUIEvents
    data class ChangeUrgency(val importance: Importance) : FragmentUIEvents
    data class ChangeDeadline(val deadline: Date?) : FragmentUIEvents
    object SaveTodo : FragmentUIEvents
    object DeleteTodo : FragmentUIEvents
    object Close : FragmentUIEvents
}
