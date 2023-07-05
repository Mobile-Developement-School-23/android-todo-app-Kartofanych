package com.example.todoapp

import android.app.Application
import android.content.Context
import com.example.todoapp.data.data_source.network.NetworkSource
import com.example.todoapp.data.data_source.room.TodoListDatabase
import com.example.todoapp.data.repository.RepositoryImpl
import com.example.todoapp.ioc.SharedPreferencesHelper
import com.example.todoapp.utils.ServiceLocator
import com.example.todoapp.utils.internet_connection.NetworkConnectivityObserver
import com.example.todoapp.utils.locale


class App : Application() {


    override fun onCreate() {
        super.onCreate()

        ServiceLocator.register<Context>(this)
        ServiceLocator.register(SharedPreferencesHelper(this))
        ServiceLocator.register(TodoListDatabase.create(locale()))
        ServiceLocator.register(NetworkSource(locale()))
        ServiceLocator.register(RepositoryImpl(locale(), locale()))
        ServiceLocator.register(NetworkConnectivityObserver(this))

    }

}