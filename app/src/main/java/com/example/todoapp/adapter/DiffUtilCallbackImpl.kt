package com.example.todoapp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.room.TodoItem

class DiffUtilCallbackImpl : DiffUtil.ItemCallback<TodoItem>(){
    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean = oldItem == newItem

}