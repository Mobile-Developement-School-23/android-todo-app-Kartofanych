package com.example.todoapp.adapter

import com.example.todoapp.room.TodoItem


interface SwipeCallbackInterface {
    fun onDelete(todoItem: TodoItem)
    fun onChangeDone(todoItem: TodoItem)
}