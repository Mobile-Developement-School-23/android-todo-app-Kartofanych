package com.example.todoapp.network.responces

import com.example.todoapp.room.Importance
import com.example.todoapp.room.ToDoItemEntity
import com.example.todoapp.room.TodoItem
import com.google.gson.annotations.SerializedName
import java.sql.Date

data class ListApiResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("list")
    val list: List<ItemResponse>,


    @SerializedName("revision")
    val revision: Int

)

data class ItemResponse(
    @SerializedName("id")
    var id: String,
    @SerializedName("text")
    var text: String,
    @SerializedName("importance")
    var importance: String,
    @SerializedName("deadline")
    var deadline: Long?,
    @SerializedName("done")
    var done: Boolean,
    @SerializedName("created_at")
    var dateCreation: Long,
    @SerializedName("changed_at")
    var dateChanged: Long?
) {

    fun toItem(): TodoItem = TodoItem(
        id,
        text,
        when (importance) {
            "low" -> Importance.LOW
            "basic" -> Importance.REGULAR
            "important" -> Importance.URGENT
            else -> {
                Importance.REGULAR
            }
        },
        deadline?.let { Date(it) },
        done,
        Date(dateCreation),
        dateChanged?.let { Date(it) }
    )

    companion object {
        fun fromItem(toDoItem: TodoItem): ToDoItemEntity {
            return ToDoItemEntity(
                id = toDoItem.id,
                description = toDoItem.text,
                importance = toDoItem.importance,
                deadline = toDoItem.deadline?.time,
                done = toDoItem.done,
                createdAt = toDoItem.dateCreation.time,
                changedAt = toDoItem.dateChanged?.time
            )
        }
    }
}