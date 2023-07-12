package com.example.todoapp.di.modules

import com.example.todoapp.utils.notifications.NotificationsScheduler
import com.example.todoapp.utils.notifications.NotificationsSchedulerImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable

@Module
interface NotificationModule {

    @Reusable
    @Binds
    fun bindNotificationModule(notificationsSchedulerImpl: NotificationsSchedulerImpl): NotificationsScheduler
}