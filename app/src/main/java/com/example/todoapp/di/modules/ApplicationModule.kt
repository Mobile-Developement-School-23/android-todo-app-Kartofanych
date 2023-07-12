package com.example.todoapp.di.modules

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
class ApplicationModule {
    @Provides
    @Singleton
    fun provideScope() = CoroutineScope(SupervisorJob())
}
