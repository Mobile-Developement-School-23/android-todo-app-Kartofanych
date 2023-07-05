package com.example.todoapp.ui.stateholders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.repository.ItemsRepository
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.utils.NetworkState
import com.example.todoapp.utils.UiState
import com.example.todoapp.utils.internet_connection.ConnectivityObserver
import com.example.todoapp.utils.internet_connection.NetworkConnectivityObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.job
import kotlinx.coroutines.launch


class MainViewModel(
    private val repository: ItemsRepository,
    private val connection: NetworkConnectivityObserver
) : ViewModel() {

    private val _status = MutableStateFlow(ConnectivityObserver.Status.Unavailable)
    val status = _status.asStateFlow()

    private val _visibility: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val visibility: StateFlow<Boolean> = _visibility

    private val _data = MutableStateFlow<UiState<List<TodoItem>>>(UiState.Start)
    val data: StateFlow<UiState<List<TodoItem>>> = _data.asStateFlow()


    val countComplete = _data.map{ state->
        when(state){
            is UiState.Success->{
                state.data.count{it.done}
            }else->{
                0
            }
        }
    }

    private var _item = MutableStateFlow(TodoItem())
    var item = _item.asStateFlow()


    init {
        observeNetwork()
        loadData()
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            connection.observe().collectLatest {
                _status.emit(it)
            }
        }
    }

    fun changeMode() {
        _visibility.value = _visibility.value.not()
    }


    fun loadData(){
        viewModelScope.launch(Dispatchers.IO) {
            _data.emitAll(repository.getAllData())
        }
    }
    fun loadNetworkList(){
        viewModelScope.launch(Dispatchers.IO) {
            _data.emitAll(repository.getNetworkTasks())
        }
    }


    fun getItem(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _item.value = repository.getItem(id)
        }
    }

    fun nullItem() {
        _item.value = TodoItem()
    }


    fun addItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addItem(todoItem)
        }
    }

    fun deleteItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(todoItem)
        }
    }

    fun setTask(task: TodoItem) {
        task.dateChanged?.time = System.currentTimeMillis()
        viewModelScope.launch(Dispatchers.IO) {
            repository.changeItem(task)
        }
    }
    fun uploadNetworkItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.postNetworkItem(todoItem)
        }
    }

    fun deleteNetworkItem(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNetworkItem(id)
        }
    }

    fun updateNetworkItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateNetworkItem(todoItem)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
            Log.d("1", "HEY")
            loadNetworkList()
        }
    }


}