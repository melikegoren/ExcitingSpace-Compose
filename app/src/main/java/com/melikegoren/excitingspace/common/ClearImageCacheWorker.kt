package com.melikegoren.excitingspace.common

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import coil.Coil

class ClearImageCacheWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            // Clear Coil image cache
            Coil.imageLoader(applicationContext).memoryCache?.clear()
            Coil.imageLoader(applicationContext).diskCache?.clear()
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}
