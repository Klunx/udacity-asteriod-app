package com.udacity.asteroidradar.pictureoftheday.domain.repository

interface PictureOfTheDayRepository {
    suspend fun getPictureOfTheDay()
}