package com.example.mycountrynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mycountrynotes.inet.Resource
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AdapterClickListener {

    private val infos = mutableListOf<CountryInfo>()
    var searchName :String = ""

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
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            }
        mainItems.layoutManager = layoutManager
        adapter = CountryItemRecyclerAdapter(this, infos)
        mainItems.adapter = adapter
        refresh()

        main_search_click.setOnClickListener {
            searchName = main_city_search_view.text.toString()
            Toast.makeText(this, searchName, Toast.LENGTH_SHORT).show()
        }
    }

    private fun refresh() {
        viewModel.getAllCountries().observe(this, androidx.lifecycle.Observer {
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
        })
    }


    override fun deleteClicked(item: CountryInfo) {
//        viewModel.removeItem(item.id).observe(this, Observer { refresh() })
    }

    override fun itemClicked(item: CountryInfo) {
//        val intent = Intent(this, DetailActivity::class.java)
//            .putExtra(EXTRA_ID, info.name)
//        startActivityForResult(intent, REQUEST_CODE_DETAILS)
    }
    companion object {
        const val EXTRA_ID = "lv.romstr.mobile.extras.shopping_item_id"
        const val REQUEST_CODE_DETAILS = 1234
    }

}
interface AdapterClickListener {

    fun itemClicked(item: CountryInfo)

    fun deleteClicked(item: CountryInfo)

}
