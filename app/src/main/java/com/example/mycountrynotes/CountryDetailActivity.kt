package com.example.mycountrynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.example.mycountrynotes.main.CountryItemRecyclerAdapter.Companion.pos
import com.example.mycountrynotes.main.MainActivity.Companion.EXTRA_ID
import com.example.mycountrynotes.main.MainActivity.Companion.infos
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.activity_country_detail.*

class CountryDetailActivity : AppCompatActivity() {

    private val db get() = Database.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)

        val id = intent.getLongExtra(EXTRA_ID, 0)
        val note = db.detailNoteDao().getNoteById(id)

// design detail activity header
        detail_country_name.text = infos[pos].name
        detail_capital_city.text = infos[pos].capital
        detail_population.text = getString(R.string.number_citezens, infos[pos].population)
        detail_area.text = getString(R.string.number_sqkm, infos[pos].area.toString())
        drawFlag()
// work with notes



    }
    fun drawFlag(){
        val flagUri  = infos[pos].flag.toUri()
        GlideToVectorYou
            .init()
            .with(this)
            .load(flagUri, detail_flag_background)
    }
}