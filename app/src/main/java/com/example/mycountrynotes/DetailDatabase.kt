package com.example.mycountrynotes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [DetailNote::class])
abstract class DetailDatabase : RoomDatabase() {
    abstract fun detailNoteDao(): DetailNoteDao

}


object Database {

    private var instance: DetailDatabase? = null

    fun getInstance(context: Context) = instance ?: Room.databaseBuilder(
        context.applicationContext, DetailDatabase::class.java, "detail-db"
    )
        .allowMainThreadQueries()
        .build()
        .also { instance = it }
}