package com.example.mycountrynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.example.mycountrynotes.CountryItemRecyclerAdapter.Companion.pos
import com.example.mycountrynotes.MainActivity.Companion.infos
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.activity_country_detail.*

class CountryDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)
        detail_country_name.text = infos[pos].name
        detail_capital_city.text = infos[pos].capital
        detail_population.text = getString(R.string.number_citezens, infos[pos].population)
        detail_area.text = getString(R.string.number_sqkm, infos[pos].area.toString())
        drawFlag()
    }
    fun drawFlag(){
        val flagUri  = infos[pos].flag.toUri()
        GlideToVectorYou
            .init()
            .with(this)
            .load(flagUri, detail_flag_background)
    }
}