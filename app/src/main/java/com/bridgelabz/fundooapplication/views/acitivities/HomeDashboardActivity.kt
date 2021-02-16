package com.bridgelabz.fundooapplication.views.acitivities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bridgelabz.fundooapplication.R
import com.bridgelabz.fundooapplication.R.*
import com.bridgelabz.fundooapplication.adapter.NoteAdapter
import com.bridgelabz.fundooapplication.model.Note
import com.bridgelabz.fundooapplication.repository.NoteService
import com.bridgelabz.fundooapplication.views.fragments.MainContainFragment
import com.bridgelabz.fundooapplication.views.mainactivityview.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.stream.Collectors


class HomeDashboardActivity : AppCompatActivity(), NoteAdapter.OnItemClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    lateinit var mDrawerLayout: DrawerLayout
    lateinit var mDrawerToggle: ActionBarDrawerToggle
    private val noteService: NoteService = NoteService()
    private var notesList: ArrayList<Note> = ArrayList()
    private val noteAdapter: NoteAdapter = NoteAdapter(notesList)
    private var mainContainFragment: MainContainFragment = MainContainFragment()
    private var isListView: Boolean = true
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_home_dashboard1)

        var navigationView = findViewById<NavigationView>(R.id.nav_view)
        addActionBarDrawerToggle(navigationView)
        configurePage(navigationView)

        navigateToSecondDashboard()
        checkUserIsLoggedIn()
        recyclerViewToDisplayNotesInList()
        auth = FirebaseAuth.getInstance()
    }

    private fun configurePage(navigationView: NavigationView) {
        val isTrashPage = intent.getBooleanExtra("isTrashPage", false)
        if (isTrashPage) {
            findViewById<TextView>(R.id.toolbarTitle).text = "Trash"
            navigationView.menu.findItem(R.id.trashPage).isChecked = true
            navigationView.menu.findItem(R.id.notesPage).isChecked = false
        }
    }

    private fun recyclerViewToDisplayNotesInList() {
        val recyclerView = findViewById<RecyclerView>(R.id.fragmentRecycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        noteAdapter.setOnItemClickListener(this)
        recyclerView.adapter = noteAdapter
    }

    private fun recyclerViewToDisplayNotesInGrid() {
        val recyclerView = findViewById<RecyclerView>(R.id.fragmentRecycle)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
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
        val showDeletedNotes = intent.getBooleanExtra("showDeletedNotes", false)
        if (userEmail != null) {
            noteService.getNoteList(userEmail).addOnCompleteListener {
                if (it.isSuccessful) {
                    notesList = ArrayList(it.result!!.toObjects(Note::class.java))
                        .stream()
                        .filter { note: Note -> showDeletedNotes.equals(note.isDeleted) }
                        .collect(Collectors.toList()) as ArrayList<Note>
                    //  noteAdapter.notes = notesList
                    noteAdapter.updateList(notesList)
                    noteAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val searchItem = menu?.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    //noteAdapter.notes = notesList
                    noteAdapter.updateList(notesList)

                }
                noteAdapter.filter.filter(newText)
                return true
            }
        })

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.home_toolbar_menu, menu)
        return true
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.notesView -> {
                Toast.makeText(this, "Switched view", Toast.LENGTH_LONG).show()
                isListView = if (isListView) {
                    recyclerViewToDisplayNotesInGrid()
                    item.setIcon(R.drawable.ic_grid_view_24px)
                    false
                } else {
                    recyclerViewToDisplayNotesInList()
                    item.setIcon(R.drawable.ic_list_view)
                    true
                }
            }
            R.id.search -> {
                Toast.makeText(this, "Searching", Toast.LENGTH_LONG).show()
            }
            R.id.profile -> {
                Toast.makeText(this, "Profile", Toast.LENGTH_LONG).show()
                val profileDialogue = Dialog(this)
                profileDialogue.setContentView(R.layout.profile_dialogue)
                val emailTextView = profileDialogue.findViewById<TextView>(R.id.profileEmailId)
                val usernameTextView = profileDialogue.findViewById<TextView>(R.id.profileUsername)
                if (auth.currentUser != null) {
                    usernameTextView.text = auth.currentUser?.displayName
                    emailTextView.text = auth.currentUser?.email
                }
                profileDialogue.show()
            }

        }
        return true
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

    private fun addActionBarDrawerToggle(navigationView: NavigationView) {
        val toolbar = findViewById<Toolbar?>(id.toolbar)
        setSupportActionBar(toolbar)
        navigationView.setNavigationItemSelectedListener(this)
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
        intent.putExtra("isUpdateOperation", true)
        intent.putExtra("desc", note.description)
        intent.putExtra("title", note.title)
        intent.putExtra("userId", note.userId)
        intent.putExtra("noteId", note.noteId)
        startActivity(intent)
    }

    override fun onDeleteButtonClicked(view: View?, pos: Int) {
        var note = notesList[pos]
        notesList.removeAt(pos)
        note.isDeleted = !note.isDeleted
        val ref = noteService.findNoteByNoteId(note.noteId).addOnCompleteListener {
            if (it.isComplete) {
                if (it.result!!.any()) {
                    //  noteAdapter.note = notesList
                    val docId = it.result!!.documents[0].id
                    noteService.update(docId, note)
                    noteAdapter.updateList(notesList)
                    noteAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_logout -> {
                auth.signOut()
                Toast.makeText(baseContext, "Logged Out", Toast.LENGTH_SHORT).show()
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            R.id.trashPage -> {
                Toast.makeText(baseContext, "Opening Trash Page", Toast.LENGTH_SHORT).show()
                var newIntent = Intent(this, HomeDashboardActivity::class.java)
                newIntent.putExtra("showDeletedNotes", true)
                newIntent.putExtra("isTrashPage", true)
                startActivity(newIntent)
                finish()
            }

            R.id.notesPage -> {
                Toast.makeText(baseContext, "Opening Notes Page", Toast.LENGTH_SHORT).show()
                var newIntent = Intent(this, HomeDashboardActivity::class.java)
                newIntent.putExtra("showDeletedNotes", false)
                newIntent.putExtra("isTrashPage", false)
                startActivity(newIntent)
                finish()
            }
        }
        return true
    }


}


