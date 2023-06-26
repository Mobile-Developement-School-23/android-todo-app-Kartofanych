package com.example.todoapp.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.todoapp.room.TodoItem

interface OnItemListener{
    fun onItemClick(id: String)

    fun onCheckClick(id:String, done:Boolean)
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

