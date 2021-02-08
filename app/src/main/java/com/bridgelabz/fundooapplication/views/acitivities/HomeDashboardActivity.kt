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
import com.bridgelabz.fundooapplication.repository.FirebaseRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeDashboardActivity : AppCompatActivity() {
    lateinit var mDrawerLayout: DrawerLayout
    lateinit var mDrawerToggle: ActionBarDrawerToggle
    val firebaseRepository: FirebaseRepository = FirebaseRepository()
    private var notesList: List<Note> = ArrayList()
    private val noteAdapter: NoteAdapter = NoteAdapter(notesList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_home_dashboard)
        addActionBarDrawerToggle()
        navigateToSecondDashboard()
        checkUserIsLoggedIn()
        recyclerViewToDisplayNotes()
    }

    private fun recyclerViewToDisplayNotes() {
        val recyclerView = findViewById<RecyclerView>(R.id.fragmentRecycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = noteAdapter
    }

    private fun checkUserIsLoggedIn() {
        if (firebaseRepository.getUser() != null)
            loadNotesData()
    }

    private fun loadNotesData() {
        val user = firebaseRepository.getUser()
        val userEmail = user?.email
        if (userEmail != null) {
            firebaseRepository.getNoteList(userEmail).addOnCompleteListener{
                if (it.isSuccessful) {
                    notesList = it.result!!.toObjects(Note::class.java)
                    noteAdapter.notes = notesList
                    noteAdapter.notifyDataSetChanged()
                }
            }
        }
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