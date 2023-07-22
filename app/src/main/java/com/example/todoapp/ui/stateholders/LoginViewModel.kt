package com.example.todoapp.ui.stateholders

import androidx.lifecycle.ViewModel
import com.example.todoapp.data.repository.RepositoryImpl
import com.example.todoapp.utils.notifications.NotificationsSchedulerImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    private val coroutineScope: CoroutineScope,
    private val schedulerImpl: NotificationsSchedulerImpl
) : ViewModel() {

    fun deleteCurrentItems() {
        schedulerImpl.cancelAll()
        coroutineScope.launch(Dispatchers.IO) {
            repository.deleteCurrentItems()
        }
    }
}
