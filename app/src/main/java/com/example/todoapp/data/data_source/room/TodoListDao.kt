package com.example.todoapp.data.data_source.room

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
    fun getAllFlow(): Flow<List<ToDoItemEntity>>


    @Query("SELECT * FROM todolist")
    fun getAll(): List<ToDoItemEntity>

    @Query("SELECT * FROM todolist WHERE id=:itemId")
    fun getItem(itemId: String): ToDoItemEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(todoItem: ToDoItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addList(newItems: List<ToDoItemEntity>)

    @Update
    suspend fun updateItem(toDoItemEntity: ToDoItemEntity)

    @Delete
    suspend fun delete(entity: ToDoItemEntity)

    @Query("DELETE FROM todoList")
    suspend fun deleteAll()

    @Query("UPDATE todolist SET done= :done, changedAt=:time WHERE id = :id")
    suspend fun updateDone(id: String, done: Boolean, time: Long)
}