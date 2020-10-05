package com.example.mycountrynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.net.toUri
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mycountrynotes.main.CountryItemRecyclerAdapter.Companion.pos
import com.example.mycountrynotes.main.MainActivity.Companion.infos
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.activity_country_detail.*

class CountryDetailActivity : AppCompatActivity() {


    private val db get() = Database.getInstance(this)
    val databaseNotes = mutableListOf<DetailNote>()
    private var notes = mutableListOf<Note>()
    private lateinit var adapter: DetailNoteReciclerAdapter
    private lateinit var layoutManager: StaggeredGridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        databaseNotes.addAll(db.detailNoteDao().getCountryNote(infos[pos].name))
        notes = databaseNotes.map { it.toNote() } as MutableList<Note>

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
         val note = DetailNote(infos[pos].name, "", "", "", NoteType.TEXT,0L )
         note.uid = db.detailNoteDao().insertAll(note).first()
         val intent = Intent(this, TextNoteInput::class.java)
             .putExtra(EXTRA_ID,note.uid)
         startActivityForResult(intent, REQUEST_CODE_DETAILS)
     }

    1 -> {
// here must be picture get from galery
    }
    2 -> {
        val note = DetailNote(infos[pos].name, "", "", "", NoteType.LINK,0L )
        note.uid = db.detailNoteDao().insertAll(note).first()
        val intent = Intent(this, LinkNoteInput::class.java)
            .putExtra(EXTRA_ID,note.uid)
        startActivityForResult(intent, REQUEST_CODE_DETAILS)
    }
    else -> {val message = "UNKNOWN OPERATION"}

}
}

   fun noteClicked (note: DetailNote){
       val intent = Intent(this, TextNoteInput::class.java)
           .putExtra(EXTRA_ID,note.uid)
       startActivityForResult(intent, REQUEST_CODE_DETAILS)
   }

 fun deleteClicked(datbaseNote: DetailNote){
    db.detailNoteDao().delete(datbaseNote)
//     refreshDetail()
}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DETAILS && resultCode == RESULT_OK && data != null){
            refreshDetail()
        }
    }

    fun refreshDetail() {
        databaseNotes.clear()
        databaseNotes.addAll(db.detailNoteDao().getCountryNote(infos[pos].name))
        notes = databaseNotes.map { it.toNote() } as MutableList<Note>
        adapter.notifyDataSetChanged()
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId){
//            android.R.id.home -> {
//                finish()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    companion object {
        const val EXTRA_ID = "ID1"
        const val REQUEST_CODE_DETAILS = 1234
        const val REQUEST_CODE_DETAILS2 = 4321
        const val REQUEST_CODE_DETAILS3 = 1111
    }
}

interface listener {
    fun noteClicked(note: Note)
    fun deleteClicked(note: Note)
}
