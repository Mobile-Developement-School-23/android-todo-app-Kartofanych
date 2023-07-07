package com.example.todoapp.di.components

import android.content.Context
import com.example.todoapp.App
import com.example.todoapp.di.modules.ApplicationModule
import com.example.todoapp.di.modules.DatabaseModule
import com.example.todoapp.di.modules.NetworkModule
import com.example.todoapp.di.modules.RepositoryModule
import com.example.todoapp.di.modules.SharedPreferencesHelperModule
import com.example.todoapp.di.modules.WorkManagerModule
import com.example.todoapp.di.scopes.AppScope
import com.example.todoapp.ui.view.MainActivity
import com.example.todoapp.utils.MyWorkManager
import com.example.todoapp.utils.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@AppScope
@Singleton
@Component(
    dependencies = [],
    modules = [ApplicationModule::class,
        DatabaseModule::class,
        SharedPreferencesHelperModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        WorkManagerModule::class]
)
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(workManager: MyWorkManager)
    fun inject(app:App)
    fun viewModelsFactory(): ViewModelFactory
    fun loginFragmentComponentBuilder(): LoginFragmentComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}
