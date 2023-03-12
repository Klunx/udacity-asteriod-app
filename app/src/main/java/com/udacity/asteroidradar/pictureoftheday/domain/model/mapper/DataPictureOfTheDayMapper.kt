package com.udacity.asteroidradar.pictureoftheday.domain.model.mapper

import com.udacity.asteroidradar.pictureoftheday.data.model.DataPictureOfDay
import com.udacity.asteroidradar.pictureoftheday.domain.model.PictureOfDay

fun PictureOfDay.asDataModel(): DataPictureOfDay {
    return DataPictureOfDay(
        mediaType = mediaType,
        title = title,
        url = url
    )
}