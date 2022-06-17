package com.udacity.asteroidradar.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DatabaseAsteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase: RoomDatabase() {
    abstract  val asteroidDAO: AsteroidDAO
}