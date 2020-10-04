package com.example.mycountrynotes

import androidx.room.*

@Entity(tableName = "detail_note")
open class DetailNote(
    open val name: String,
    open var text: String?,
    open var picture: String?,
    open var link: String?,
    @PrimaryKey(autoGenerate = true) open var uid: Long = 0
)

@Dao
interface DetailNoteDao {
    @Query("SELECT * FROM detail_note WHERE name = :noteName")
    fun getCountryNote(noteName: String): List<DetailNote>

    @Query("SELECT * FROM detail_note WHERE uid = :noteId")
    fun getNoteById(noteId: Long): DetailNote

    @Insert
    fun insertAll(vararg notes: DetailNote): List<Long>

    @Update
    fun update(note: DetailNote)

    @Delete
    fun delete(note: DetailNote)
}