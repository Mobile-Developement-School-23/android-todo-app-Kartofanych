package com.example.todoapp.data.repository

import com.example.todoapp.data.data_source.network.NetworkSource
import com.example.todoapp.data.data_source.network.dto.responses.TodoItemResponse
import com.example.todoapp.data.data_source.room.ToDoItemEntity
import com.example.todoapp.data.data_source.room.TodoListDao
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.domain.repository.Repository
import com.example.todoapp.domain.model.DataState
import com.example.todoapp.domain.model.ResponseState
import com.example.todoapp.domain.model.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val dao: TodoListDao,
    private val networkSource: NetworkSource
) : Repository {

    override fun getAllData(): Flow<UiState<List<TodoItem>>> = flow {
        emit(UiState.Start)
        dao.getAllFlow().collect { list ->
            emit(UiState.Success(list.map { it.toItem() }))
        }
    }

    override suspend fun addItem(todoItem: TodoItem): Flow<ResponseState> = flow {
        val toDoItemEntity = ToDoItemEntity.fromItem(todoItem)
        dao.add(toDoItemEntity)

        networkSource.postElement(todoItem).collect{
            emit(it)
        }
    }

    override suspend fun deleteItem(todoItem: TodoItem) {
        val toDoItemEntity = ToDoItemEntity.fromItem(todoItem)
        dao.delete(toDoItemEntity)
        try {
            networkSource.deleteElement(todoItem.id)
        } catch (exception: Exception) {
            //exception
        }
    }

    override suspend fun changeItem(todoItem: TodoItem){
        val toDoItemEntity = ToDoItemEntity.fromItem(todoItem)
        dao.updateItem(toDoItemEntity)
        try {
            networkSource.updateElement(todoItem)
        } catch (exception: Exception) {
            //exception
        }
    }


    override fun getNetworkTasks(): Flow<UiState<List<TodoItem>>> = flow {
        emit(UiState.Start)
        networkSource.getMergedList(dao.getAll().map { TodoItemResponse.fromItem(it.toItem()) })
            .collect { state ->
                when (state) {
                    DataState.Initial -> emit(UiState.Start)
                    is DataState.Exception -> emit(UiState.Error(state.cause.message.toString()))
                    is DataState.Result -> {
                        dao.addList(state.data.map { ToDoItemEntity.fromItem(it) })
                        emit(UiState.Success(state.data))
                    }
                }
            }
    }


    override fun getItem(itemId: String): TodoItem = dao.getItem(itemId).toItem()


    override suspend fun deleteCurrentItems() {
        dao.deleteAll()
    }


}