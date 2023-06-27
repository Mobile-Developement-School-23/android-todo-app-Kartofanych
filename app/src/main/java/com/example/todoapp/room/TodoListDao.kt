package com.example.todoapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoListDao {

    @Query("SELECT * FROM todolist")
    fun getAllFlow(): Flow<List<ToDoItemEntity>>


    @Query("SELECT * FROM todolist")
    fun getAll(): List<ToDoItemEntity>

    @Query("SELECT * FROM todolist WHERE id=:itemId")
    fun getItem(itemId: String): Flow<ToDoItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(todoItem: ToDoItemEntity)

    @Update
    suspend fun update(newItem: ToDoItemEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addList(newItems: List<ToDoItemEntity>)

    @Query("DELETE FROM todoList WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM todoList")
    suspend fun deleteAll()

    @Query("UPDATE todolist SET done= :done WHERE id = :id")
    suspend fun updateDone(id: String, done: Boolean)
}