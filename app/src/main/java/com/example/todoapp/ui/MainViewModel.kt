package com.example.todoapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.repository.ItemsRepository
import com.example.todoapp.room.TodoItem
import com.example.todoapp.utils.localeLazy
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    private val repository: ItemsRepository by localeLazy()
    var modeAll: Boolean = false

    val data = MutableSharedFlow<List<TodoItem>>()
    val countComplete = MutableSharedFlow<Int>()
    private var loadingJob: Job? = null

    var item = MutableSharedFlow<TodoItem>()

    fun changeDone(mode:Boolean){
        modeAll = mode
        loadData()
    }
    private fun loadData() {
        loadingJob?.cancel()
        loadingJob = viewModelScope.launch {
            data.emitAll(repository.getData(modeAll))
            countComplete.emitAll(data.map { it.filter { it.done }.size })
        }
    }

    fun getItem(id:String){
        viewModelScope.launch {
            item.emitAll(repository.getItem(id))
        }
    }


    fun addItem(todoItem: TodoItem) {
        viewModelScope.launch {
            repository.addItem(todoItem)
        }
    }

    fun deleteItem(id: String) {
        viewModelScope.launch {
            repository.deleteItem(id)
        }
    }

    fun updateItem(newItem: TodoItem) {
        viewModelScope.launch {
            repository.changeItem(newItem)
        }
    }


    fun changeItemDone(id: String, done: Boolean) {
        viewModelScope.launch {
            repository.changeDone(id, done)
            countComplete.emitAll(data.map { it.count { it.done }})
        }
    }

    fun getLastId(): String {
        return (0..100000).random().toString()
    }


}