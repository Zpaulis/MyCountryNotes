package com.example.mycountrynotes.inet

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CountryServiceProvider {

    val retrofit: CountryService by lazy {
        Retrofit.Builder()
            .baseUrl("https://restcountries.eu/rest/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountryService::class.java)
    }
}