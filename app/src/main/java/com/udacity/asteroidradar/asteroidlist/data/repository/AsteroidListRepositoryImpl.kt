package com.udacity.asteroidradar.asteroidlist.data.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.asteroid.domain.model.Asteroid
import com.udacity.asteroidradar.asteroidlist.data.model.mapper.toDatabaseModel
import com.udacity.asteroidradar.asteroidlist.domain.mapper.toDomainModel
import com.udacity.asteroidradar.asteroidlist.domain.repository.AsteroidListRepository
import com.udacity.asteroidradar.common.api.getDaysFormattedDates
import com.udacity.asteroidradar.common.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.common.api.getTodayAsAnArray
import com.udacity.asteroidradar.common.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.common.database.NasaDatabase
import com.udacity.asteroidradar.common.database.mapper.toDomainModel
import com.udacity.asteroidradar.common.network.NasaApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.util.*

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

    private var _listOfAsteroids = MutableLiveData<List<Asteroid>>()
    override val listOfAsteroids: LiveData<List<Asteroid>>
        get() = _listOfAsteroids

    override suspend fun getListOfAsteroids() {
        // I used this approach to remove the enqueue call. I was having a lot of timeouts before.
        //https://proandroiddev.com/suspend-what-youre-doing-retrofit-has-now-coroutines-support-c65bd09ba067
        GlobalScope.async(Dispatchers.IO) {
            retrieveAsteroidList(getDaysFormattedDates())
        }.await()
    }

    override suspend fun getNextSevenDaysAsteroids() {
        GlobalScope.async(Dispatchers.IO) {
            retrieveAsteroidList(getNextSevenDaysFormattedDates())
        }.await()
    }

    @SuppressLint("LongLogTag")
    private suspend fun retrieveAsteroidList(daysFormattedDates: ArrayList<String>) {
        val finished = GlobalScope.async {
            try {
                val response = nasaApiService.getListOfAsteroids(daysFormattedDates.first(), daysFormattedDates.last(), API_KEY)
                delay(10000L)
                val listDataAsteroid = parseAsteroidsJsonResult(JSONObject(response), daysFormattedDates).toDomainModel()
                database.nasaDao.insertAsteroids(listDataAsteroid.toDatabaseModel())
                getFilteredList(AsteroidListFilter.SAVED)
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
                getFilteredList(AsteroidListFilter.SAVED)
            }
        }
    }

    fun getFilteredList(filter: AsteroidListFilter) {
        return loadDatabase(filter)
    }

    private fun loadDatabase(filter: AsteroidListFilter) {
        when (filter) {
            AsteroidListFilter.SAVED -> getAllAsteroids()
            AsteroidListFilter.WEEK -> getCurrentWeek()
            AsteroidListFilter.TODAY -> getTodayAsteroidsList()
        }
    }

    private fun getAllAsteroids() {
        try {
            _listOfAsteroids.postValue(database.nasaDao.getAsteroids().toDomainModel())
        } catch (e: Exception) {
            emptyList<Asteroid>()
        }
    }

    @SuppressLint("WeekBasedYear")
    private fun getTodayAsteroidsList() {
        try {
            _listOfAsteroids.postValue(database.nasaDao.getAsteroidsInDays(getTodayAsAnArray()).toDomainModel())
        } catch (e: Exception) {
            emptyList<Asteroid>()
        }
    }

    @SuppressLint("WeekBasedYear")
    private fun getCurrentWeek() {
        try {
            _listOfAsteroids.postValue(database.nasaDao.getAsteroidsInDays(getNextSevenDaysFormattedDates()).toDomainModel())
        } catch (e: Exception) {
            emptyList<Asteroid>()
        }
    }
}