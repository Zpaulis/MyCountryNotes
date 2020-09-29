package com.example.mycountrynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mycountrynotes.CountryItemRecyclerAdapter.Companion.pos
import com.example.mycountrynotes.MainActivity.Companion.infos
import kotlinx.android.synthetic.main.activity_country_detail.*

class CountryDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_detail)
        detail_country_name.text = infos[pos].name
        detail_capital_city.text = infos[pos].capital
        detail_population.text = infos[pos].population.toString()
        detail_area.text = infos[pos].area.toString()
    }
}