package com.example.todoapp.utils.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.todoapp.domain.model.TodoItem
import com.example.todoapp.domain.sheduler.NotificationsScheduler
import com.example.todoapp.utils.Constants.HOUR_IN_MILLIS
import com.example.todoapp.utils.SharedPreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class NotificationsSchedulerImpl @Inject constructor(
    private val context: Context,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : NotificationsScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: TodoItem) {
        if (item.deadline != null
            && item.deadline!!.time >= System.currentTimeMillis()+HOUR_IN_MILLIS
            && !item.done
            && sharedPreferencesHelper.getNotificationPermission() == "true") {
            val intent = Intent(context, NotificationsReceiver::class.java).apply {
                putExtra("id", item.id)
            }
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                item.deadline!!.time-HOUR_IN_MILLIS,
                //System.currentTimeMillis()+10000,
                PendingIntent.getBroadcast(
                    context,
                    item.id.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            sharedPreferencesHelper.addNotification(item.id)

        }else{
            cancel(item.id)
        }
    }

    override fun cancel(id:String) {
        try {
            alarmManager.cancel(
                PendingIntent.getBroadcast(
                    context,
                    id.hashCode(),
                    Intent(context, NotificationsReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            sharedPreferencesHelper.removeNotification(id)
        }catch (err:Exception){
            Log.d("1", err.message.toString())
        }
    }


    override fun cancelAll() {
        Log.d("1", sharedPreferencesHelper.getNotificationsId())
        val notifications = sharedPreferencesHelper.getNotificationsId().split(" ")

        for(notification in notifications){
            cancel(notification)
        }
    }
}