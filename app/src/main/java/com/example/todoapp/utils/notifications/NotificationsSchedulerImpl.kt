package com.example.todoapp.utils.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.todoapp.domain.model.TodoItem
import java.util.UUID
import javax.inject.Inject

class NotificationsSchedulerImpl @Inject constructor(
    private val context: Context
) : NotificationsScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: TodoItem) {
        if (item.deadline != null) {
            val intent = Intent(context, NotificationsReceiver::class.java).apply {
                putExtra("item", item.toString())
            }
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                item.deadline!!.time-3600000,
                PendingIntent.getBroadcast(
                    context,
                    item.id.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }

    override fun cancel(item: TodoItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.id.hashCode(),
                Intent(context, NotificationsReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}