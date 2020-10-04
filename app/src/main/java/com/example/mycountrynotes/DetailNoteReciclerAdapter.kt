package com.example.mycountrynotes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.room.PrimaryKey
import kotlinx.android.synthetic.main.detail_note_link.view.*
import kotlinx.android.synthetic.main.detail_note_text.view.*


class DetailNoteReciclerAdapter(
//    private val listener: DetailAdapterClickListener,
    private val notes: MutableList<DetailNote>
) : RecyclerView.Adapter<DetailNoteReciclerAdapter.DetailViewHolder>() {

    class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view)
    
//    abstract class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view){
//        abstract fun bind(position: Int)
//    }
//inner class TextViewHolder(view: View) :DetailViewHolder(view){
//    override fun bind(position: Int) {
//        val note = notes[position] as CountryDetailActivity.DetailNoteText
//         itemView.detail_text.text = note.text
//    }
//}
//    inner class LinkViewHolder(view: View) :DetailViewHolder(view){
//        override fun bind(position: Int) {
//            val note = notes[position] as CountryDetailActivity.DetailNoteLink
//            itemView.detail_link.text = note.link
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_note_text, parent, false)
        return DetailViewHolder(view)
//val inflater = LayoutInflater.from(parent.context)
//        return when (viewType) {
//            TEXT_NOTE -> TextViewHolder(inflater.inflate(R.layout.detail_note_text, parent, false))
//            else -> LinkViewHolder(inflater.inflate(R.layout.detail_note_link, parent, false))
//        }
    }

//    override fun getItemViewType(position: Int): Int =
//        when (notes[position]){
//            is DetailNote -> TEXT_NOTE
////            is CountryDetailActivity.DetailNoteText -> TEXT_NOTE
//
//            else -> LINK_NOTE}

    override fun getItemCount() = notes.size


    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {

//holder.bind(position)
    val note = notes[position]
        val context = holder.itemView.context
holder.itemView.detail_text.text = note.text


        holder.itemView.setOnClickListener{
//            listener.noteClicked(notes[position])
        }
    }


    companion object {
        private const val TEXT_NOTE = 0
        private const val PHOTO_NOTE = 1
        private const val LINK_NOTE = 2
    }



}
