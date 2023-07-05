package com.example.todoapp.data.data_source.network

import android.util.Log
import com.example.todoapp.data.data_source.network.dto.requests.PatchListApiRequest
import com.example.todoapp.data.data_source.network.dto.requests.PostItemApiRequest
import com.example.todoapp.data.data_source.network.dto.responses.TodoItemResponse
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.ioc.SharedPreferencesHelper
import com.example.todoapp.domain.model.DataState
import com.example.todoapp.utils.Common
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkSource(
    private val sharedPreferencesHelper: SharedPreferencesHelper
) {
    private val service by lazy {
        Common.retrofitService
    }


    suspend fun getMergedList(currentList: List<TodoItemResponse>): Flow<DataState<List<TodoItem>>> =
        flow {
            emit(DataState.Initial)
            try {
                val getResponse = service.getList()

                val revision = getResponse.revision
                val networkList = getResponse.list
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
                sharedPreferencesHelper.putRevision(patchResponse.revision)

                emit(DataState.Result(patchResponse.list.map { it.toItem() }))
            } catch (exception: Exception) {
                emit(DataState.Exception(exception))
            }
        }

    suspend fun postElement(item: TodoItem) {
        try {
            val postResponse = service.postElement(
                sharedPreferencesHelper.getLastRevision(),
                PostItemApiRequest(TodoItemResponse.fromItem(item))
            )

            sharedPreferencesHelper.putRevision(postResponse.revision)
        } catch (err: Exception) {
            Log.d("1", err.message.toString())
        }
    }

    suspend fun deleteElement(id: String) {
        try {
            val deleteResponse =
                service.deleteElement(id, sharedPreferencesHelper.getLastRevision())
            sharedPreferencesHelper.putRevision(deleteResponse.revision)
        } catch (err: Exception) {
            Log.d("1", err.message.toString())
        }
    }

    suspend fun updateElement(item: TodoItem) {
        try {
            val updateItemResponse = service.updateElement(
                item.id, sharedPreferencesHelper.getLastRevision(), PostItemApiRequest(
                    TodoItemResponse.fromItem(item)
                )
            )
            sharedPreferencesHelper.putRevision(updateItemResponse.revision)
        } catch (err: Exception) {
            Log.d("1", "err")
        }
    }
}