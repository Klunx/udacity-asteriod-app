package com.udacity.asteroidradar.asteroidlist.domain.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.asteroid.domain.model.Asteroid

interface AsteroidListRepository {

    val listOfAsteroids: LiveData<List<Asteroid>>

    suspend fun getListOfAsteroids()
    suspend fun getNextSevenDaysAsteroids()
}