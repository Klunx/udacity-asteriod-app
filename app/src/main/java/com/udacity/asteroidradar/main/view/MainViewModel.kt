package com.udacity.asteroidradar.main.view

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.asteroidlist.data.repository.AsteroidListRepositoryImpl
import com.udacity.asteroidradar.common.database.getDatabase
import com.udacity.asteroidradar.common.network.NasaApi
import com.udacity.asteroidradar.pictureoftheday.data.repository.PictureOfTheDayRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val pictureOfTheDayRepository = PictureOfTheDayRepositoryImpl(database, NasaApi.retrofitService)
    private val asteroidListRepository = AsteroidListRepositoryImpl(database)

    init {
        viewModelScope.launch {
            pictureOfTheDayRepository.getPictureOfTheDay()
        }
        viewModelScope.launch(Dispatchers.IO) {
            asteroidListRepository.getListOfAsteroids()
        }
    }

    val pictureOfTheDay = pictureOfTheDayRepository.pictureOfTheDay
    val listOfAsteroids = asteroidListRepository.listOfAsteroids

}