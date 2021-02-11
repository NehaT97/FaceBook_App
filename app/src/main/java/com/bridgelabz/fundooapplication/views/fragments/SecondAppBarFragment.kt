package com.bridgelabz.fundooapplication.views.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.bridgelabz.fundooapplication.R
import com.bridgelabz.fundooapplication.views.acitivities.HomeDashboardActivity


class SecondAppBarFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_second_app_bar, container, false)
        val toolbarSecond = view.findViewById<Toolbar>(R.id.toolbarSecond)

        setBackNavigationIconColor(toolbarSecond)
        navigateToHomeDashboard(toolbarSecond)
        return view
    }

    private fun navigateToHomeDashboard(toolbarSecond: Toolbar?) {
        toolbarSecond?.setNavigationOnClickListener {
            Log.i("Great", "You Clicked")
            val navigateBackToHomeDashboard = Intent(context, HomeDashboardActivity::class.java)
            startActivity(navigateBackToHomeDashboard)
        }
    }

    private fun setBackNavigationIconColor(toolbarSecond: Toolbar) {
        val white = "#ffffff"
        val whiteInt: Int = Color.parseColor(white)
        toolbarSecond.navigationIcon?.setTint(whiteInt)
        toolbarSecond.title = "Note"
    }
}