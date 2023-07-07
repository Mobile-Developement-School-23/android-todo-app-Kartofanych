package com.example.todoapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.todoapp.di.components.AppComponent
import com.example.todoapp.di.components.DaggerAppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject


class App : Application() {


    @Inject
    lateinit var myWorkRequest: PeriodicWorkRequest

    @Inject
    lateinit var coroutineScope: CoroutineScope


    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        appComponent.inject(this)

        periodicUpdate()
    }

    private fun periodicUpdate() {
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "update_data",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            myWorkRequest
        )
    }


    override fun onTerminate() {
        super.onTerminate()
        coroutineScope.cancel()
    }


}
