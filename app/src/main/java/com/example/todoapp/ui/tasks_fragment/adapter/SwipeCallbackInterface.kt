package com.example.todoapp.ui.tasks_fragment.adapter

import com.example.todoapp.data_source.room.TodoItem


interface SwipeCallbackInterface {
    fun onDelete(todoItem: TodoItem)
    fun onChangeDone(todoItem: TodoItem)
}