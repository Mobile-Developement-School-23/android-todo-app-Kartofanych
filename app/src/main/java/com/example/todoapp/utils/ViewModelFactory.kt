package com.example.todoapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.data.repository.RepositoryImpl
import com.example.todoapp.ui.stateholders.LoginViewModel
import com.example.todoapp.ui.stateholders.MainViewModel
import com.example.todoapp.ui.stateholders.ManageTaskComposeViewModel
import com.example.todoapp.ui.stateholders.ManageTaskViewModel
import com.example.todoapp.utils.internetConnection.NetworkConnectivityObserver
import com.example.todoapp.utils.notifications.NotificationsSchedulerImpl
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val repositoryImpl: RepositoryImpl,
    private val connectivityObserver: NetworkConnectivityObserver,
    private val coroutineScope: CoroutineScope,
    private val schedulerImpl: NotificationsSchedulerImpl
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            MainViewModel::class.java -> {
                MainViewModel(repositoryImpl, connectivityObserver, coroutineScope)
            }
            LoginViewModel::class.java -> {
                LoginViewModel(repositoryImpl, coroutineScope, schedulerImpl)
            }
            ManageTaskViewModel::class.java -> {
                ManageTaskViewModel(repositoryImpl, coroutineScope)
            }
            ManageTaskComposeViewModel::class.java -> {
                ManageTaskComposeViewModel(repositoryImpl, coroutineScope)
            }

            else -> {
                error("Unknown view model class")
            }
        }
        return viewModel as T
    }

}
