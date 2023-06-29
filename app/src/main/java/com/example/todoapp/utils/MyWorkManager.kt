package com.example.todoapp.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todoapp.repository.ItemsRepository
import kotlinx.coroutines.runBlocking

class MyWorkManager(
    context:Context,
    workerParameters: WorkerParameters,
    ): Worker(context,workerParameters){

    private val repository: ItemsRepository by localeLazy()

    override fun doWork(): Result {
        return when(mergeData()){
            is LoadingState.Success -> Result.success()
            else -> {
                Result.failure()
            }
        }
    }

    private fun mergeData() = runBlocking{
        return@runBlocking repository.getNetworkData()
    }

}