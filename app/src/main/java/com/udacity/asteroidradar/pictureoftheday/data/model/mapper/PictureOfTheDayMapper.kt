package com.udacity.asteroidradar.pictureoftheday.data.model.mapper

import com.udacity.asteroidradar.pictureoftheday.data.model.DataPictureOfDay
import com.udacity.asteroidradar.pictureoftheday.domain.model.PictureOfDay

fun DataPictureOfDay.toDomainModel(): PictureOfDay {
    return PictureOfDay(
        mediaType = mediaType,
        title = title,
        url = url
    )
}