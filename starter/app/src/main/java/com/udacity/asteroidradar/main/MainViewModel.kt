package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Filters
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidRetrofit
import com.udacity.asteroidradar.api.AsteroidService
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)

    private val _asteroidList = MutableLiveData(Filters.ALL)
    val asteroidList = Transformations.switchMap(_asteroidList) {
        when(it!!) {
            Filters.TODAY -> repository.dailyAsteroids
            Filters.WEEK -> repository.weeklyAsteroids
            else -> repository.asteroids
        }
    }

    private val _currentPicture = MutableLiveData<PictureOfDay>()
    val currentPicture: LiveData<PictureOfDay>
            get() = _currentPicture

    private val _navigateToDetails = MutableLiveData<Asteroid>()
    val navigateToDetails: LiveData<Asteroid>
        get() = _navigateToDetails

    init {
        refreshAsteroids()
        refreshCurrentPicture()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }

    private fun refreshCurrentPicture() {
        viewModelScope.launch {
            try {
                _currentPicture.postValue(AsteroidRetrofit.retrofitService.getPictureOfDay())
            } catch( error: Exception) {
                Log.e("MainViewModel", "error: "+error.message)
            }
        }
    }

    private fun refreshAsteroids() {
        viewModelScope.launch {
            repository.getAllAsteroids()
        }
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetails.value = asteroid
    }

    fun onAsteroidNavigated() {
        _navigateToDetails.value = null
    }

    fun onFilterChanged(filter: Filters) {
        _asteroidList.postValue(filter)
    }

}