package com.bridgelabz.fundooapplication.views.acitivities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.fundooapplication.R
import com.bridgelabz.fundooapplication.R.*
import com.bridgelabz.fundooapplication.adapter.NoteAdapter
import com.bridgelabz.fundooapplication.model.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class HomeDashboardActivity : AppCompatActivity() {
    lateinit var mDrawerLayout: DrawerLayout
    lateinit var mDrawerToggle: ActionBarDrawerToggle
    private val mDocRef: FirebaseFirestore =
        FirebaseFirestore.getInstance()
    private var notesList: List<Note> = ArrayList()
    var noteAdapter: NoteAdapter = NoteAdapter(notesList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_home_dashboard)
        addActionBarDrawerToggle()
        navigateToSecondDashboard()
        recyclerView()
    }


    private fun recyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.fragmentRecycle)
        //var Notes = ArrayList<Note>()

        recyclerView.layoutManager = LinearLayoutManager(this)
        // Notes.add(Note("Neha", "Maharashtra"))

        /*  mDocRef.collection("Notes").get().addOnCompleteListener{
              if (it.isSuccessful){
                  notesList = it.result!!.toObjects(Note::class.java)
                  noteAdapter.notes = notesList
                  noteAdapter.notifyDataSetChanged()
                 recyclerView.adapter = noteAdapter
              }else{
                  Log.d("ERROR","ERROR:${it.exception?.message}")
              }
          }*/
    }

    private fun navigateToSecondDashboard() {
        val fabButton = findViewById<FloatingActionButton>(id.addNotesFab)
        fabButton.setOnClickListener {
            Log.i("ButtonReference", "reference of button found {$fabButton}")
            Toast.makeText(applicationContext, "YOU CLICKED!!!!", Toast.LENGTH_LONG).show()
            val navigateToSecondDashboardActivity =
                Intent(this, SecondDashboardActivity::class.java)
            startActivity(navigateToSecondDashboardActivity)
        }
    }

    private fun addActionBarDrawerToggle() {
        val toolbar = findViewById<Toolbar?>(id.toolbar)
        mDrawerLayout = findViewById(id.drawer_layout)
        mDrawerToggle = ActionBarDrawerToggle(
            this,
            mDrawerLayout,
            toolbar,
            string.drawer_open,
            string.drawer_close
        )
        mDrawerToggle.syncState()
    }
}