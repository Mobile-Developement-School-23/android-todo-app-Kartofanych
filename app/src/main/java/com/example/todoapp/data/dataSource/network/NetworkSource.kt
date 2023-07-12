package com.example.todoapp.data.dataSource.network

import android.util.Log
import com.example.todoapp.data.dataSource.network.api.RetrofitService
import com.example.todoapp.data.dataSource.network.dto.requests.PatchListApiRequest
import com.example.todoapp.data.dataSource.network.dto.requests.PostItemApiRequest
import com.example.todoapp.data.dataSource.network.dto.responses.TodoItemResponse
import com.example.todoapp.domain.model.DataState
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.utils.SharedPreferencesHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class NetworkSource @Inject constructor(
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val service: RetrofitService
) {

    suspend fun getMergedList(currentList: List<TodoItemResponse>): Flow<DataState<List<TodoItem>>> =
        flow {
            emit(DataState.Initial)
            try {
                val getResponse = service.getList(
                    sharedPreferencesHelper.getTokenForResponse()
                )

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
                    sharedPreferencesHelper.getTokenForResponse(),
                    sharedPreferencesHelper.getLastRevision(),
                    PatchListApiRequest(mergedList)
                )
                sharedPreferencesHelper.putRevision(patchResponse.revision)

                emit(DataState.Result(patchResponse.list.map { it.toItem() }))
            } catch (exception: SocketTimeoutException) {
                emit(DataState.Exception(exception))
            } catch (exception: UnknownHostException){
                emit(DataState.Exception(exception))
            } catch (exception: HttpException){
                emit(DataState.Exception(exception))
            } catch (other:Exception){
                emit(DataState.Exception(other))
            }
        }

    suspend fun postElement(item: TodoItem) {
        try {
            val postResponse = service.postElement(
                sharedPreferencesHelper.getTokenForResponse(),
                sharedPreferencesHelper.getLastRevision(),
                PostItemApiRequest(TodoItemResponse.fromItem(item))
            )
            sharedPreferencesHelper.putRevision(postResponse.revision)
        }  catch (exception: SocketTimeoutException) {
            Log.d("1", exception.toString())
        } catch (exception: UnknownHostException){
            Log.d("1", exception.toString())
        } catch (exception: HttpException){
            Log.d("1", exception.toString())
        } catch (other:Exception){
            Log.d("1", other.toString())
        }
    }

    suspend fun deleteElement(id: String) {
        try {
            val deleteResponse =
                service.deleteElement(
                    sharedPreferencesHelper.getTokenForResponse(),
                    id,
                    sharedPreferencesHelper.getLastRevision()
                )
            sharedPreferencesHelper.putRevision(deleteResponse.revision)
        }  catch (exception: SocketTimeoutException) {
            Log.d("1", exception.toString())
        } catch (exception: UnknownHostException){
            Log.d("1", exception.toString())
        } catch (exception: HttpException){
            Log.d("1", exception.toString())
        } catch (other:Exception){
            Log.d("1", other.toString())
        }
    }

    suspend fun updateElement(item: TodoItem) {
        try {
            val updateItemResponse = service.updateElement(
                sharedPreferencesHelper.getTokenForResponse(),
                item.id,
                sharedPreferencesHelper.getLastRevision(),
                PostItemApiRequest(
                    TodoItemResponse.fromItem(item)
                )
            )
            sharedPreferencesHelper.putRevision(updateItemResponse.revision)
        }  catch (exception: SocketTimeoutException) {
            Log.d("1", exception.toString())
        } catch (exception: UnknownHostException){
            Log.d("1", exception.toString())
        } catch (exception: HttpException){
            Log.d("1", exception.toString())
        } catch (other:Exception){
            Log.d("1", other.toString())
        }
    }
}
