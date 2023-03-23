package com.udacity.asteroidradar.common.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NasaDao {

    @Query("SELECT * FROM picture_of_the_day LIMIT 1")
    fun getPictureOfTheDay(): LiveData<DatabasePictureOfDay>

    @Insert
    fun insertPictureOfTheDay(pictureOfDay: DatabasePictureOfDay)

    @Query("DELETE FROM picture_of_the_day")
    fun clearPictureOfTheDay()

    @Query("DELETE FROM asteroid")
    fun clearAsteroid()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroids(asteroids: List<DatabaseAsteroid>)

    @Query("SELECT * FROM asteroid ORDER BY closeApproachDate DESC")
    fun getAsteroids(): List<DatabaseAsteroid>

    @Query("SELECT * FROM asteroid where closeApproachDate IN (:days) ORDER BY closeApproachDate DESC")
    fun getAsteroidsInDays(days: List<String>): List<DatabaseAsteroid>
}