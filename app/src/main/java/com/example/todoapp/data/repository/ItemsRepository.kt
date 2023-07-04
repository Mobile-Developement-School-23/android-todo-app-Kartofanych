package com.example.todoapp.data.repository

import android.util.Log
import com.example.todoapp.data.data_source.network.Common
import com.example.todoapp.data.data_source.network.responses.PatchListApiRequest
import com.example.todoapp.data.data_source.network.responses.PostItemApiRequest
import com.example.todoapp.data.data_source.network.responses.TodoItemResponse
import com.example.todoapp.data.data_source.room.ToDoItemEntity
import com.example.todoapp.data.data_source.room.TodoListDatabase
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.ioc.SharedPreferencesHelper
import com.example.todoapp.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow


class ItemsRepository(
    db: TodoListDatabase,
    private val sharedPreferencesHelper: SharedPreferencesHelper
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


    private val service by lazy {
        Common.retrofitService
    }


    suspend fun getNetworkTasks(): UiState<List<TodoItem>>{
        try {
            val getResponse = service.getList()

            val revision = getResponse.revision
            val networkList = getResponse.list
            val currentList = dao.getAll().map { TodoItemResponse.fromItem(it.toItem()) }
            val mergedMap = HashMap<String, TodoItemResponse>()

            for (item in currentList) {
                mergedMap[item.id] = item
            }
            for (item in networkList) {
                if (mergedMap.containsKey(item.id)) {
                    val item1 = mergedMap[item.id]
                    if (item.dateChanged > item1!!.dateChanged) {
                        mergedMap[item.id] = item
                    } else {
                        mergedMap[item.id] = item1
                    }
                } else if (revision != sharedPreferencesHelper.getLastRevision()) {
                    mergedMap[item.id] = item
                }
            }
            sharedPreferencesHelper.putRevision(revision)
            val mergedList = mergedMap.values.toList()
            val patchResponse = service.updateList(
                sharedPreferencesHelper.getLastRevision(),
                PatchListApiRequest(mergedList)
            )
            updateRoom(mergedList)
            return UiState.Success(patchResponse.list.map { it.toItem() })
        }catch (exception:Exception){
            return UiState.Error("Error merging data")
        }
    }

    private suspend fun updateRoom(mergedList: List<TodoItemResponse>) {
        dao.addList(mergedList.map { ToDoItemEntity.fromItem(it.toItem()) })
    }

    suspend fun postNetworkItem(
        newItem: TodoItem
    ) {
        try {
            val postResponse = service.postElement(
                sharedPreferencesHelper.getLastRevision(),
                PostItemApiRequest(TodoItemResponse.fromItem(newItem))
            )

            if (postResponse.isSuccessful) {
                val responseBody = postResponse.body()
                if (responseBody != null) {
                    sharedPreferencesHelper.putRevision(responseBody.revision)
                }
            } else {
                postResponse.errorBody()?.close()
            }
        } catch (err: Exception) {
            Log.d("1", err.message.toString())
        }
    }

    suspend fun deleteNetworkItem(
        id: String
    ) {
        try {
            val postResponse = service.deleteElement(id, sharedPreferencesHelper.getLastRevision())

            if (postResponse.isSuccessful) {
                val responseBody = postResponse.body()
                if (responseBody != null) {
                    sharedPreferencesHelper.putRevision(responseBody.revision)
                }
            } else {
                postResponse.errorBody()?.close()
            }
        } catch (err: Exception) {
            Log.d("1", err.message.toString())
        }
    }

    suspend fun updateNetworkItem(
        item: TodoItem
    ) {
        try {
            val updateItemResponse = service.updateElement(
                item.id, sharedPreferencesHelper.getLastRevision(), PostItemApiRequest(
                    TodoItemResponse.fromItem(item)
                )
            )
            if (updateItemResponse.isSuccessful) {
                val body = updateItemResponse.body()
                if (body != null) {
                    sharedPreferencesHelper.putRevision(body.revision)
                }
            } else {
                updateItemResponse.errorBody()?.close()
            }
        } catch (err: Exception) {
            Log.d("1", "err")
        }
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }


}