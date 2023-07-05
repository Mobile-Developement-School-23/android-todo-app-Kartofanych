package com.example.todoapp.data.repository

import com.example.todoapp.data.data_source.network.NetworkSource
import com.example.todoapp.data.data_source.network.responses.TodoItemResponse
import com.example.todoapp.data.data_source.room.ToDoItemEntity
import com.example.todoapp.data.data_source.room.TodoListDatabase
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.ioc.SharedPreferencesHelper
import com.example.todoapp.utils.DataState
import com.example.todoapp.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow


class ItemsRepository(
    db: TodoListDatabase,
    private val networkSource: NetworkSource
) {

    private val dao = db.listDao

    fun getAllData(): Flow<UiState<List<TodoItem>>> = flow {
        emit(UiState.Start)
        dao.getAllFlow().collect { list ->
            emit(UiState.Success(list.map { it.toItem() }))
        }
    }

    fun getItem(itemId: String): TodoItem = dao.getItem(itemId).toItem()

    suspend fun addItem(todoItem: TodoItem) {
        val toDoItemEntity = ToDoItemEntity.fromItem(todoItem)
        return dao.add(toDoItemEntity)
    }

    suspend fun deleteItem(todoItem: TodoItem) {
        val toDoItemEntity = ToDoItemEntity.fromItem(todoItem)
        return dao.delete(toDoItemEntity)
    }

    suspend fun changeItem(todoItem: TodoItem) {
        val toDoItemEntity = ToDoItemEntity.fromItem(todoItem)
        return dao.updateItem(toDoItemEntity)
    }


    suspend fun getNetworkTasks(): Flow<UiState<List<TodoItem>>> = flow {
        emit(UiState.Start)
        networkSource.getMergedList(dao.getAll().map { TodoItemResponse.fromItem(it.toItem()) })
            .collect { state ->
                when (state) {
                    DataState.Initial -> emit(UiState.Start)
                    is DataState.Exception -> emit(UiState.Error(state.cause.message.toString()))
                    is DataState.Result -> {
                        updateRoom(state.data)
                        emit(UiState.Success(state.data))
                    }
                }
            }
    }

    private suspend fun updateRoom(mergedList: List<TodoItem>) {
        dao.addList(mergedList.map { ToDoItemEntity.fromItem(it) })
    }

    suspend fun postNetworkItem(
        newItem: TodoItem
    ) {
        networkSource.postElement(newItem)
    }

    suspend fun deleteNetworkItem(
        id: String
    ) {
        networkSource.deleteElement(id)
    }

    suspend fun updateNetworkItem(
        item: TodoItem
    ) {
        networkSource.updateElement(item)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }


}