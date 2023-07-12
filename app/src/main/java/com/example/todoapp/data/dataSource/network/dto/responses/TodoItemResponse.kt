package com.example.todoapp.data.dataSource.network.dto.responses

import com.example.todoapp.domain.model.Importance
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.utils.Constants
import com.google.gson.annotations.SerializedName
import java.sql.Date

data class TodoItemResponse(
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
    var dateChanged: Long,
    @SerializedName("last_updated_by")
    var updatedBy: String
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
        Date(dateChanged)
    )

    companion object {
        fun fromItem(toDoItem: TodoItem): TodoItemResponse {
            return TodoItemResponse(
                id = toDoItem.id,
                text = toDoItem.text,
                importance = when (toDoItem.importance) {
                    Importance.LOW -> "low"
                    Importance.REGULAR -> "basic"
                    Importance.URGENT -> "important"
                },
                deadline = toDoItem.deadline?.time,
                done = toDoItem.done,
                dateCreation = toDoItem.dateCreation.time,
                dateChanged = when (toDoItem.dateChanged) {
                    null -> toDoItem.dateCreation.time
                    else -> {
                        toDoItem.dateChanged!!.time
                    }
                },
                updatedBy = Constants.phoneId
            )
        }
    }
}
