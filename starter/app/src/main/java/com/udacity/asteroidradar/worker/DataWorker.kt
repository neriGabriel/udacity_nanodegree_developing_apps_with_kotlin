package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import java.lang.Exception

class DataWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    companion object {
        const val WORK_ID = "DataRefreshWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.getAllAsteroids()
            Result.success()
        } catch (error: Exception) {
            Result.retry()
        }
    }

}