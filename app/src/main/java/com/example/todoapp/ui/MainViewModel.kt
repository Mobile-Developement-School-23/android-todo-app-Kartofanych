package com.example.todoapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.network.NetworkAccess
import com.example.todoapp.repository.ItemsRepository
import com.example.todoapp.room.TodoItem
import com.example.todoapp.utils.localeLazy
import kotlinx.coroutines.Dispatchers
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
        loadingJob?.cancel()
        modeAll = mode
        loadData()
    }
    private fun loadData() {
        when(modeAll){
            true -> loadAllData()
            false -> loadToDoData()
        }
    }
    private fun loadAllData() {
        loadingJob = viewModelScope.launch {
            data.emitAll(repository.getAllData())
        }
    }

    private fun loadToDoData() {
        loadingJob = viewModelScope.launch {
            data.emitAll(repository.getToDoData())
        }
    }


    fun getItem(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            item.emitAll(repository.getItem(id))
        }
    }


    fun addItem(todoItem: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addItem(todoItem)
        }
    }

    fun deleteItem(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(id)
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
            countComplete.emitAll(data.map { it.count { it.done }})
        }
    }

    fun getLastId(): String {
        return (0..100000).random().toString()
    }

    fun loadNetworkList(){
        viewModelScope.launch(Dispatchers.IO) {
            val state = repository.getNetworkData()
            when(state){
                is NetworkAccess.Success->{
                    data.emit(state.data.list.map{it.toItem()})
                }
                is NetworkAccess.Error->{

                }
            }
        }

    }


}