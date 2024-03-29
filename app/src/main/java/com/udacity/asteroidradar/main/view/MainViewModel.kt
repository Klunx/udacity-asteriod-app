package com.udacity.asteroidradar.main.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.asteroid.domain.model.Asteroid
import com.udacity.asteroidradar.asteroidlist.data.repository.AsteroidListFilter
import com.udacity.asteroidradar.asteroidlist.data.repository.AsteroidListRepositoryImpl
import com.udacity.asteroidradar.common.database.getDatabase
import com.udacity.asteroidradar.common.network.NasaApi
import com.udacity.asteroidradar.pictureoftheday.data.repository.PictureOfTheDayRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

enum class NasaApiStatus { LOADING, ERROR, DONE }
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val nasaApiService = NasaApi.retrofitService

    private var currentJob: Job? = null

    private val pictureOfTheDayRepository = PictureOfTheDayRepositoryImpl(database, nasaApiService)
    private val asteroidListRepository = AsteroidListRepositoryImpl(database, nasaApiService)

    val pictureOfTheDay = pictureOfTheDayRepository.pictureOfTheDay
    val listOfAsteroids = asteroidListRepository.listOfAsteroids

    private val _navigateToAsteroid = MutableLiveData<Asteroid>()
    val navigateToAsteroid: LiveData<Asteroid>
        get() = _navigateToAsteroid

    private val _status = MutableLiveData<NasaApiStatus>()

    // The external immutable LiveData for the request status String
    val status: LiveData<NasaApiStatus>
        get() = _status

    init {
        _status.value = NasaApiStatus.LOADING
        initializePictureOfTheDay()
        initializeAsteroidList()
    }

    private fun initializePictureOfTheDay() {
        viewModelScope.launch {
            pictureOfTheDayRepository.getPictureOfTheDay()
        }
    }

    private fun initializeAsteroidList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                asteroidListRepository.getListOfAsteroids()
                delay(5000)
                _status.postValue(NasaApiStatus.DONE)
            } catch (e: IOException) {
                _status.postValue(NasaApiStatus.DONE)
            }
        }
    }

    private fun onQueryChanged(filter: AsteroidListFilter) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                // Using postValue as per suggested below
                // https://stackoverflow.com/questions/53304347/mutablelivedata-cannot-invoke-setvalue-on-a-background-thread-from-coroutine
                asteroidListRepository.getFilteredList(filter)
                delay(1000)
                _status.postValue(NasaApiStatus.DONE)
            } catch (e: IOException) {
                _status.postValue(NasaApiStatus.DONE)
            }
        }
    }

    fun updateAsteroidList(filter: AsteroidListFilter) {
        _status.value = NasaApiStatus.LOADING
        onQueryChanged(filter)
    }

    fun navigateToAsteroidDetails(asteroid: Asteroid) {
        _navigateToAsteroid.value = asteroid
    }

    fun navigateToAsteroidComplete() {
        _navigateToAsteroid.value = null
    }
}