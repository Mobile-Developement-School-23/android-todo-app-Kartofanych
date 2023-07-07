package com.example.todoapp.ui.stateholders

import androidx.lifecycle.ViewModel
import com.example.todoapp.data.repository.RepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    private val coroutineScope: CoroutineScope
) : ViewModel() {

    fun deleteCurrentItems() {
        coroutineScope.launch(Dispatchers.IO) {
            repository.deleteCurrentItems()
        }
    }
}
