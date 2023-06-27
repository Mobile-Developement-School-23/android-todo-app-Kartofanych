package com.example.todoapp.repository

import com.example.todoapp.network.Common
import com.example.todoapp.network.NetworkAccess
import com.example.todoapp.network.responses.GetListApiResponse
import com.example.todoapp.network.responses.PatchListApiRequest
import com.example.todoapp.network.responses.PostItemApiRequest
import com.example.todoapp.network.responses.PostItemApiResponse
import com.example.todoapp.network.responses.TodoItemResponse
import com.example.todoapp.room.ToDoItemEntity
import com.example.todoapp.room.TodoItem
import com.example.todoapp.room.TodoListDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class ItemsRepository(
    db: TodoListDatabase
) {

    private val dao = db.listDao

    fun getAllData(): Flow<List<TodoItem>> =
        dao.getAllFlow().map { list -> list.map { it.toItem() } }

    fun getItem(itemId: String): Flow<TodoItem> = dao.getItem(itemId).map { it.toItem() }

    suspend fun addItem(todoItem: TodoItem) {
        return dao.add(ToDoItemEntity.fromItem(todoItem))
    }

    suspend fun deleteItem(id: String) {
        return dao.delete(id)
    }

    suspend fun changeItem(todoItem: TodoItem) {
        return dao.update(ToDoItemEntity.fromItem(todoItem))
    }

    suspend fun changeDone(id: String, done: Boolean) {
        return dao.updateDone(id, done, System.currentTimeMillis())
    }


    private val service = Common.retrofitService

    suspend fun getNetworkData(lastRevision: Int): NetworkAccess<GetListApiResponse> {

        val updateResponse = service.updateList(
            lastRevision,
            PatchListApiRequest(dao.getAll().map { TodoItemResponse.fromItem(it.toItem()) })
        )


        if (updateResponse.isSuccessful) {
            val responseBody = updateResponse.body()
            if (responseBody == null) {
                return NetworkAccess.Error(updateResponse)
            } else {
                updateRoom(responseBody)
                return NetworkAccess.Success(responseBody)
            }
        }
        return NetworkAccess.Error(updateResponse)
    }

    private suspend fun updateRoom(response: GetListApiResponse) {
        val list = response.list.map { it.toItem() }
        dao.addList(list.map { ToDoItemEntity.fromItem(it) })
    }

    suspend fun postItem(lastRevision: Int, newItem: TodoItem): NetworkAccess<PostItemApiResponse> {
        val postResponse = service.postElement(
            lastRevision,
            PostItemApiRequest(TodoItemResponse.fromItem(newItem))
        )

        if (postResponse.isSuccessful) {
            val responseBody = postResponse.body()
            if (responseBody != null) {
                return NetworkAccess.Success(responseBody)
            }
        }
        return NetworkAccess.Error(postResponse)
    }


}