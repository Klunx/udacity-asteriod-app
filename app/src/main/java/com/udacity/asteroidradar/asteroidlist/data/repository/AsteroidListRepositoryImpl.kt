package com.udacity.asteroidradar.asteroidlist.data.repository

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.asteroid.domain.model.Asteroid
import com.udacity.asteroidradar.asteroidlist.data.model.mapper.toDatabaseModel
import com.udacity.asteroidradar.asteroidlist.domain.mapper.toDomainModel
import com.udacity.asteroidradar.asteroidlist.domain.repository.AsteroidListRepository
import com.udacity.asteroidradar.common.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.common.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.common.database.NasaDatabase
import com.udacity.asteroidradar.common.database.mapper.toDomainModel
import com.udacity.asteroidradar.common.network.NasaApiService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

enum class AsteroidListFilter(val value: String) {
    WEEK("week"),
    TODAY("today"),
    SAVED("all")
}

class AsteroidListRepositoryImpl(
    private val database: NasaDatabase,
    private val nasaApiService: NasaApiService
) : AsteroidListRepository {

    private val TAG = "AsteroidListRepositoryImpl"

    override suspend fun getListOfAsteroids() {
        val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
        nasaApiService.getListOfAsteroids(nextSevenDaysFormattedDates.first(), nextSevenDaysFormattedDates.last(), API_KEY)
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

    fun getFilteredList(filter: AsteroidListFilter): List<Asteroid> {
        return loadDatabase(filter)
    }

    private fun loadDatabase(filter: AsteroidListFilter): List<Asteroid> {
        return when (filter) {
            AsteroidListFilter.SAVED -> getAllAsteroids()
            AsteroidListFilter.WEEK -> getCurrentWeek()
            AsteroidListFilter.TODAY -> getTodayAsteroidsList()
        }
    }

    private fun getAllAsteroids(): List<Asteroid> {
        return try {
            database.nasaDao.getAsteroids().toDomainModel()
        } catch (e: Exception) {
            emptyList<Asteroid>()
        }
    }

    @SuppressLint("WeekBasedYear")
    private fun getTodayAsteroidsList(): List<Asteroid> {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val today = dateFormat.format(currentTime)
        val days = listOf(today)
        return database.nasaDao.getAsteroidsInDays(days).toDomainModel()
    }

    @SuppressLint("WeekBasedYear")
    private fun getCurrentWeek(): List<Asteroid> {
        val formattedDateList = ArrayList<String>()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        val dateFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
            formattedDateList.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return database.nasaDao.getAsteroidsInDays(formattedDateList).toDomainModel()
    }
}