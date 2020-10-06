package com.example.mycountrynotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mycountrynotes.main.CountryItemRecyclerAdapter
import com.example.mycountrynotes.main.MainActivity
const val TAG = "PHOTO NOTE INPUT"

class PhotoNoteInput : AppCompatActivity() {

    private val db get() = Database.getInstance(this)
    private val detailNotes = mutableListOf<DetailNote>()
    private fun openGalleryForImage() {
        val intentPhoto = Intent(Intent.ACTION_PICK)
        intentPhoto.type = "image/*"
        startActivityForResult(intentPhoto, REQUEST_CODE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        detailNotes.addAll(
            db.detailNoteDao()
                .getCountryNote(MainActivity.infos[CountryItemRecyclerAdapter.pos].name)
        )
        openGalleryForImage()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val id = intent.getLongExtra(CountryDetailActivity.EXTRA_ID, 0)
            val note = db.detailNoteDao().getNoteById(id)
            note.picture = data?.data.toString()
            db.detailNoteDao().update(note)
            val intent = Intent().putExtra(CountryDetailActivity.EXTRA_ID, id)
            setResult(RESULT_OK, intent)
        }
        finish()
    }
    val REQUEST_CODE = 100
}