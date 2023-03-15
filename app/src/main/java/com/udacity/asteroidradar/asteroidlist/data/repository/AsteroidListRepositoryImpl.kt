package com.udacity.asteroidradar.asteroidlist.data.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.asteroid.domain.model.Asteroid
import com.udacity.asteroidradar.asteroidlist.data.model.mapper.toDatabaseModel
import com.udacity.asteroidradar.asteroidlist.domain.mapper.toDomainModel
import com.udacity.asteroidradar.asteroidlist.domain.repository.IAsteroidListRepository
import com.udacity.asteroidradar.common.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.common.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.common.database.NasaDatabase
import com.udacity.asteroidradar.common.database.mapper.toDomainModel
import com.udacity.asteroidradar.common.network.NasaApiService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AsteroidListRepositoryImpl(
    private val database: NasaDatabase,
    private val nasaApiService: NasaApiService
) : IAsteroidListRepository {

    private val TAG = "AsteroidListRepositoryImpl"
    override val apiKey = "njunRLyfKbrdac47JO9rJnf6boN1S7dWF9BQXjDq"

    override val listOfAsteroids: LiveData<List<Asteroid>> = Transformations.map(database.nasaDao.getAsteroids()) { databaseListOfAsteroids ->
        databaseListOfAsteroids.toDomainModel()
    }

    override suspend fun getListOfAsteroids() {
        val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
        nasaApiService.getListOfAsteroids(nextSevenDaysFormattedDates.first(), nextSevenDaysFormattedDates.last(), apiKey)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    response.body()?.let {
                        val listDataAsteroid = parseAsteroidsJsonResult(JSONObject(it)).toDomainModel()
                        // Having issues saying this was running in the main thread I have sent it in another thread.
                        Thread {
                            database.nasaDao.clearAsteroid()
                            database.nasaDao.insertAsteroids(listDataAsteroid.toDatabaseModel())
                        }.start()
                    }
                }

                @SuppressLint("LongLogTag")
                override fun onFailure(call: Call<String>, t: Throwable) {
                    t.message?.let { Log.e(TAG, it) }
                }
            })
    }
}