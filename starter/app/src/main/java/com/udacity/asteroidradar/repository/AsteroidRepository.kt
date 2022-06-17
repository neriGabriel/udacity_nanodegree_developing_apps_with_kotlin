package com.udacity.asteroidradar.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.AsteroidRetrofit
import com.udacity.asteroidradar.api.AsteroidService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AsteroidRepository(private val database: AsteroidDatabase) {

    @RequiresApi(Build.VERSION_CODES.O)
    private val startDate = LocalDateTime.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val endDate = LocalDateTime.now().plusDays(7)

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDAO.getAsteroids()) {
            it.asDomainModel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    val dailyAsteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDAO.getDailyAsteroids(
            startDate.format(DateTimeFormatter.ISO_DATE))) {
                it.asDomainModel()
        }

    @RequiresApi(Build.VERSION_CODES.O)
    val weeklyAsteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDAO.getWeeklyAsteroids(
            startDate.format(DateTimeFormatter.ISO_DATE),
            endDate.format(DateTimeFormatter.ISO_DATE))) {
                it.asDomainModel()
        }

    suspend fun getAllAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroids = AsteroidRetrofit.retrofitService.getAllAsteroids()
                val result = parseAsteroidsJsonResult(JSONObject(asteroids))
                database.asteroidDAO.insert(*result.asDatabaseModel())
            } catch (error: Exception) {
                Log.e("AsteroidRepository", "msg: "+error.message);
            }
        }
    }
}