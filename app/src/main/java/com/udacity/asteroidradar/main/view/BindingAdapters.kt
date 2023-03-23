package com.udacity.asteroidradar.main.view

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Constants.EXCEPTED_FORMAT
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.asteroid.domain.model.Asteroid
import com.udacity.asteroidradar.pictureoftheday.domain.model.PictureOfDay

@BindingAdapter("setPictureOfTheDay")
fun ImageView.setPictureOfTheDay(data: PictureOfDay?) {
    // https://www.tutorialspoint.com/how-do-i-load-an-image-by-url-using-picasso-library-on-kotlin
    data?.let {
        if (it.mediaType.lowercase() != EXCEPTED_FORMAT) {
            Picasso.with(context).load(it.url).into(this)
        }
        contentDescription =
            String.format(context.getString(R.string.nasa_picture_of_day_content_description_format), it.title)
    }
}

@BindingAdapter("setTitlePictureOfTheDay")
fun TextView.setTitlePictureOfTheDay(data: PictureOfDay?) {
    data?.let {
        text = String.format(context.getString(R.string.nasa_picture_of_day_content_description_format), it.title)
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidListAdapter
    adapter.submitList(data)
}

@BindingAdapter("nasaApiStatus")
fun ProgressBar.bindStatus(status: NasaApiStatus) {
    when (status) {
        NasaApiStatus.LOADING -> {
            visibility = View.VISIBLE
        }
        NasaApiStatus.ERROR -> {
            visibility = View.VISIBLE
        }
        NasaApiStatus.DONE -> {
            visibility = View.GONE
        }
    }
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription = imageView.context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = imageView.context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = imageView.context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = imageView.context.getString(R.string.not_hazardous_asteroid_image)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
    textView.contentDescription = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
    textView.contentDescription = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
    textView.contentDescription = String.format(context.getString(R.string.km_s_unit_format), number)
}
