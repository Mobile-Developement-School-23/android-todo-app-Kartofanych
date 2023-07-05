package com.example.todoapp.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todoapp.data.repository.ItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class MyWorkManager(
    context: Context,
    workerParameters: WorkerParameters,
) : Worker(context, workerParameters) {

    private val repository: ItemsRepository by localeLazy()

    override fun doWork(): Result {
        mergeData()
        return Result.success()
    }

    private fun mergeData() = runBlocking {
        return@runBlocking repository.getNetworkTasks()
    }

}