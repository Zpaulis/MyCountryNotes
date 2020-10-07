package com.example.mycountrynotes.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.activity.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
import com.example.mycountrynotes.CountryDetailActivity
import com.example.mycountrynotes.R
import com.example.mycountrynotes.inet.Resource
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AdapterClickListener {

    private var searchName :String = ""

    private lateinit var adapter: CountryItemRecyclerAdapter
    private lateinit var layoutManager: StaggeredGridLayoutManager
    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutManager =
            StaggeredGridLayoutManager(
                resources.getInteger(R.integer.span_count), StaggeredGridLayoutManager.VERTICAL
            ).apply {
                gapStrategy = GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            }
        mainItems.layoutManager = layoutManager
        adapter = CountryItemRecyclerAdapter(this, infos)
        mainItems.adapter = adapter
        if (infos.size == 0) refresh()

        main_search_click.setOnClickListener { refresh() }
        refreshLayout.setOnRefreshListener { refresh() }
    }

    private fun refresh() {
        if (main_city_search_view.text.toString() == ""){
        viewModel.getAllCountries().observe(this, Observer {
            when (it) {
                is Resource.Loading -> refreshLayout.isRefreshing = true
                is Resource.Loaded -> refreshLayout.isRefreshing = false
                is Resource.Error -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                is Resource.Success -> {
                    infos.clear()
                    infos.addAll(it.data)
                    adapter.notifyDataSetChanged()
                }
            }
        })} else {
            searchName = main_city_search_view.text.toString()
            viewModel.getCountriesByName(searchName).observe(this, Observer {
                when (it) {
                    is Resource.Loading -> refreshLayout.isRefreshing = true
                    is Resource.Loaded -> refreshLayout.isRefreshing = false
                    is Resource.Error -> Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    is Resource.Success -> {
                        infos.clear()
                        infos.addAll(it.data)
                        adapter.notifyDataSetChanged()
                    }
                }
            })}
        }

    override fun itemClicked(item: CountryInfo) {
        startActivity(Intent(this, CountryDetailActivity::class.java))
    }
    companion object {
        val infos = mutableListOf<CountryInfo>()
    }
}
interface AdapterClickListener {
    fun itemClicked(item: CountryInfo)
}
