package com.udacity.asteroidradar.main.view

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.common.database.getDatabase
import com.udacity.asteroidradar.common.network.NasaApi
import com.udacity.asteroidradar.pictureoftheday.data.repository.PictureOfTheDayRepositoryImpl
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val pictureOfTheDayRepository = PictureOfTheDayRepositoryImpl(database, NasaApi.retrofitService)

    init {
        viewModelScope.launch {
            pictureOfTheDayRepository.getPictureOfTheDay()
        }
    }

    val pictureOfTheDay = pictureOfTheDayRepository.pictureOfTheDay

}