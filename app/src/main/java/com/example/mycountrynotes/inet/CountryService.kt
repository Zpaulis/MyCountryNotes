package com.example.mycountrynotes.inet

import com.example.mycountrynotes.CountryInfo
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryService {
    @GET("all")
    suspend fun getAllCountries(): List<CountryInfo>

    @GET("name/{name}")
    suspend fun getCountriesByName(@Path("name") id: String):  List<CountryInfo>
}