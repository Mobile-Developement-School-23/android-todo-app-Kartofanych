package com.example.todoapp.ui.view.list_adapter

import com.example.todoapp.domain.model.TodoItem


interface SwipeCallbackInterface {
    fun onDelete(todoItem: TodoItem)
    fun onChangeDone(todoItem: TodoItem)
}