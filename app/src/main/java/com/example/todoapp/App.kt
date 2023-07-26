package com.example.todoapp

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.todoapp.di.components.AppComponent
import com.example.todoapp.di.components.DaggerAppComponent
import com.example.todoapp.utils.SharedPreferencesHelper
import javax.inject.Inject


class App : Application() {


    @Inject
    lateinit var myWorkRequest: PeriodicWorkRequest

    @Inject
    lateinit var sharedPreferencesHelper:SharedPreferencesHelper


    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        appComponent.inject(this)

        sharedPreferencesHelper.setMode(sharedPreferencesHelper.getMode())
        periodicUpdate()
    }

    private fun periodicUpdate() {
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "update_data",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            myWorkRequest
        )
    }




}
