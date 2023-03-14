package com.udacity.asteroidradar.common.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "picture_of_the_day")
class DatabasePictureOfDay constructor(
    @PrimaryKey
    val url: String,
    val title: String,
    val mediaType: String
)

@Entity(tableName = "asteroid")
class DatabaseAsteroid constructor(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)