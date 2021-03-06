package com.example.mycountrynotes.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.mycountrynotes.R
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.card_short_country_info.view.*

class CountryItemRecyclerAdapter (
    private val listener: AdapterClickListener,
    private val infos: MutableList<CountryInfo>
) :
    RecyclerView.Adapter<CountryItemRecyclerAdapter.KeepViewHolder>() {

    class KeepViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeepViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.card_short_country_info, parent, false)
        return KeepViewHolder(inflater)
    }

    override fun getItemCount() = infos.size

    override fun onBindViewHolder(holder: KeepViewHolder, position: Int) {
        val context = holder.itemView.context
        val info = infos[position]
        val flagUri  = info.flag.toUri()
        holder.itemView.country_name.text = context.getString(R.string.text_with_spaces, info.name)

// SVG to Bitmap from URL via GlideToVectorYou
        GlideToVectorYou
            .init()
            .with(context)
            .load(flagUri, holder.itemView.flagImage)

        holder.itemView.keepClose.setOnClickListener {
            val currentPosition = infos.indexOf(info)
            infos.removeAt(currentPosition)
            notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener {
            listener.itemClicked(infos[position])
            pos = position
        }
    }
    companion object {
        var pos:Int = 0
    }
}