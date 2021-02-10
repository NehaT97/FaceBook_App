package com.bridgelabz.fundooapplication.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.fundooapplication.R
import com.bridgelabz.fundooapplication.model.Note
import com.bridgelabz.fundooapplication.views.acitivities.SecondDashboardActivity

class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private val description = view.findViewById<TextView>(R.id.description)
    private val title = view.findViewById<TextView>(R.id.noteTitle)
    lateinit var context:Context
    lateinit var activity: Activity

  /*  constructor(context: Context,itemView: View){
        super.itemView
        itemView.setOnClickListener {
            Log.i("Event called", "On Clicked!!!!!!!!!!!!!!")

        }
    }*/

    fun bind(note: Note) {
        description.text = note.description
        title.text = note.title

    }

   /* override fun onClick(view : View?) {
        //val Intent =  Intent(context,SecondDashboardActivity::class.java)
    }*/
}