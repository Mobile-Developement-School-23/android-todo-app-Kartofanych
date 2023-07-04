package com.example.todoapp.ui.stateholders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.repository.ItemsRepository
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.utils.Loader
import com.example.todoapp.utils.MyLoader
import com.example.todoapp.utils.internet_connection.NetworkConnectivityObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NewMainViewModel(
    private val repository: ItemsRepository,
    private val connection: NetworkConnectivityObserver
):ViewModel() {

    private val loader:MyLoader = MyLoader(viewModelScope)

    var loadState: Flow<Loader.State<List<TodoItem>>> = loader.state


    fun loadData(){
        viewModelScope.launch {
            loader.load()
        }
    }






}