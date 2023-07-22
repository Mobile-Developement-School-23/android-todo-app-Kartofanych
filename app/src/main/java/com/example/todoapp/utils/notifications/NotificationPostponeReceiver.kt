package com.example.todoapp.utils.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.todoapp.App
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.domain.repository.Repository
import com.example.todoapp.utils.Constants.DAY_IN_MILLIS
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

class NotificationPostponeReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var coroutineScope: CoroutineScope

    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as App).appComponent.inject(this)

        try {
            val id: String = intent.getStringExtra("id")!!
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.cancel(id.hashCode())

            coroutineScope.launch(Dispatchers.IO) {
                val item = repository.getItem(id)
                if (item.deadline != null) {
                    repository.changeItem(item.copy(deadline = Date(item.deadline!!.time + DAY_IN_MILLIS)))
                }
            }
        } catch (err: Exception) {
            Log.d("1", err.message.toString())
        }
    }
}