package com.example.todoapp

import android.app.Application
import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.todoapp.data_source.room.TodoListDatabase
import com.example.todoapp.repository.ItemsRepository
import com.example.todoapp.shared_preferences.SharedPreferencesHelper
import com.example.todoapp.utils.MyWorkManager
import com.example.todoapp.utils.ServiceLocator
import com.example.todoapp.utils.internet_connection.NetworkConnectivityObserver
import com.example.todoapp.utils.locale
import com.yandex.authsdk.YandexAuthSdk
import java.util.concurrent.TimeUnit


class App:Application() {


    override fun onCreate() {
        super.onCreate()

        ServiceLocator.register<Context>(this)
        ServiceLocator.register(SharedPreferencesHelper(this))
        ServiceLocator.register(TodoListDatabase.create(locale()))
        ServiceLocator.register(ItemsRepository(locale(), locale()))
        ServiceLocator.register(NetworkConnectivityObserver(this))

    }

}