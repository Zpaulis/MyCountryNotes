package com.example.mycountrynotes

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mycountrynotes.main.CountryItemRecyclerAdapter.Companion.pos
import com.example.mycountrynotes.main.MainActivity.Companion.infos
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.activity_country_detail.*

class CountryDetailActivity : AppCompatActivity() {

    private val db get() = Database.getInstance(this)
    val detailNotes = mutableListOf<DetailNote>()
    private var notes = mutableListOf<Note>()
    private lateinit var adapter: DetailNoteReciclerAdapter
    private lateinit var layoutManager: StaggeredGridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        detailNotes.clear()
        detailNotes.addAll(db.detailNoteDao().getCountryNote(infos[pos].name))
        notes = detailNotes.map { it.toNote() } as MutableList<Note>

// design detail activity header
        detail_country_name.text = infos[pos].name
        detail_capital_city.text = infos[pos].capital
        detail_population.text = getString(R.string.number_citezens, infos[pos].population)
        detail_area.text = getString(R.string.number_sqkm, infos[pos].area.toString())
        drawFlag()
// work with notes

//add new note button's listener's
        button_detail_text.setOnClickListener{ addNewNote(0)}
        button_detail_picture.setOnClickListener{ addNewNote(1)}
        button_detail_link.setOnClickListener{ addNewNote(2)}

    layoutManager =
        StaggeredGridLayoutManager(
            resources.getInteger(R.integer.span_count), StaggeredGridLayoutManager.VERTICAL
        ).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        }
    detail_notes.layoutManager = layoutManager
    adapter = DetailNoteReciclerAdapter( this, notes)
    detail_notes.adapter = adapter
    }

    // SVG from Uri to View using GlideToVectorYou
    fun drawFlag(){
        val flagUri  = infos[pos].flag.toUri()
        GlideToVectorYou
            .init()
            .with(this)
            .load(flagUri, detail_flag_background)
    }

    // Add notes to Country
    private fun addNewNote(int :Int){
when (int){
     0 -> {
         //input new TEXT note
         val note = DetailNote(infos[pos].name, "", "", "", NoteType.TEXT,0L )
         note.uid = db.detailNoteDao().insertAll(note).first()
         val intent = Intent(this, TextNoteInput::class.java)
             .putExtra(EXTRA_ID,note.uid)
         startActivityForResult(intent, REQUEST_CODE_DETAILS)
     }

    1 -> {
// choose picture from gallery
        val note = DetailNote(infos[pos].name, "", "", "", NoteType.PHOTO,0L )
        note.uid = db.detailNoteDao().insertAll(note).first()
        val intent = Intent(this, PhotoNoteInput::class.java)
            .putExtra(EXTRA_ID,note.uid)
        startActivityForResult(intent, REQUEST_CODE_DETAILS)

    }
    2 -> {
        //input new LINK
        val note = DetailNote(infos[pos].name, "", "", "", NoteType.LINK,0L )
        note.uid = db.detailNoteDao().insertAll(note).first()
        val intent = Intent(this, LinkNoteInput::class.java)
            .putExtra(EXTRA_ID,note.uid)
        startActivityForResult(intent, REQUEST_CODE_DETAILS)
    }
    else -> {val message = "UNKNOWN OPERATION"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
}

   fun noteClicked (note: DetailNote, viewType: Int){
       when (viewType){
           //edit TEXT note
           0 -> {
               val intent = Intent(this, TextNoteInput::class.java)
           .putExtra(EXTRA_ID,note.uid)
       startActivityForResult(intent, REQUEST_CODE_DETAILS)
           }
           1 -> {
               //Picture edit?
               Toast.makeText(this, note.toString(), Toast.LENGTH_SHORT).show()
           }
           2 ->{
               //edit LINK
               val intent = Intent(this, LinkNoteInput::class.java)
                   .putExtra(EXTRA_ID,note.uid)
               startActivityForResult(intent, REQUEST_CODE_DETAILS)
       }
   }
   }

fun deleteClicked(detailNote: DetailNote){
//    Toast.makeText(this, detailNote.uid.toString(), Toast.LENGTH_SHORT).show()
    db.detailNoteDao().delete(detailNote)

}
    fun getUrlFromIntent (uri: String) {
        val intentLink = Intent(Intent.ACTION_VIEW)
        intentLink.data = Uri.parse(uri)
        startActivity(intentLink)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DETAILS && resultCode == RESULT_OK && data != null){
            refreshDetail()
        }
    }

    fun refreshDetail() {
        detailNotes.clear()
        detailNotes.addAll(db.detailNoteDao().getCountryNote(infos[pos].name))
        notes = detailNotes.map { it.toNote() } as MutableList<Note>
        adapter.notifyDataSetChanged()
    }
// Menu- back
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_ID = "ID1"
        const val REQUEST_CODE_DETAILS = 1234
    }
}

//interface listener {
//    fun noteClicked(note: Note)
//    fun deleteClicked(detailNote: DetailNote)

