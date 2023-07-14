package com.example.todoapp.utils.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.example.todoapp.App
import com.example.todoapp.R
import com.example.todoapp.domain.repository.Repository
import com.example.todoapp.utils.toName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class NotificationsReceiver : BroadcastReceiver() {

    @Inject
    lateinit var scheduler: NotificationsSchedulerImpl
    @Inject
    lateinit var coroutineScope: CoroutineScope
    @Inject
    lateinit var repository: Repository
    private companion object {
        const val CHANNEL_ID = "deadlines"
        const val CHANNEL_NAME = "Deadline notification"
    }


    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as App).appComponent.inject(this)
        try {

            val id:String = intent.getStringExtra("id")!!

            coroutineScope.launch(Dispatchers.IO) {
                val item = repository.getItem(id)

                val manager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(
                    NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                )

                val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_check_24dp)
                    .setContentTitle("Остался час! Важность ${item.importance.toName()}")
                    .setContentText(item.text)
                    .setAutoCancel(true)
                    .setContentIntent(deepLinkIntent(context, item.id))
                    .addAction(
                        NotificationCompat.Action(
                            R.drawable.ic_delay, "Отложить на день",
                            postponeIntent(context, item.id)
                        )
                    )
                    .build()
                scheduler.cancel(item.id)
                manager.notify(item.id.hashCode(), notification)
            }
        } catch (err: Exception) {
            Log.d("exception", err.stackTraceToString())
        }
    }

    private fun deepLinkIntent(context: Context, id: String): PendingIntent =
        NavDeepLinkBuilder(context)
            .setGraph(R.navigation.tasks_navigation)
            .setDestination(R.id.newTaskComposeFragment, bundleOf("id" to id))
            .createPendingIntent()

    private fun postponeIntent(context: Context, id: String): PendingIntent =
        PendingIntent.getBroadcast(
            context, id.hashCode(),
            Intent(context, NotificationPostponeReceiver::class.java).apply {
                putExtra("id", id)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
}