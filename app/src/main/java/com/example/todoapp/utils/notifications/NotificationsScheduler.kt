package com.example.todoapp.utils.notifications

import com.example.todoapp.domain.model.TodoItem

interface NotificationsScheduler {
    fun schedule(item:TodoItem)
    fun cancel(item: TodoItem)
}