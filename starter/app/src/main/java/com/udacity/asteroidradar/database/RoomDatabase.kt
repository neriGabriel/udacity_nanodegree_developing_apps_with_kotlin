package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Room


private lateinit var  sINSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if(!::sINSTANCE.isInitialized) {
            sINSTANCE = Room.databaseBuilder(context.applicationContext,
            AsteroidDatabase::class.java,
            "asteroids").build()
        }
    }
    return sINSTANCE
}