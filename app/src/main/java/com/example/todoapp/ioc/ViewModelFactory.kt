package com.example.todoapp.ioc

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.App
import com.example.todoapp.ui.stateholders.MainViewModel
import com.example.todoapp.ui.stateholders.NewMainViewModel
import com.example.todoapp.utils.locale

class ViewModelFactory(
    private val app: App
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            MainViewModel::class.java -> {
                MainViewModel(locale(), locale())
            }
            NewMainViewModel::class.java->{
                NewMainViewModel(locale(), locale())
            }

            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }

}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)