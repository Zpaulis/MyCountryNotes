package com.example.mycountrynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mycountrynotes.main.CountryItemRecyclerAdapter.Companion.pos
import com.example.mycountrynotes.main.MainActivity.Companion.infos
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.OnMapReadyCallback

val coord = infos[pos].latlng
val countryMarker : LatLng = LatLng(coord[0],coord[1])
class CountryMapActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        Toast.makeText(this, coord.toString(), Toast.LENGTH_LONG).show()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_map)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.country_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.addMarker(MarkerOptions().position(countryMarker).title(infos[pos].name))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(countryMarker))
    }
}
