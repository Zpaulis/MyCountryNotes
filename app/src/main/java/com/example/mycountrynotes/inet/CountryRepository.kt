package com.example.mycountrynotes.inet

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    class Loading<T> : Resource<T>()
    class Loaded<T> : Resource<T>()
    class Error<T>(val message: String?) : Resource<T>()
}

object CountryRepository {
    private val service: CountryService by lazy { CountryServiceProvider.retrofit }

    fun getAllCountries() = request { service.getAllCountries() }

//    fun getCountriesByName(name: String) = request { service.getCountriesByName(name) }

    private fun <T> request(request: suspend () -> T) = liveData<Resource<T>>(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(request()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message))
        } finally {
            emit(Resource.Loaded())
        }
    }
}