package com.example.todoapp.shared_preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.example.todoapp.data_source.network.Common
import java.util.UUID

class SharedPreferencesHelper(context: Context){
    private val sharedPreferences: SharedPreferences
    private val editor:Editor

    init {
        sharedPreferences = context.getSharedPreferences("states", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        if(!sharedPreferences.contains("UID")){
            editor.putString("UID", UUID.randomUUID().toString())
            editor.apply()
        }

        Common.updated_by = sharedPreferences.getString("UID", "-1").toString()

    }

    fun putRevision(revision:Int){
        editor.putInt("REVISION", revision)
        editor.apply()
    }
    fun getLastRevision():Int = sharedPreferences.getInt("REVISION", 1)


}