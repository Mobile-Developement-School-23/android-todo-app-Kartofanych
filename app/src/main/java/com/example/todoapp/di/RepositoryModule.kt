package com.example.todoapp.di

import com.example.todoapp.data.data_source.network.NetworkSource
import com.example.todoapp.data.data_source.room.TodoListDao
import com.example.todoapp.data.repository.RepositoryImpl
import com.example.todoapp.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface RepositoryModule {
    @Singleton
    @Binds
    fun bindTodoRepository(repositoryImpl: RepositoryImpl):Repository
}