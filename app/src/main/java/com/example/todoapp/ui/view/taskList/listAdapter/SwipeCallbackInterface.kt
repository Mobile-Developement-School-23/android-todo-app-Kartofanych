package com.example.todoapp.ui.view.taskList.listAdapter

import com.example.todoapp.domain.model.TodoItem


interface SwipeCallbackInterface {
    fun onDelete(todoItem: TodoItem)
    fun onChangeDone(todoItem: TodoItem)
}
