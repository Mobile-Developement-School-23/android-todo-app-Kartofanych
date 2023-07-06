package com.example.todoapp.data.data_source.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ToDoItemEntity::class], version = 3, exportSchema = false)
abstract class TodoListDatabase : RoomDatabase() {
    abstract val listDao: TodoListDao
}