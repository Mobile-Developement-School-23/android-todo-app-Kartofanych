package com.example.todoapp.ui.view.listAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.domain.model.TodoItem

class DiffUtilCallbackImpl : DiffUtil.ItemCallback<TodoItem>() {
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean =
        oldItem == newItem

}
