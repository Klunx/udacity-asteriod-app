package com.udacity.asteroidradar.asteroidlist.domain.mapper

import com.udacity.asteroidradar.asteroid.data.model.DataAsteroid
import com.udacity.asteroidradar.asteroid.data.model.mapper.toDomainModel
import com.udacity.asteroidradar.asteroid.domain.model.Asteroid

fun List<DataAsteroid>.toDomainModel(): List<Asteroid> =
    List(size = this.size) {
        this[it].toDomainModel()
    }