package com.bridgelabz.fundooapplication.views.acitivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bridgelabz.fundooapplication.R

class SecondDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_dashboard)
    }

    /* override fun onStart() {
         super.onStart()
         val addNoteFragment = AddNoteFragment()
         supportFragmentManager.beginTransaction()
             .add(R.id.Second_Dashboard_activity, addNoteFragment).commit()
     }*/
}