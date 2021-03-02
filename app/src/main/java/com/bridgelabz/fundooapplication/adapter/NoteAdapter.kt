package com.bridgelabz.fundooapplication.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.fundooapplication.R
import com.bridgelabz.fundooapplication.model.Note


class NoteAdapter(private var notes: ArrayList<Note>, val isTrashPage: Boolean, val isArchived: Boolean) : RecyclerView.Adapter<NoteViewHolder>(), Filterable {
    private var mOnItemClickLister: OnItemClickListener? = null
    private var noteListTemp = ArrayList<Note>(notes)

    interface OnItemClickListener {

        fun onItemClicked(view: View?, pos: Int)
        fun onDeleteButtonClicked(view: View?, pos: Int)
        fun onArchivedButtonClicked(view: View?, pos: Int)
        fun onNotificationButtonClicked(
            view: View?,
            pos: Int,
            noteTitle: String,
            noteDescription: String
        )
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickLister = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val displayView = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item_fragment, parent, false)
        Log.i("ON_Create_ViewHolder", "calling ->{$displayView}")
        val viewHolder =  NoteViewHolder(displayView)

        displayView.findViewById<Button>(R.id.deleteButton).setOnClickListener {
            Log.i("Delete", "button clicked")
            mOnItemClickLister?.onDeleteButtonClicked(it,viewHolder.adapterPosition)
        }

        displayView.findViewById<Button>(R.id.archivedButton).setOnClickListener {
            Log.i("Archived","button Clicked")
            mOnItemClickLister?.onArchivedButtonClicked(it,viewHolder.adapterPosition)
        }

        displayView.findViewById<Button>(R.id.reminderButton).setOnClickListener {
            Log.i("reminder","button Clicked")
            val note = notes[viewHolder.adapterPosition]
            val noteTitle= note.title
            val noteDescription = note.description
            Log.i("CHECK_TITLE","${noteTitle}")
            Log.i("CHECK_Description","${noteDescription}")
            mOnItemClickLister?.onNotificationButtonClicked(it,viewHolder.adapterPosition,noteTitle,noteDescription)
        }

        return viewHolder
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
        Log.i("ON_BIND_VIEW_HOLDER", "Calling {$position}")

        holder.itemView.setOnClickListener {
            Log.i("FinallyItem", "Working")
            mOnItemClickLister?.onItemClicked(it, position)
        }
        if (isTrashPage) {
            holder.itemView.findViewById<Button>(R.id.deleteButton).setBackgroundResource(R.drawable.ic_baseline_restore_from_trash_24)
            holder.itemView.findViewById<Button>(R.id.archivedButton).isEnabled = false
        }
        if (isArchived) {
            holder.itemView.findViewById<Button>(R.id.archivedButton).setBackgroundResource(R.drawable.ic_baseline_unarchive_24)
        }

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                Log.i("Note List Notes", "$noteListTemp")
                val filteredNotes = ArrayList<Note>()
                if (constraint.isNullOrBlank() || constraint.isNullOrEmpty()) {
                    filteredNotes.addAll(noteListTemp)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()
                    for (note: Note in noteListTemp) {
                        if (note.title.toLowerCase().contains(filterPattern)) {
                            filteredNotes.add(note)
                        }
                    }
                }
                val filterResults = FilterResults()
                Log.i("Filtered Notes", "$filteredNotes")
                filterResults.values = filteredNotes
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notes = results?.values as ArrayList<Note>
                Log.i("noteList", "$notes")
                notifyDataSetChanged()
            }
        }
    }

    fun updateList(notesList:ArrayList<Note>){
        this.notes = notesList
        this.noteListTemp = notesList
        notifyDataSetChanged()
    }
}