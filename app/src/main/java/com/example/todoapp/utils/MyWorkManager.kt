package com.example.todoapp.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todoapp.data.repository.RepositoryImpl
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MyWorkManager(
    context: Context,
    workerParameters: WorkerParameters,
) : Worker(context, workerParameters) {


    override fun doWork(): Result {
        return Result.success()
    }

    /*private fun mergeData() = runBlocking {
        return@runBlocking repository.getNetworkTasks()
    }*/

}