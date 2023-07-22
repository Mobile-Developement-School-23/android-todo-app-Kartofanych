package com.example.todoapp.ui.view.taskList.listAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.todoapp.domain.model.TodoItem

interface OnItemListener {
    fun onItemClick(id: String)

    fun onCheckClick(todoItem: TodoItem)
}

class DealsAdapter(
    private val onItemListener: OnItemListener
) : ListAdapter<TodoItem, ViewHolder>(DiffUtilCallbackImpl()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.create(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemListener)
    }
}

