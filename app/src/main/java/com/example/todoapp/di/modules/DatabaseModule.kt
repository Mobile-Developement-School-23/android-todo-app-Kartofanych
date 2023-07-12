package com.example.todoapp.di.modules

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.dataSource.room.TodoListDao
import com.example.todoapp.data.dataSource.room.TodoListDatabase
import dagger.Module
import dagger.Provides
import dagger.Reusable
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
    @Reusable
    fun provideTaskDao(database: TodoListDatabase): TodoListDao = database.listDao
}
