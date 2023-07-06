package com.example.todoapp.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.data_source.room.TodoListDao
import com.example.todoapp.data.data_source.room.TodoListDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context) = Room.databaseBuilder(
        context,
        TodoListDatabase::class.java,
        "todolist_database"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideTaskDao(database: TodoListDatabase): TodoListDao = database.listDao
}