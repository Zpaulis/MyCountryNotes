package com.example.mycountrynotes

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.detail_note_link.view.*
import kotlinx.android.synthetic.main.detail_note_link.view.detail_link_button
import kotlinx.android.synthetic.main.detail_note_photo.view.*
import kotlinx.android.synthetic.main.detail_note_text.view.*
import kotlinx.android.synthetic.main.detail_note_text.view.note_close


class DetailNoteReciclerAdapter(
    private val listener:CountryDetailActivity,
    private val notes: MutableList<Note>
) : RecyclerView.Adapter<DetailNoteReciclerAdapter.DetailViewHolder>() {

    abstract class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view){
        abstract fun bind(position: Int)
    }
    inner class TextViewHolder(view: View) :DetailViewHolder(view){
        override fun bind(position: Int) {
            val note = notes[position] as Note.TextNote
            itemView.detail_text.text = note.text
        }
    }
    inner class PhotoViewHolder(view: View) :DetailViewHolder(view) {
        override fun bind(position: Int) {
            val note = notes[position] as Note.PhotoNote
            val uri = Uri.parse(note.picture)
            itemView.detail_image.setImageURI(uri)
        }
    }
    inner class LinkViewHolder(view: View) :DetailViewHolder(view){
        override fun bind(position: Int) {
            val note = notes[position] as Note.LinkNote
            itemView.detail_link.text = note.link
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TEXT_NOTE -> TextViewHolder(inflater.inflate(R.layout.detail_note_text, parent, false))
            PHOTO_NOTE -> PhotoViewHolder(inflater.inflate(R.layout.detail_note_photo, parent, false))
            else -> LinkViewHolder(inflater.inflate(R.layout.detail_note_link, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int =
        when (notes[position]){
            is Note.TextNote -> TEXT_NOTE
            is Note.PhotoNote -> PHOTO_NOTE
            else -> LINK_NOTE}

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(position)
        val detailNotes = notes.map { DetailNote.from(it) } as MutableList<DetailNote>
//        val context = holder.itemView.context

holder.itemView.detail_link_button.setOnClickListener {
    val note = detailNotes[position]
    listener.getUrlFromIntent(note.link)
}

        holder.itemView.setOnClickListener{
            listener.noteClicked(detailNotes[position],getItemViewType(position))
            listener.refreshDetail()
            notifyDataSetChanged()

        }

        holder.itemView.note_close.setOnClickListener{
            val note = detailNotes[position]
            listener.deleteClicked(note)
            notes.removeAt(position)
            notifyDataSetChanged()
        }
    }

    companion object {
        private const val TEXT_NOTE = 0
        private const val PHOTO_NOTE = 1
        private const val LINK_NOTE = 2
    }
}
