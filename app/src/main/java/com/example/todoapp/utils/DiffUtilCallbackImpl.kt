package com.example.todoapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.room.TodoItem

class DiffUtilCallbackImpl(
    private val oldItems : List<TodoItem>,
    private val newItems : List<TodoItem>,
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem.done == newItem.done && oldItem.id == newItem.id
                && oldItem.text == newItem.text
    }
}