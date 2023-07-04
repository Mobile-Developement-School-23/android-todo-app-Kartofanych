package com.example.todoapp

import android.app.Application
import android.content.Context
import com.example.todoapp.data.data_source.room.TodoListDatabase
import com.example.todoapp.data.repository.ItemsRepository
import com.example.todoapp.shared_preferences.SharedPreferencesHelper
import com.example.todoapp.utils.ServiceLocator
import com.example.todoapp.utils.internet_connection.NetworkConnectivityObserver
import com.example.todoapp.utils.locale


class App : Application() {


    override fun onCreate() {
        super.onCreate()

        ServiceLocator.register<Context>(this)
        ServiceLocator.register(SharedPreferencesHelper(this))
        ServiceLocator.register(TodoListDatabase.create(locale()))
        ServiceLocator.register(ItemsRepository(locale(), locale()))
        ServiceLocator.register(NetworkConnectivityObserver(this))

    }

}