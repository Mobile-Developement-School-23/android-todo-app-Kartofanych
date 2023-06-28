package com.example.todoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.network.NetworkAccess
import com.example.todoapp.repository.ItemsRepository
import com.example.todoapp.room.TodoItem
import com.example.todoapp.shared_preferences.SharedPreferencesHelper
import com.example.todoapp.utils.InternetConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class MainViewModel(
    private val repository: ItemsRepository,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val connection: InternetConnection
) : ViewModel() {

    var modeAll: Boolean = false

    private val _data = MutableSharedFlow<List<TodoItem>>()
    val data: SharedFlow<List<TodoItem>> = _data.asSharedFlow()
    val countComplete: Flow<Int> = _data.map { it.count { item -> item.done } }

    private var _item = MutableStateFlow(TodoItem())
    var item = _item.asStateFlow()

    private var job: Job? = null

    init {
        if(connection.isOnline()){
            loadNetworkList()
        }
        loadData()
    }

    fun changeMode() {
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
        viewModelScope.launch(Dispatchers.IO) {
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
        deleteNetworkItem(todoItem.id)
    }

    fun updateItem(todoItem: TodoItem) {
        todoItem.dateChanged?.time = System.currentTimeMillis()
        updateNetworkItem(todoItem)
        viewModelScope.launch(Dispatchers.IO) {
            repository.changeItem(todoItem)
        }
    }


    fun changeItemDone(todoItem: TodoItem) {
        val item = todoItem.copy(done = !todoItem.done)
        updateNetworkItem(item)
        viewModelScope.launch(Dispatchers.IO) {
            repository.changeDone(todoItem.id, !todoItem.done)
        }
    }

    fun loadNetworkList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getNetworkData()
        }
    }

    private fun uploadNetworkItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                repository.postNetworkItem(sharedPreferencesHelper.getLastRevision(), todoItem)
            when (response) {
                is NetworkAccess.Success -> {
                    sharedPreferencesHelper.putRevision(response.data.revision)
                }

                is NetworkAccess.Error -> {

                }
            }
        }
    }

    private fun deleteNetworkItem(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                repository.deleteNetworkItem(sharedPreferencesHelper.getLastRevision(), id)
            when (response) {
                is NetworkAccess.Success -> {
                    sharedPreferencesHelper.putRevision(response.data.revision)
                }

                is NetworkAccess.Error -> {

                }
            }
        }
    }

    private fun updateNetworkItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNetworkItem(sharedPreferencesHelper.getLastRevision(), todoItem )
        }
    }


    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }


}