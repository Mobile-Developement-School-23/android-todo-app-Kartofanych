package com.example.todoapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.data.ItemsRepository
import com.example.todoapp.data.models.TodoItem
import com.example.todoapp.utils.Filter


class MainViewModel : ViewModel() {



    private val repository = ItemsRepository()
    private var modeAll:Boolean = false
    private var filter:Filter = Filter.PRIORITY
    private var currentItem = TodoItem()

    val numberDone:MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val data: MutableLiveData<List<TodoItem>> by lazy {
        MutableLiveData<List<TodoItem>>()
    }

    init {
        data.value = repository.getData(filter).filter { !it.done }
        numberDone.value = repository.getNumDone()
    }



    fun getData(mode:Boolean, filt: Filter) : LiveData<List<TodoItem>>{
        modeAll = mode
        filter = filt
        loadData()
        return data
    }


    private fun loadData() {
        numberDone.postValue(repository.getNumDone())
        when(modeAll){
            true -> data.postValue(repository.getData(filter))
            false -> data.postValue(repository.getData(filter).filter { !it.done })
        }
    }


    fun removeData(id:String){
        repository.removeData(id)
        loadData()
    }


    fun addItem(todoItem: TodoItem){
        repository.addData(todoItem)
    }

    fun updateItem(todoItem: TodoItem){
        repository.updateItem(todoItem)
        loadData()
    }


    fun changeDone(id: String, done: Boolean) {
        repository.changeStatus(id, done)
        loadData()
    }

    fun getItem(id: String) : TodoItem {
        currentItem = repository.getItem(id)
        return currentItem
    }


    fun getLastId():Int{
        return repository.getLastId()
    }

}