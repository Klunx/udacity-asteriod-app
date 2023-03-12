package com.udacity.asteroidradar.common.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NasaDao {

    @Query("SELECT * FROM picture_of_the_day LIMIT 1")
    fun getPictureOfTheDay(): LiveData<DatabasePictureOfDay>

    @Insert
    fun insertPictureOfTheDay(pictureOfDay: DatabasePictureOfDay)

    @Query("DELETE FROM picture_of_the_day")
    fun clearPictureOfTheDay()
}