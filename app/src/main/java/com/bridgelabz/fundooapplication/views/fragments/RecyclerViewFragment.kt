package com.bridgelabz.fundooapplication.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.fundooapplication.R


class RecyclerViewFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recycler_view, container, false)
      /*  val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView1)
        recyclerView.layoutManager = LinearLayoutManager(context)*/
        return view
    }

}
