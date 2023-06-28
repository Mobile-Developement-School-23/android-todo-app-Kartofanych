package com.example.todoapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.network.NetworkAccess
import com.example.todoapp.repository.ItemsRepository
import com.example.todoapp.room.TodoItem
import com.example.todoapp.shared_preferences.SharedPreferencesHelper
import com.example.todoapp.utils.localeLazy
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.NullPointerException


class MainViewModel: ViewModel() {

    private val sharedPreferencesHelper: SharedPreferencesHelper by localeLazy()
    private val repository: ItemsRepository by localeLazy()

    var modeAll: Boolean = false

    private val _data = MutableSharedFlow<List<TodoItem>>()
    val data: SharedFlow<List<TodoItem>> = _data.asSharedFlow()
    val countComplete: Flow<Int> = _data.map { it.count { item -> item.done } }

    private var _item = MutableStateFlow<TodoItem>(TodoItem())
    var item = _item.asStateFlow()

    private var job: Job? = null

    init {
        loadData()
    }

    fun changeDone() {
        modeAll = !modeAll
        job?.cancel()
        loadData()
    }


    fun loadData() {
        job = viewModelScope.launch(Dispatchers.IO) {
            _data.emitAll(repository.getAllData())
        }
    }


    fun getItem(id: String) {
        viewModelScope.launch (Dispatchers.IO) {
            _item.emit(repository.getItem(id))
        }
    }


    fun addItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addItem(todoItem)
        }
        uploadNetworkItem(todoItem)
    }

    fun deleteItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(todoItem)
        }
    }

    fun updateItem(newItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.changeItem(newItem)
        }
    }


    fun changeItemDone(id: String, done: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.changeDone(id, done)
        }
    }

    fun getLastId(): String {
        return (0..100000).random().toString()
    }

    fun loadNetworkList() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getNetworkData(sharedPreferencesHelper.getLastRevision())
            when (response) {
                is NetworkAccess.Success -> {
                    sharedPreferencesHelper.putRevision(response.data.revision)
                }
                is NetworkAccess.Error -> {

                }
            }
        }
    }

    private fun uploadNetworkItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.postNetworkItem(sharedPreferencesHelper.getLastRevision(), todoItem)
            when (response) {
                is NetworkAccess.Success -> {
                    sharedPreferencesHelper.putRevision(response.data.revision)
                }
                is NetworkAccess.Error -> {

                }
            }
        }
    }

    fun deleteNetworkItem(id:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.deleteNetworkItem(sharedPreferencesHelper.getLastRevision(), id)
            when (response) {
                is NetworkAccess.Success -> {
                    sharedPreferencesHelper.putRevision(response.data.revision)
                }
                is NetworkAccess.Error -> {

                }
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }


}