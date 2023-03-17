package com.udacity.asteroidradar.pictureoftheday.domain.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.pictureoftheday.domain.model.PictureOfDay

interface PictureOfTheDayRepository {
    val pictureOfTheDay: LiveData<PictureOfDay>

    suspend fun getPictureOfTheDay()
}