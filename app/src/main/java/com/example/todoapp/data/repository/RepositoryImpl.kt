package com.example.todoapp.data.repository

import com.example.todoapp.data.data_source.network.NetworkSource
import com.example.todoapp.data.data_source.network.dto.responses.TodoItemResponse
import com.example.todoapp.data.data_source.room.ToDoItemEntity
import com.example.todoapp.data.data_source.room.TodoListDao
import com.example.todoapp.data.data_source.room.TodoListDatabase
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.domain.repository.Repository
import com.example.todoapp.domain.model.DataState
import com.example.todoapp.domain.model.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


class RepositoryImpl @Inject constructor(
    private val dao: TodoListDao,
    private val networkSource: NetworkSource
): Repository {

    override fun getAllData(): Flow<UiState<List<TodoItem>>> = flow {
        emit(UiState.Start)
        dao.getAllFlow().collect { list ->
            emit(UiState.Success(list.map { it.toItem() }))
        }
    }

    override fun getItem(itemId: String): TodoItem = dao.getItem(itemId).toItem()

    override suspend fun addItem(todoItem: TodoItem) {
        val toDoItemEntity = ToDoItemEntity.fromItem(todoItem)
        return dao.add(toDoItemEntity)
    }

    override suspend fun deleteItem(todoItem: TodoItem) {
        val toDoItemEntity = ToDoItemEntity.fromItem(todoItem)
        return dao.delete(toDoItemEntity)
    }

    override suspend fun changeItem(todoItem: TodoItem) {
        val toDoItemEntity = ToDoItemEntity.fromItem(todoItem)
        return dao.updateItem(toDoItemEntity)
    }


    override fun getNetworkTasks(): Flow<UiState<List<TodoItem>>> = flow {
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

    override suspend fun postNetworkItem(
        newItem: TodoItem
    ) {
        networkSource.postElement(newItem)
    }

    override suspend fun deleteNetworkItem(
        id: String
    ) {
        networkSource.deleteElement(id)
    }

    override suspend fun updateNetworkItem(
        item: TodoItem
    ) {
        networkSource.updateElement(item)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }


}