package com.example.todoapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.data.repository.RepositoryImpl
import com.example.todoapp.ui.stateholders.LoginViewModel
import com.example.todoapp.ui.stateholders.MainViewModel
import com.example.todoapp.ui.stateholders.ManageTaskViewModel
import com.example.todoapp.utils.internet_connection.NetworkConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val repositoryImpl: RepositoryImpl,
    private val connectivityObserver: NetworkConnectivityObserver,
    private val coroutineScope: CoroutineScope
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            MainViewModel::class.java -> {
                MainViewModel(repositoryImpl, connectivityObserver, coroutineScope)
            }
            LoginViewModel::class.java -> {
                LoginViewModel(repositoryImpl, coroutineScope)
            }

            ManageTaskViewModel::class.java -> {
                ManageTaskViewModel(repositoryImpl, connectivityObserver, coroutineScope)
            }

            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }

}