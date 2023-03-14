package com.udacity.asteroidradar.asteroidlist.data.model.mapper

import com.udacity.asteroidradar.asteroid.domain.model.Asteroid
import com.udacity.asteroidradar.common.database.DatabaseAsteroid
import com.udacity.asteroidradar.asteroid.domain.model.mapper.toDatabaseModel

fun List<Asteroid>.toDatabaseModel(): List<DatabaseAsteroid> =
    List(size = this.size) {
        this[it].toDatabaseModel()
    }
