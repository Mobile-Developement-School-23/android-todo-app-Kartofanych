package com.example.todoapp.di.modules

import com.example.todoapp.data.repository.RepositoryImpl
import com.example.todoapp.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface RepositoryModule {
    @Reusable
    @Binds
    fun bindTodoRepository(repositoryImpl: RepositoryImpl):Repository
}
