package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.work.*
import com.udacity.asteroidradar.worker.DataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidRadarApplication : Application() {

    private val appScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        appScope.launch{
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .apply {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        setRequiresDeviceIdle(true)
                }.build()

            val workRequest = PeriodicWorkRequestBuilder<DataWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance().enqueueUniquePeriodicWork(DataWorker.WORK_ID,
            ExistingPeriodicWorkPolicy.KEEP,
                workRequest)

            Log.d("AsteroidRadarApp", "Work enqueded!");
        }
    }
}