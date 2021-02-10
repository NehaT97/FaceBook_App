package com.bridgelabz.fundooapplication.views.acitivities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.bridgelabz.fundooapplication.repository.NoteService
import com.bridgelabz.fundooapplication.views.fragments.MainContainFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.DocumentSnapshot


class HomeDashboardActivity : AppCompatActivity(), NoteAdapter.OnItemClickListener {
    lateinit var mDrawerLayout: DrawerLayout
    lateinit var mDrawerToggle: ActionBarDrawerToggle
    private val noteService: NoteService = NoteService()
    private var notesList: List<Note> = ArrayList()
    private val noteAdapter: NoteAdapter = NoteAdapter(notesList)
    private var mainContainFragment: MainContainFragment = MainContainFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_home_dashboard1)
        addActionBarDrawerToggle()
        navigateToSecondDashboard()
        checkUserIsLoggedIn()
        recyclerViewToDisplayNotes()
        //editNote()
    }

    /* private fun editNote() {
         val cardView = findViewById<RecyclerView>(R.id.fragmentRecycle)
         cardView.setOnClickListener {
             val navigateToSecondDashboardActivity =
                 Intent(this, SecondDashboardActivity::class.java)
             startActivity(navigateToSecondDashboardActivity)

         }
     }
 */
    private fun recyclerViewToDisplayNotes() {
        val recyclerView = findViewById<RecyclerView>(R.id.fragmentRecycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        noteAdapter.setOnItemClickListener(this)
        recyclerView.adapter = noteAdapter


    }

    private fun checkUserIsLoggedIn() {
        if (noteService.getUser() != null)
            loadNotesData()
    }

    private fun loadNotesData() {
        val user = noteService.getUser()
        val userEmail = user?.email
        if (userEmail != null) {
            noteService.getNoteList(userEmail).addOnCompleteListener {
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
        //setSupportActionBar(toolbar)
        var navigationView = findViewById<NavigationView>(R.id.nav_view);

        navigationView.menu.getItem(0).isCheckable = true
        navigationView.menu.getItem(1).isCheckable = true
        navigationView.menu.getItem(0).isChecked = true
        mDrawerLayout = findViewById(id.drawer_layout)
        mDrawerToggle = ActionBarDrawerToggle(
            this,
            mDrawerLayout,
            toolbar,
            string.drawer_open,
            string.drawer_close
        )

        val white = "#ffffff"
        val whiteInt: Int = Color.parseColor(white)
        mDrawerToggle.drawerArrowDrawable.color = whiteInt
        mDrawerToggle.syncState()
    }

    override fun onItemClicked(view: View?, pos: Int) {
        val intent = Intent(this, SecondDashboardActivity::class.java)
        val note = notesList[pos];
        intent.putExtra("desc", note.description)
        intent.putExtra("title", note.title)
        startActivity(intent)
    }
}