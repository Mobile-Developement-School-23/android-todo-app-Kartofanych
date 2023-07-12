package com.example.todoapp.di.components

import com.example.todoapp.di.modules.YandexAuthModule
import com.example.todoapp.di.scopes.FragmentScope
import com.example.todoapp.ui.view.login.LoginFragment
import dagger.Subcomponent


@FragmentScope
@Subcomponent(modules = [YandexAuthModule::class])
interface LoginFragmentComponent {
    fun inject(fragment: LoginFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): LoginFragmentComponent
    }
}
