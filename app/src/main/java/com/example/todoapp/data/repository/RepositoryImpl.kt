package com.example.todoapp.data.repository

import android.util.Log
import com.example.todoapp.data.dataSource.network.NetworkSource
import com.example.todoapp.data.dataSource.network.dto.responses.TodoItemResponse
import com.example.todoapp.data.dataSource.room.ToDoItemEntity
import com.example.todoapp.data.dataSource.room.TodoListDao
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.domain.repository.Repository
import com.example.todoapp.domain.model.DataState
import com.example.todoapp.domain.model.UiState
import com.example.todoapp.utils.notifications.NotificationsSchedulerImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val dao: TodoListDao,
    private val networkSource: NetworkSource,
    private val notificationsScheduler: NotificationsSchedulerImpl
) : Repository {

    override fun getAllData(): Flow<UiState<List<TodoItem>>> = flow {
        emit(UiState.Start)
        dao.getAllFlow().collect { list ->
            emit(UiState.Success(list.map { it.toItem() }))
        }
    }

    override suspend fun addItem(todoItem: TodoItem){
        val toDoItemEntity = ToDoItemEntity.fromItem(todoItem)
        dao.add(toDoItemEntity)
        networkSource.postElement(todoItem)
        notificationsScheduler.schedule(todoItem)
    }

    override suspend fun deleteItem(todoItem: TodoItem) {
        val toDoItemEntity = ToDoItemEntity.fromItem(todoItem)
        dao.delete(toDoItemEntity)
        networkSource.deleteElement(todoItem.id)
        notificationsScheduler.cancel(todoItem.id)
    }

    override suspend fun changeItem(todoItem: TodoItem){
        val toDoItemEntity = ToDoItemEntity.fromItem(todoItem)
        dao.updateItem(toDoItemEntity)
        networkSource.updateElement(todoItem)
        notificationsScheduler.schedule(todoItem)

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
                        updateNotifications(state.data)
                    }
                }
            }
    }

    private fun updateNotifications(items: List<TodoItem>) {
        notificationsScheduler.cancelAll()
        for (item in items){
            notificationsScheduler.schedule(item)
        }
    }


    override fun getItem(itemId: String): TodoItem = dao.getItem(itemId).toItem()


    override suspend fun deleteCurrentItems() {
        dao.deleteAll()
    }


}
