package com.example.mycountrynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mycountrynotes.CountryDetailActivity.Companion.EXTRA_ID
import com.example.mycountrynotes.main.CountryItemRecyclerAdapter
import com.example.mycountrynotes.main.MainActivity
import kotlinx.android.synthetic.main.detail_note_link.*
import kotlinx.android.synthetic.main.text_input.*

class LinkNoteInput : AppCompatActivity() {
    private val db get() = Database.getInstance(this)
    private val detailNotes = mutableListOf<DetailNote>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.text_input)
        detailNotes.addAll(db.detailNoteDao().getCountryNote(MainActivity.infos[CountryItemRecyclerAdapter.pos].name))
        val id = intent.getLongExtra(EXTRA_ID, 0)
        val note = db.detailNoteDao().getNoteById(id)
        detail_link.setText(note.link)
        button_save_text.setOnClickListener {
            note.link =  detail_link.text.toString()
            db.detailNoteDao().update(note)
            val intent = Intent().putExtra(EXTRA_ID, id)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}