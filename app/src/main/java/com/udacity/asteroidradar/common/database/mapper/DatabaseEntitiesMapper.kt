package com.udacity.asteroidradar.common.database.mapper

import com.udacity.asteroidradar.asteroid.domain.model.Asteroid
import com.udacity.asteroidradar.common.database.DatabaseAsteroid
import com.udacity.asteroidradar.common.database.DatabasePictureOfDay
import com.udacity.asteroidradar.pictureoftheday.domain.model.PictureOfDay

fun DatabasePictureOfDay.toDomainModel(): PictureOfDay {
    return PictureOfDay(
        mediaType = mediaType,
        title = title,
        url = url
    )
}

fun PictureOfDay.toDatabaseModel(): DatabasePictureOfDay {
    return DatabasePictureOfDay(
        mediaType = mediaType,
        title = title,
        url = url
    )
}

fun DatabaseAsteroid.toDomainModel(): Asteroid =
    Asteroid(
        id = id,
        codename = codename,
        closeApproachDate = closeApproachDate,
        absoluteMagnitude = absoluteMagnitude,
        estimatedDiameter = estimatedDiameter,
        relativeVelocity = relativeVelocity,
        distanceFromEarth = distanceFromEarth,
        isPotentiallyHazardous = isPotentiallyHazardous)


fun List<DatabaseAsteroid>.toDomainModel(): List<Asteroid> =
    List(size = this.size) {
        this[it].toDomainModel()
    }

