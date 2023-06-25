package com.example.todoapp.room

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoListDao {

    @Query("SELECT * FROM todoList")
    fun getList(): Flow<List<TodoItem>>
}