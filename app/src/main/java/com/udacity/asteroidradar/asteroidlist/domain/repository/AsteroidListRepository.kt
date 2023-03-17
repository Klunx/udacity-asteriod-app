package com.udacity.asteroidradar.asteroidlist.domain.repository

interface AsteroidListRepository {

    suspend fun getListOfAsteroids()
}