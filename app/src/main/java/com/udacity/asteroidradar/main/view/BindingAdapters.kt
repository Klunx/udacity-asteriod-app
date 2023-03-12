package com.udacity.asteroidradar.main.view

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.pictureoftheday.domain.model.PictureOfDay

@BindingAdapter("setPictureOfTheDay")
fun ImageView.setPictureOfTheDay(data: PictureOfDay?) {
    // https://www.tutorialspoint.com/how-do-i-load-an-image-by-url-using-picasso-library-on-kotlin
    data?.let {
        Picasso.with(context).load(it.url).into(this)
    }
}

@BindingAdapter("setDescriptionFromPictureOfTheDay")
fun ImageView.setDescriptionFromPictureOfTheDay(data: PictureOfDay?) {
    data?.let {
        contentDescription = it.title
    }
}
