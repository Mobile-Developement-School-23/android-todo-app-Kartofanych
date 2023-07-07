package com.example.todoapp.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todoapp.App
import com.example.todoapp.data.repository.RepositoryImpl
import com.example.todoapp.domain.repository.Repository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MyWorkManager(
    private val context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {


    @Inject
    lateinit var repository:RepositoryImpl

    override fun doWork(): Result {
        (context.applicationContext as App).appComponent.inject(this)
        mergeData()
        return Result.success()
    }

    private fun mergeData() = runBlocking {
        return@runBlocking repository.getNetworkTasks()
    }

}
