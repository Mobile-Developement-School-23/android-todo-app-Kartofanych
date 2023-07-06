package com.example.todoapp

import android.app.Application
import com.example.todoapp.di.AppComponent
import com.example.todoapp.di.ApplicationModule
import com.example.todoapp.di.DaggerAppComponent


class App : Application() {


    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()

    }

}