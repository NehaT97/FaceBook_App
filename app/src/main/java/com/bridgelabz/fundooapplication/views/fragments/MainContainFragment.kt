package com.bridgelabz.fundooapplication.views.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.bridgelabz.fundooapplication.R


class MainContainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_contain_fragment, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

        backButtonColor(toolbar)
        return view
    }


    private fun backButtonColor(toolbar: Toolbar?) {
        val white = "#ffffff"
        val whiteInt: Int = Color.parseColor(white)
        if (toolbar != null) {
            toolbar.navigationIcon?.setTint(whiteInt)
        }
    }
}

