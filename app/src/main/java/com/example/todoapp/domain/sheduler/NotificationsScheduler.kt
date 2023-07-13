package com.example.todoapp.domain.sheduler

import com.example.todoapp.domain.model.TodoItem

interface NotificationsScheduler {
    fun schedule(item:TodoItem)
    fun cancel(id:String)
    fun cancelAll()
}