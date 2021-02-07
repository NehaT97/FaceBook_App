package com.bridgelabz.fundooapplication.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.fundooapplication.R
import com.bridgelabz.fundooapplication.adapter.NoteAdapter
import com.bridgelabz.fundooapplication.model.Note
import com.bridgelabz.fundooapplication.viewModel.MainContainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.ArrayList


class MainContainFragment : Fragment(){

    lateinit var fabButton :FloatingActionButton

    private lateinit var viewModel: MainContainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_contain_fragment, container, false)
       // displayNotes(view)
        return view
    }

    /*private fun displayNotes(view: View) {
        var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView) as RecyclerView
        val Notes = ArrayList<Note>()
        recyclerView.layoutManager = LinearLayoutManager(activity)
        Notes.add(Note("Neha", "Maharashtra"))
        Notes.add(Note("Neha", "Maharashtra"))
        Notes.add(Note("Neha", "Maharashtra"))
        Notes.add(Note("Neha", "Maharashtra"))
        Notes.add(Note("Neha", "Maharashtra"))
        Notes.add(Note("Neha", "Maharashtra"))
        Notes.add(Note("Neha", "Maharashtra"))
        Notes.add(Note("Neha", "Maharashtra"))

        val adapter = NoteAdapter(Notes)
        recyclerView.adapter = adapter
    }*/

}