package com.example.todoapp.ioc

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.App
import com.example.todoapp.data.repository.RepositoryImpl
import com.example.todoapp.ui.stateholders.MainViewModel
import com.example.todoapp.utils.internet_connection.NetworkConnectivityObserver
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val repositoryImpl: RepositoryImpl,
    private val connectivityObserver: NetworkConnectivityObserver
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            MainViewModel::class.java -> {
                MainViewModel(repositoryImpl, connectivityObserver)
            }

            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }

}