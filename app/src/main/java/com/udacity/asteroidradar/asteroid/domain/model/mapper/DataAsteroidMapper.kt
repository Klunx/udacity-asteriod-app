package com.udacity.asteroidradar.asteroid.domain.model.mapper

import com.udacity.asteroidradar.asteroid.domain.model.Asteroid
import com.udacity.asteroidradar.common.database.DatabaseAsteroid

fun Asteroid.toDatabaseModel(): DatabaseAsteroid =
    DatabaseAsteroid(
        id = id,
        codename = codename,
        closeApproachDate = closeApproachDate,
        absoluteMagnitude = absoluteMagnitude,
        estimatedDiameter = estimatedDiameter,
        relativeVelocity = relativeVelocity,
        distanceFromEarth = distanceFromEarth,
        isPotentiallyHazardous = isPotentiallyHazardous
    )
