package com.example.todoapp.domain.repository

import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.domain.model.UiState
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getAllData(): Flow<UiState<List<TodoItem>>>
    fun getItem(itemId: String): TodoItem
    suspend fun addItem(todoItem: TodoItem)
    suspend fun deleteItem(todoItem: TodoItem)
    suspend fun changeItem(todoItem: TodoItem)
    fun getNetworkTasks(): Flow<UiState<List<TodoItem>>>
    suspend fun deleteCurrentItems()
}
