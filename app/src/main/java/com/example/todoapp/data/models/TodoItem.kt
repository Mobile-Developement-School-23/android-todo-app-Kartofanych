package com.example.todoapp.data.models

import android.util.Log
import com.google.gson.Gson
import java.sql.Date
import java.text.SimpleDateFormat

data class TodoItem(
    var id : String,
    var text : String,
    var importance : Importance,
    var deadline : Long?,
    var done : Boolean,
    var dateCreation : Long,
    var dateChanged : String?
) {


    constructor():this(id = "-1", text = "", importance = Importance.REGULAR,
        deadline = null, done = false, dateCreation = 1000, dateChanged = null)


    
    fun deadlineToString():String?{
        if(deadline != null) {
            val date = Date(deadline!!)
            val dateFormat = SimpleDateFormat("dd MMMM YYYY")
            Log.d("time", System.currentTimeMillis().toString())
            return dateFormat.format(date)
        }
        return null
    }

    fun setDate(millis:Long){
        deadline = millis
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
