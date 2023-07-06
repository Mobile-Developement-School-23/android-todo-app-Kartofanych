package com.example.todoapp.di

import com.example.todoapp.utils.ViewModelFactory
import com.example.todoapp.ui.view.LoginFragment
import com.example.todoapp.ui.view.MainActivity
import com.example.todoapp.ui.view.ManageTaskFragment
import com.example.todoapp.ui.view.TasksFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [], modules = [ApplicationModule::class, DatabaseModule::class, SharedPreferencesHelperModule::class, NetworkModule::class])
interface AppComponent {

    fun viewModelsFactory(): ViewModelFactory
    fun inject(activity: MainActivity)
    fun inject(fragment:LoginFragment)
    fun inject(fragment:TasksFragment)
    fun inject(fragment:ManageTaskFragment)

}