package com.example.todoapp.di.modules

import android.content.Context
import com.example.todoapp.utils.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Singleton

@Module
class SharedPreferencesHelperModule {

    @Provides
    @Reusable
    fun provideSharedPreferences(context: Context): SharedPreferencesHelper =
        SharedPreferencesHelper(context)
}
