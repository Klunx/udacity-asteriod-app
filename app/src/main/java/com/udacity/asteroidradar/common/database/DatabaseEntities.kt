package com.udacity.asteroidradar.common.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.pictureoftheday.domain.model.PictureOfDay

@Entity(tableName = "picture_of_the_day")
class DatabasePictureOfDay constructor(
    @PrimaryKey
    val url: String,
    val title: String,
    val mediaType: String
)

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