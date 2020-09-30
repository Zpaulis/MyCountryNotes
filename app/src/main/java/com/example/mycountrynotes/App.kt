package com.example.mycountrynotes

import android.app.Application
import androidx.room.Room

class App : Application() {

    val db by lazy {
        Room.databaseBuilder(this, DetailDatabase::class.java, "detail-db")
            .allowMainThreadQueries()
            .build()
    }

}