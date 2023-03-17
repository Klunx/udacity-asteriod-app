package com.udacity.asteroidradar.main.view

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.asteroid.domain.model.Asteroid
import com.udacity.asteroidradar.asteroidlist.data.repository.AsteroidListFilter
import com.udacity.asteroidradar.asteroidlist.data.repository.AsteroidListRepositoryImpl
import com.udacity.asteroidradar.common.database.getDatabase
import com.udacity.asteroidradar.common.network.NasaApi
import com.udacity.asteroidradar.pictureoftheday.data.repository.PictureOfTheDayRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val nasaApiService = NasaApi.retrofitService

    private var currentJob: Job? = null

    private val pictureOfTheDayRepository = PictureOfTheDayRepositoryImpl(database, nasaApiService)
    private val asteroidListRepository = AsteroidListRepositoryImpl(database, nasaApiService)

    val pictureOfTheDay = pictureOfTheDayRepository.pictureOfTheDay

    private var _listOfAsteroids = MutableLiveData<List<Asteroid>>()
    val listOfAsteroids: LiveData<List<Asteroid>>
        get() = _listOfAsteroids

    private val _navigateToAsteroid = MutableLiveData<Asteroid>()
    val navigateToAsteroid: LiveData<Asteroid>
        get() = _navigateToAsteroid

    init {
        viewModelScope.launch {
            pictureOfTheDayRepository.getPictureOfTheDay()
        }
        initializeAsteroidList()
    }

    private fun initializeAsteroidList() {
        currentJob?.cancel()
        currentJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                asteroidListRepository.getListOfAsteroids()
                onQueryChanged(AsteroidListFilter.SAVED)
            } catch (e: IOException) {

            }
        }
    }

    private fun onQueryChanged(filter: AsteroidListFilter) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                // Using postValue as per suggested below
                // https://stackoverflow.com/questions/53304347/mutablelivedata-cannot-invoke-setvalue-on-a-background-thread-from-coroutine
                _listOfAsteroids.postValue(asteroidListRepository.getFilteredList(filter))
            } catch (e: IOException) {
                _listOfAsteroids.postValue(listOf())
            }
        }
    }

    fun updateAsteroidList(filter: AsteroidListFilter) {
        onQueryChanged(filter)
    }

    fun navigateToAsteroidDetails(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

    fun navigateToAsteroidComplete() {
        _navigateToAsteroid.value = null
    }
}