package com.udacity.asteroidradar.asteroid.data.model.mapper

import com.udacity.asteroidradar.asteroid.data.model.DataAsteroid
import com.udacity.asteroidradar.asteroid.domain.model.Asteroid

fun DataAsteroid.toDomainModel(): Asteroid =
    Asteroid(
        id = id,
        codename = codename,
        closeApproachDate = closeApproachDate,
        absoluteMagnitude = absoluteMagnitude,
        estimatedDiameter = estimatedDiameter,
        relativeVelocity = relativeVelocity,
        distanceFromEarth = distanceFromEarth,
        isPotentiallyHazardous = isPotentiallyHazardous
    )
