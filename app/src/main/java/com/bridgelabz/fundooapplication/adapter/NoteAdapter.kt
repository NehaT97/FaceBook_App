package com.bridgelabz.fundooapplication.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.fundooapplication.R
import com.bridgelabz.fundooapplication.model.Note


class NoteAdapter(var notes: List<Note>) : RecyclerView.Adapter<NoteViewHolder>() {
    private var mOnItemClickLister: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClicked(view: View?, pos: Int)
        fun onDeleteButtonClicked(view: View?, pos: Int)
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

    }
}