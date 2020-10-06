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
    data class TextNote(val text:String,val uid: Long):Note()
    data class PhotoNote(val picture:String,val uid: Long):Note()
    data class LinkNote(val link:String,val uid: Long):Note()
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
        NoteType.TEXT -> Note.TextNote(text = text, uid = uid)
        NoteType.PHOTO -> Note.PhotoNote(picture=picture, uid = uid)
        NoteType.LINK -> Note.LinkNote(link=link, uid = uid)
    }
    companion object{
        fun from(note: Note): DetailNote = when (note){
            is Note.TextNote -> DetailNote(text = note.text, type = NoteType.TEXT, uid = note.uid)
            is Note.PhotoNote -> DetailNote(picture = note.picture, type = NoteType.PHOTO, uid = note.uid)
            is Note.LinkNote -> DetailNote(link = note.link, type = NoteType.LINK, uid = note.uid)
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