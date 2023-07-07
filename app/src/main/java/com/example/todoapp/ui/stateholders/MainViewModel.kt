package com.example.todoapp.ui.stateholders

import androidx.lifecycle.ViewModel
import com.example.todoapp.data.repository.RepositoryImpl
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.domain.model.UiState
import com.example.todoapp.utils.internetConnection.ConnectivityObserver
import com.example.todoapp.utils.internetConnection.NetworkConnectivityObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainViewModel @Inject constructor(
    private val repository: RepositoryImpl,
    private val connection: NetworkConnectivityObserver,
    private val coroutineScope: CoroutineScope
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
        coroutineScope.launch {
            connection.observe().collectLatest {
                _status.emit(it)
            }
        }
    }

    fun changeMode() {
        _visibility.value = _visibility.value.not()
    }


    fun loadData(){
        coroutineScope.launch(Dispatchers.IO) {
            _data.emitAll(repository.getAllData())
        }
    }
    fun loadNetworkList(){
        coroutineScope.launch(Dispatchers.IO) {
            _data.emitAll(repository.getNetworkTasks())
        }
    }

    fun deleteItem(todoItem: TodoItem) {
        coroutineScope.launch(Dispatchers.IO) {
            repository.deleteItem(todoItem)
        }
    }

    fun updateItem(task: TodoItem) {
        task.dateChanged?.time = System.currentTimeMillis()
        coroutineScope.launch(Dispatchers.IO) {
            repository.changeItem(task)
        }
    }




}
