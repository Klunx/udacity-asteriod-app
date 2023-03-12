package com.udacity.asteroidradar.pictureoftheday.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PictureOfDay(
    val mediaType: String,
    val title: String,
    val url: String
) : Parcelable
