package com.example.todoapp.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoListDao {

    @Query("SELECT * FROM todolist")
    fun getAll(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todolist WHERE done == 0")
    fun getToDo(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todolist WHERE id=:itemId")
    fun getItem(itemId: String): Flow<TodoItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(todoItem: TodoItem)

    @Update
    suspend fun update(newItem: TodoItem)

    @Delete
    suspend fun delete(todoItem: TodoItem)

    @Query("UPDATE todolist SET done= :done WHERE id = :id")
    suspend fun updateDone(id: String, done: Boolean)
}