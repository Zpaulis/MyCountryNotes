package com.example.mycountrynotes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(version = 3, entities = [DetailNote::class])
@TypeConverters(Converters::class)
abstract class DetailDatabase : RoomDatabase() {
    abstract fun detailNoteDao(): DetailNoteDao
}

object Database {

    private var instance: DetailDatabase? = null

    fun getInstance(context: Context) = instance ?: Room.databaseBuilder(
        context.applicationContext, DetailDatabase::class.java, "detail-db"
    )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()
        .also { instance = it }
}