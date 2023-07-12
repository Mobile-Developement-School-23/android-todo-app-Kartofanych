package com.example.todoapp.data.dataSource.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ToDoItemEntity::class], version = 3, exportSchema = false)
abstract class TodoListDatabase : RoomDatabase() {
    abstract val listDao: TodoListDao
}
