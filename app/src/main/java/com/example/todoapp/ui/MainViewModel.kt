package com.example.todoapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.network.NetworkAccess
import com.example.todoapp.repository.ItemsRepository
import com.example.todoapp.room.TodoItem
import com.example.todoapp.shared_preferences.SharedPreferencesHelper
import com.example.todoapp.utils.localeLazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    private val sharedPreferencesHelper: SharedPreferencesHelper by localeLazy()
    private val repository: ItemsRepository by localeLazy()

    var modeAll: Boolean = false

    val data = MutableStateFlow<List<TodoItem>>(listOf())
    val countComplete : Flow<Int> = data.map { it.count{item->item.done} }

    var item = MutableSharedFlow<TodoItem>()

    fun changeDone(mode:Boolean){
        modeAll = mode
        loadData()
    }
    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            data.emitAll(repository.getAllData())
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
        }
    }

    fun getLastId(): String {
        return (0..100000).random().toString()
    }

    fun loadNetworkList(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getNetworkData(sharedPreferencesHelper.getLastRevision())
            when(response){
                is NetworkAccess.Success->{
                    data.emit(response.data.list.map{it.toItem()})
                    sharedPreferencesHelper.putRevision(response.data.revision)
                }
                is NetworkAccess.Error->{

                }
            }
        }
    }

    fun uploadNetworkItem(todoItem: TodoItem){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.postItem(sharedPreferencesHelper.getLastRevision(), todoItem)
            when(response){
                is NetworkAccess.Success->{
                    sharedPreferencesHelper.putRevision(response.data.revision)
                }
                is NetworkAccess.Error->{

                }
            }
        }

    }


}