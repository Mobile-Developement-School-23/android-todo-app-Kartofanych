package com.example.todoapp.room

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import java.sql.Date
import java.text.SimpleDateFormat

@Entity(tableName = "todoList")
data class TodoItem(
    @PrimaryKey var id : String,
    var text : String,
    var importance : Importance,
    var deadline : Date?,
    var done : Boolean,
    var dateCreation : Long,
    var dateChanged : String?
) {


    constructor():this(id = "-1", text = "", importance = Importance.REGULAR,
        deadline = null, done = false, dateCreation = 1000, dateChanged = null)

    fun deadlineToString():String?{
        if(deadline != null) {
            val dateFormat = SimpleDateFormat("dd MMMM YYYY")
            return dateFormat.format(deadline!!)
        }
        return null
    }

    override fun toString(): String {
        val gson = Gson()
        return gson.toJson(this)
    }

}

enum class Importance{
    LOW,
    REGULAR,
    URGENT
}
