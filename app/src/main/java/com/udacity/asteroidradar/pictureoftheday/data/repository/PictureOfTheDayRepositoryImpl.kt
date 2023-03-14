package com.udacity.asteroidradar.pictureoftheday.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.common.database.NasaDatabase
import com.udacity.asteroidradar.common.database.mapper.toDatabaseModel
import com.udacity.asteroidradar.common.database.mapper.toDomainModel
import com.udacity.asteroidradar.common.network.NasaApiService
import com.udacity.asteroidradar.pictureoftheday.data.model.mapper.toDomainModel
import com.udacity.asteroidradar.pictureoftheday.domain.model.PictureOfDay
import com.udacity.asteroidradar.pictureoftheday.domain.repository.PictureOfTheDayRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PictureOfTheDayRepositoryImpl(
    private val database: NasaDatabase,
    private val nasaApiService: NasaApiService
) : PictureOfTheDayRepository {

    private val apiKey = "njunRLyfKbrdac47JO9rJnf6boN1S7dWF9BQXjDq"
    val pictureOfTheDay: LiveData<PictureOfDay> = Transformations.map(database.nasaDao.getPictureOfTheDay()) { databasePictureOfTheDay ->
        databasePictureOfTheDay?.let {
            it.toDomainModel()
        }
    }

    override suspend fun getPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            val response = nasaApiService.getPictureOfTheDay(apiKey).toDomainModel()
            database.nasaDao.clearPictureOfTheDay()
            database.nasaDao.insertPictureOfTheDay(response.toDatabaseModel())
        }
    }
}