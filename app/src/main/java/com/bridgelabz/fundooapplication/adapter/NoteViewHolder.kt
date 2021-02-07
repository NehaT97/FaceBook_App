package com.bridgelabz.fundooapplication.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.fundooapplication.R
import com.bridgelabz.fundooapplication.model.Note

class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val description = view.findViewById<TextView>(R.id.description)
    private val title = view.findViewById<TextView>(R.id.noteTitle)

    fun bind(note: Note) {
        description.text = note.description
        title.text = note.title
    }
}