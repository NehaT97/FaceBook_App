package com.bridgelabz.fundooapplication.adapter

import android.content.Context
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


class NoteAdapter(private var notes: ArrayList<Note>) : RecyclerView.Adapter<NoteViewHolder>(), Filterable {
    private var mOnItemClickLister: OnItemClickListener? = null
    private var noteListTemp = ArrayList<Note>(notes)

    interface OnItemClickListener {
      //  abstract val context: Context?

        fun onItemClicked(view: View?, pos: Int)
        fun onDeleteButtonClicked(view: View?, pos: Int)
        fun onArchivedButtonClicked(view: View?, pos: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickLister = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val displayView = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_item_fragment, parent, false)
        Log.i("ON_Create_ViewHolder", "calling ->{$displayView}")
        return NoteViewHolder(displayView)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
        //holder.itemView.setOnClickListener()
        Log.i("ON_BIND_VIEW_HOLDER", "Calling {$position}")

        holder.itemView.setOnClickListener {
            Log.i("FinallyItem", "Working")
            mOnItemClickLister?.onItemClicked(it, position)
        }

        holder.itemView.findViewById<Button>(R.id.deleteButton).setOnClickListener {
            Log.i("Delete", "button clicked")
            mOnItemClickLister?.onDeleteButtonClicked(it, position)
        }

        holder.itemView.findViewById<Button>(R.id.archivedButton).setOnClickListener {
            Log.i("Archived","button Clicked")
            mOnItemClickLister?.onArchivedButtonClicked(it,position)

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
               // notes.clear()
              //  notes.addAll(results?.values as Collection<Note>)
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