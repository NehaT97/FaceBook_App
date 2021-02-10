package com.bridgelabz.fundooapplication.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.bridgelabz.fundooapplication.R

class Note_Item_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       // return super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.note_item_fragment, container, false)
       /* val cardView = view.findViewById<CardView>(R.id.cardView)
        Log.i("CARD_FOUND", "$cardView")
        val result = cardView.isClickable
        println("card_result $result")
        cardView.setOnClickListener {
            Log.i("CardClick", "${cardView}")
        }*/
        return view
    }
}