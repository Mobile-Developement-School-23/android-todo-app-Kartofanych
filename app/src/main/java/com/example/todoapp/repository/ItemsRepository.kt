package com.example.todoapp.repository

import android.util.Log
import com.example.todoapp.data_source.network.Common
import com.example.todoapp.data_source.network.NetworkAccess
import com.example.todoapp.data_source.network.responses.PatchListApiRequest
import com.example.todoapp.data_source.network.responses.PostItemApiRequest
import com.example.todoapp.data_source.network.responses.PostItemApiResponse
import com.example.todoapp.data_source.network.responses.TodoItemResponse
import com.example.todoapp.data_source.room.ToDoItemEntity
import com.example.todoapp.data_source.room.TodoItem
import com.example.todoapp.data_source.room.TodoListDatabase
import com.example.todoapp.shared_preferences.SharedPreferencesHelper
import com.example.todoapp.utils.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class ItemsRepository(
    db: TodoListDatabase,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) {

    private val dao = db.listDao

    fun getAllData(): Flow<List<TodoItem>> =
        dao.getAllFlow().map { list -> list.map { it.toItem() } }

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

    suspend fun changeDone(id: String, done: Boolean) {
        return dao.updateDone(id, done, System.currentTimeMillis())
    }


    private val service = Common.retrofitService

    suspend fun getNetworkData(): LoadingState<Any> {
        val networkListResponse = service.getList()

        if (networkListResponse.isSuccessful) {
            val body = networkListResponse.body()
            if (body != null) {
                val revision = body.revision
                val networkList = body.list
                val currentList = dao.getAll().map { TodoItemResponse.fromItem(it.toItem()) }
                val mergedList = HashMap<String, TodoItemResponse>()

                for (item in currentList) {
                    mergedList[item.id] = item
                }
                for (item in networkList) {
                    if (mergedList.containsKey(item.id)) {
                        val item1 = mergedList[item.id]
                        if (item.dateChanged > item1!!.dateChanged) {
                            mergedList[item.id] = item
                        } else {
                            mergedList[item.id] = item1
                        }
                    } else if (revision != sharedPreferencesHelper.getLastRevision()) {
                        mergedList[item.id] = item
                    }
                }

                return updateNetworkList(mergedList.values.toList())
            }
        }
        return LoadingState.Error("Some error occurred, try later")
    }

    private suspend fun updateNetworkList(mergedList: List<TodoItemResponse>): LoadingState<Any> {

        val updateResponse = service.updateList(
            sharedPreferencesHelper.getLastRevision(),
            PatchListApiRequest(mergedList)
        )


        if (updateResponse.isSuccessful) {
            val responseBody = updateResponse.body()
            if (responseBody != null) {
                sharedPreferencesHelper.putRevision(responseBody.revision)
                updateRoom(responseBody.list)
                return LoadingState.Success(responseBody.list)
            }
        }
        return LoadingState.Error("Some error occurred, try later")
    }

    private suspend fun updateRoom(mergedList: List<TodoItemResponse>) {
        dao.addList(mergedList.map { ToDoItemEntity.fromItem(it.toItem()) })
    }

    suspend fun postNetworkItem(
        newItem: TodoItem
    ): NetworkAccess<PostItemApiResponse> {
        val postResponse = service.postElement(
            sharedPreferencesHelper.getLastRevision(),
            PostItemApiRequest(TodoItemResponse.fromItem(newItem))
        )

        if (postResponse.isSuccessful) {
            val responseBody = postResponse.body()
            if (responseBody != null) {
                sharedPreferencesHelper.putRevision(responseBody.revision)
                return NetworkAccess.Success(responseBody)
            }
        }
        return NetworkAccess.Error(postResponse)
    }

    suspend fun deleteNetworkItem(
        id: String
    ): NetworkAccess<PostItemApiResponse> {
        val postResponse = service.deleteElement(id, sharedPreferencesHelper.getLastRevision())

        if (postResponse.isSuccessful) {
            val responseBody = postResponse.body()
            if (responseBody != null) {
                sharedPreferencesHelper.putRevision(responseBody.revision)
                return NetworkAccess.Success(responseBody)
            }
        }
        return NetworkAccess.Error(postResponse)
    }

    suspend fun updateNetworkItem(
        item: TodoItem
    ) = withContext(Dispatchers.IO) {

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
        }
    }


}