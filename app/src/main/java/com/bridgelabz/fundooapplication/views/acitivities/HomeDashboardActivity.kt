package com.bridgelabz.fundooapplication.views.acitivities

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
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
    private lateinit var profileDialogue : Dialog
    private lateinit var userProfile: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_home_dashboard1)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        addActionBarDrawerToggle(navigationView)
        configurePage(navigationView)

        navigateToSecondDashboard()
        checkUserIsLoggedIn()
        recyclerViewToDisplayNotesInList()
        auth = FirebaseAuth.getInstance()

    }

    private fun configurePage(navigationView: NavigationView) {
        val isTrashPage = intent.getBooleanExtra("isTrashPage", false)
        val isArchivedPage = intent.getBooleanExtra("isArchivedPage", false)
        if (isTrashPage) {
            findViewById<TextView>(R.id.toolbarTitle).text = "Trash"
            navigationView.menu.findItem(R.id.trashPage).isChecked = true
            navigationView.menu.findItem(R.id.notesPage).isChecked = false
            navigationView.menu.findItem(R.id.archivedPage).isChecked = false
        }
        if (isArchivedPage) {
            findViewById<TextView>(R.id.toolbarTitle).text = "Archived"
            navigationView.menu.findItem(R.id.trashPage).isChecked = false
            navigationView.menu.findItem(R.id.notesPage).isChecked = false
            navigationView.menu.findItem(R.id.archivedPage).isChecked = true
        }
    }

    private fun recyclerViewToDisplayNotesInList() {
        val recyclerView = findViewById<RecyclerView>(id.fragmentRecycle)
        recyclerView.layoutManager = LinearLayoutManager(this)
        noteAdapter.setOnItemClickListener(this)
        recyclerView.adapter = noteAdapter
    }

    private fun recyclerViewToDisplayNotesInGrid() {
        val recyclerView = findViewById<RecyclerView>(id.fragmentRecycle)
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
        val showDeletedNotes = intent.getBooleanExtra("isTrashPage", false)
        val showArchivedNotes = intent.getBooleanExtra("isArchivedPage", false)
        if (userEmail != null) {
            noteService.getNoteList(userEmail).addOnCompleteListener {
                if (it.isSuccessful) {
                    notesList = ArrayList(it.result!!.toObjects(Note::class.java))
                        .stream()
                        .filter { note: Note -> showDeletedNotes.equals(note.isDeleted)   }
                        .filter { note: Note  -> showArchivedNotes.equals(note.isArchived)}
                        .collect(Collectors.toList()) as ArrayList<Note>
                    noteAdapter.updateList(notesList)
                    noteAdapter.notifyDataSetChanged()
                }
            }
          //  Log.i("Notes", noteService.getNoteList2(userEmail).toString())
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
                    item.setIcon(R.drawable.ic_list_view)
                    false
                } else {
                    recyclerViewToDisplayNotesInList()
                    item.setIcon(R.drawable.ic_grid_view_24px)
                    true
                }
            }
            R.id.search -> {
                Toast.makeText(this, "Searching", Toast.LENGTH_LONG).show()
            }
            R.id.profile -> {
                Toast.makeText(this, "Profile", Toast.LENGTH_LONG).show()
                profileDialogue = Dialog(this)
                profileDialogue.setContentView(R.layout.profile_dialogue)
                val emailTextView = profileDialogue.findViewById<TextView>(R.id.profileEmailId)
                val usernameTextView = profileDialogue.findViewById<TextView>(R.id.profileUsername)
                if (auth.currentUser != null) {
                    usernameTextView.text = auth.currentUser?.displayName
                    emailTextView.text = auth.currentUser?.email
                }

                profileDialogue.show()
                userProfile = profileDialogue.findViewById<ImageView>(R.id.userProfile)
                userProfile.setOnClickListener {
                    Toast.makeText(this, "U clicked", Toast.LENGTH_SHORT).show()
                    selectImageForProfile()
                }

            }

        }
        return true
    }

    private fun selectImageForProfile() {
        val options =
            arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Add profile picture")

        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Take Photo" -> {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePicture, 0)
                }
                options[item] == "Choose from Gallery" -> {
                    val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto, 1)
                }
                options[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedImage: Bitmap? = data.extras!!["data"] as Bitmap?
                    userProfile.setImageBitmap(selectedImage)
                }

                1 -> if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedImage: Uri? = data.data
                    val filePathColumn =
                        arrayOf(MediaStore.Images.Media.DATA)
                    if (selectedImage != null) {
                        val cursor: Cursor? = contentResolver.query(
                            selectedImage,
                            filePathColumn, null, null, null
                        )
                        if (cursor != null) {
                            cursor.moveToFirst()
                            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                            val picturePath: String = cursor.getString(columnIndex)
                            userProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath))
                            cursor.close()
                        }
                    }
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
        val note = notesList[pos]
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
                    if (note.isDeleted) {
                        Toast.makeText(this, "Note is Deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Note is Restored", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onArchivedButtonClicked(view: View?, pos: Int) {
        val note = notesList[pos]
        notesList.removeAt(pos)
        note.isArchived = !note.isArchived
        val ref = noteService.findNoteByNoteId(note.noteId).addOnCompleteListener {
            if (it.isComplete) {
                if (it.result!!.any()) {
                    //  noteAdapter.note = notesList
                    val docId = it.result!!.documents[0].id
                    noteService.update(docId, note)
                    noteAdapter.updateList(notesList)
                    noteAdapter.notifyDataSetChanged()
                    if (note.isArchived) {
                        Toast.makeText(this, "Note is Archived", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Note is Restored", Toast.LENGTH_SHORT).show()
                    }
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
                val newIntent = Intent(this, HomeDashboardActivity::class.java)
                newIntent.putExtra("isTrashPage", true)
                newIntent.putExtra("isArchivedPage", false)
                startActivity(newIntent)
                finish()
            }

            R.id.notesPage -> {
                Toast.makeText(baseContext, "Opening Notes Page", Toast.LENGTH_SHORT).show()
                val newIntent = Intent(this, HomeDashboardActivity::class.java)
                newIntent.putExtra("isTrashPage", false)
                newIntent.putExtra("isArchivedPage", false)
                startActivity(newIntent)
                finish()
            }

            R.id.archivedPage -> {
                Toast.makeText(baseContext, "Opening Archived Page", Toast.LENGTH_SHORT).show()
                val newIntent = Intent(this, HomeDashboardActivity::class.java)
                newIntent.putExtra("isTrashPage", false)
                newIntent.putExtra("isArchivedPage", true)
                startActivity(newIntent)
                finish()

            }
        }
        return true
    }
}


