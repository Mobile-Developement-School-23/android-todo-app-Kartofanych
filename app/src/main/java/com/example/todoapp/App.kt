package com.example.todoapp

import android.app.Application
import android.content.Context
import com.example.todoapp.repository.ItemsRepository
import com.example.todoapp.room.TodoListDatabase
import com.example.todoapp.shared_preferences.SharedPreferencesHelper
import com.example.todoapp.utils.ServiceLocator
import com.example.todoapp.utils.locale

class App:Application() {


    override fun onCreate() {
        super.onCreate()

        ServiceLocator.register<Context>(this)
        ServiceLocator.register(TodoListDatabase.create(locale()))
        ServiceLocator.register(ItemsRepository(locale()))
        ServiceLocator.register(SharedPreferencesHelper(applicationContext))
    }

}