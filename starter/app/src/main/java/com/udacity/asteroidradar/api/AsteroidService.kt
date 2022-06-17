package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Constants.KEY
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidService {
    @GET("neo/rest/v1/feed/")
    suspend fun getAllAsteroids(@Query("api_key") api_key: String = KEY): String

    @GET("planetary/apod")
    suspend fun getPictureOfDay(@Query("api_key") api_key: String = KEY): PictureOfDay
}

