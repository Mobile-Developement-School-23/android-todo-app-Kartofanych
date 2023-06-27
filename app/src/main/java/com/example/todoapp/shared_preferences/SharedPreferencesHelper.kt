package com.example.todoapp.shared_preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

class SharedPreferencesHelper(context: Context){
    private val sharedPreferences: SharedPreferences
    private val editor:Editor

    init {
        sharedPreferences = context.getSharedPreferences("states", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun putRevision(revision:Int){
        editor.putInt("REVISION", revision)
        editor.apply()
    }
    fun getLastRevision():Int = sharedPreferences.getInt("REVISION", 1)


}