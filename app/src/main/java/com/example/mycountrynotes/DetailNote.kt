package com.example.mycountrynotes

import androidx.room.*
import androidx.room.TypeConverter

enum class NoteType{
    TEXT,
    PHOTO,
    LINK
}
class Converters{
    @TypeConverter
    fun toNoteType(value: String) = enumValueOf<NoteType>(value)
    @TypeConverter
    fun fromNoteType(value: NoteType) = value.name
}
sealed class Note{
    data class TextNote(val text:String):Note()
    data class PhotoNote(val picture:String):Note()
    data class LinkNote(val link:String):Note()
}
@Entity(tableName = "detail_note")
data class DetailNote(
    val name: String = "",
    var text: String = "",
    var picture: String = "",
    var link: String = "",
    val type: NoteType,
    @PrimaryKey(autoGenerate = true) var uid: Long = 0
){
    fun toNote():Note = when (type) {
        NoteType.TEXT -> Note.TextNote(text = text)
        NoteType.PHOTO -> Note.PhotoNote(picture=picture)
        NoteType.LINK -> Note.LinkNote(link=link)
    }
    companion object{
        fun from(note: Note): DetailNote = when (note){
            is Note.TextNote -> DetailNote(text = note.text, type = NoteType.TEXT)
            is Note.PhotoNote -> DetailNote(picture = note.picture, type = NoteType.PHOTO)
            is Note.LinkNote -> DetailNote(link = note.link, type = NoteType.LINK)
        }
    }
}

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