package com.example.mycountrynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mycountrynotes.main.CountryItemRecyclerAdapter.Companion.pos
import com.example.mycountrynotes.main.MainActivity.Companion.infos
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.activity_country_detail.*
import kotlinx.android.synthetic.main.activity_main.*

class CountryDetailActivity : AppCompatActivity() {
data class DetailNoteText(
    override val name: String,
    override var text: String?,
    override var picture: String?,
    override var link: String?,
    override var uid: Long = 0
) : DetailNote(name, text, picture, link, uid)

data class DetailNoteLink(
        override val name: String,
        override var text: String?,
        override var picture: String?,
        override var link: String?,
        override var uid: Long = 0
) : DetailNote(name, text, picture, link, uid)

    private val db get() = Database.getInstance(this)
    private val notes = mutableListOf<DetailNote>()
    private lateinit var adapter: DetailNoteReciclerAdapter
    private lateinit var layoutManager: StaggeredGridLayoutManager

//sealed class DetailNote
//    data class DetailNoteText(val name: String, val text: String) : DetailNote()
//    data class DetailNoteLink(val name: String, val link: String) : DetailNote()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)
        notes.addAll(db.detailNoteDao().getCountryNote(infos[pos].name))

// design detail activity header
        detail_country_name.text = infos[pos].name
        detail_capital_city.text = infos[pos].capital
        detail_population.text = getString(R.string.number_citezens, infos[pos].population)
        detail_area.text = getString(R.string.number_sqkm, infos[pos].area.toString())
        drawFlag()
// work with notes

//add new note
        button_detail_text.setOnClickListener{ addNewNote(0)}
        button_detail_picture.setOnClickListener{ addNewNote(1)}
        button_detail_link.setOnClickListener{ addNewNote(2)}


        if (notes.isNotEmpty()) {
    layoutManager =
        StaggeredGridLayoutManager(
            resources.getInteger(R.integer.span_count), StaggeredGridLayoutManager.VERTICAL
        ).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        }
    detail_notes.layoutManager = layoutManager
    adapter = DetailNoteReciclerAdapter(notes)
    detail_notes.adapter = adapter

}

    }
//    fun sortNotes(notes: MutableList<DetailNote>){
//        if (notes.text !== "" )-> DetailNoteText
//    }

    // SVG from Uri to View using GlideToVectorYou
    fun drawFlag(){
        val flagUri  = infos[pos].flag.toUri()
        GlideToVectorYou
            .init()
            .with(this)
            .load(flagUri, detail_flag_background)
    }
    private fun addNewNote(int :Int){
when (int){
     0 -> {
         val note = DetailNote(infos[pos].name, "", "", "", 0L )
         note.uid = db.detailNoteDao().insertAll(note).first()
         val intent = Intent(this, TextNoteInput::class.java)
             .putExtra(EXTRA_ID,note.uid)
         startActivityForResult(intent, REQUEST_CODE_DETAILS)
     }

    1 -> {
// here must be picture get from galery
    }
    2 -> {
        val id = ""
        val intent = Intent(this, TextNoteInput::class.java)
            .putExtra(EXTRA_ID,id)
        startActivityForResult(intent, RESULT_OK)
    }
    else -> {val message = "UNKNOWN OPERATION"}

}
}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DETAILS && resultCode == RESULT_OK && data != null){
            val id = data.getLongExtra(EXTRA_ID, 0)
            val note = db.detailNoteDao().getNoteById(id)
            val position = notes.indexOfFirst { it.uid == note.uid }
            notes[position] = note
            adapter.notifyItemChanged(position)
        }
    }
    companion object {
        const val EXTRA_ID = "ID1"
        const val REQUEST_CODE_DETAILS = 1234
    }
}

interface DetailAdapterClickListener {

    fun noteClicked(note: DetailNote)

    fun NoteDeleteClicked(note: DetailNote)

}
