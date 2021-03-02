package com.bridgelabz.fundooapplication.views.acitivities

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
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
import com.bridgelabz.fundooapplication.reminderSettings.view.notification.NotificationHelper
import com.bridgelabz.fundooapplication.reminderSettings.view.reminder.ReminderHelper
import com.bridgelabz.fundooapplication.repository.NoteService
import com.bridgelabz.fundooapplication.views.fragments.MainContainFragment
import com.bridgelabz.fundooapplication.views.mainactivityview.MainActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import java.io.InputStream
import kotlin.properties.Delegates


class HomeDashboardActivity : AppCompatActivity(), NoteAdapter.OnItemClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var progressBar: ProgressBar
    lateinit var mDrawerLayout: DrawerLayout
    lateinit var mDrawerToggle: ActionBarDrawerToggle
    lateinit var notificationHelper: NotificationHelper
    private val noteService: NoteService = NoteService()
    private var notesList: ArrayList<Note> = ArrayList()
    //private val noteAdapter: NoteAdapter = NoteAdapter(notesList)
    private lateinit var noteAdapter: NoteAdapter
    private var mainContainFragment: MainContainFragment = MainContainFragment()
    private var isListView: Boolean = true
    private lateinit var auth: FirebaseAuth
    private lateinit var profileDialogue : Dialog
    private lateinit var userProfile: ImageView
    private val OPEN_CAMERA = 0
    private val OPEN_GALARY = 1
    private val TAKE_PHOTO = "Take Photo";
    private val CHOOSE_FROM_GALARY = "Choose from Gallery";
    private val CANCEL = "Cancel";
    private lateinit var notesDocumentSnapshots: List<DocumentSnapshot>

    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var totalNotesCount: Int = 0

    private var isScrolling:Boolean = false
    private var visibleItems by Delegates.notNull<Int>()
    private var totalItems by Delegates.notNull<Int>()
    private var scrollOutItems by Delegates.notNull<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_home_dashboard1)

        val isTrashPage = intent.getBooleanExtra("isTrashPage", false)
        val isArchivedPage = intent.getBooleanExtra("isArchivedPage", false)

        noteAdapter = NoteAdapter(notesList, isTrashPage, isArchivedPage)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        addActionBarDrawerToggle(navigationView)
        configurePage(navigationView)
        navigateToSecondDashboard()
        recyclerViewToDisplayNotesInList()
        auth = FirebaseAuth.getInstance()
        notificationHelper =
            NotificationHelper(
                this
            )
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
        loadNotesData(true, 1, 9)
        noteAdapter.updateList(notesList)
        recyclerView.adapter = noteAdapter

        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (RecyclerView.SCROLL_STATE_IDLE == newState && !recyclerView.canScrollVertically(1)){
                    Log.i("Scroll","Fetching Data")
                    fetchData()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.i("Scroll","onScrolled: Called")
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                visibleItems = layoutManager.childCount
                Log.i("Scroll","visibleItems: $visibleItems")
                totalItems = layoutManager.itemCount
                Log.i("Scroll","totalItems: $totalItems")
                scrollOutItems = layoutManager.findFirstVisibleItemPosition()
                Log.i("Scroll","scrollOutItems: $scrollOutItems")

            }
        })
    }

    private fun fetchData() {
        val handler:Handler = Handler()
         progressBar = findViewById(R.id.progressBar1)
        progressBar.visibility = View.VISIBLE
        handler.postDelayed({
            loadNotesData(false, 2, 9)
            progressBar.visibility = View.GONE
        },3000)
    }

    private fun recyclerViewToDisplayNotesInGrid() {
        val recyclerView = findViewById<RecyclerView>(id.fragmentRecycle)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        noteAdapter.setOnItemClickListener(this)
        recyclerView.adapter = noteAdapter
    }

    private fun loadNotesData(isInitLoad:Boolean, pageNo:Long, pageSize: Long) {
        val user = noteService.getUser()
        val userEmail = user?.email
        val showDeletedNotes = intent.getBooleanExtra("isTrashPage", false)
        val showArchivedNotes = intent.getBooleanExtra("isArchivedPage", false)
        val lastCreationTimeStamp:Long = if(notesList.isEmpty()) 0
                                         else notesList.last().createdAt
        if (userEmail != null) {
            noteService.getLimitedNoteList(userEmail, showDeletedNotes, showArchivedNotes, pageSize,lastCreationTimeStamp).addOnCompleteListener {
                if (it.isSuccessful) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if (isInitLoad) {
                            notesList = ArrayList(it.result!!.toObjects(Note::class.java))
                        } else {
                            notesList.addAll(ArrayList(it.result!!.toObjects(Note::class.java)))
                        }
                    }
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
        val options = resources.getStringArray(R.array.image_select_options)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("ADD PROFILE PICTURE")
        builder.setItems(options) { dialog, item ->
            when {
                 TAKE_PHOTO == options[item] -> {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePicture, OPEN_CAMERA)
                }
                CHOOSE_FROM_GALARY == options[item] -> {
                    val pickPhoto =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto, OPEN_GALARY)
                }
                CANCEL == options[item] -> {
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
                     OPEN_CAMERA -> if (resultCode == Activity.RESULT_OK && data != null) {
                         val selectedImage: Bitmap? = data.extras!!["data"] as Bitmap?
                         userProfile.setImageBitmap(selectedImage)
                     }

                     OPEN_GALARY -> if (resultCode == Activity.RESULT_OK && data != null) {
                         val inputStream: InputStream? = contentResolver.openInputStream(data.data!!)
                         val bitmap:Bitmap = BitmapFactory.decodeStream(inputStream)
                         userProfile.setImageBitmap(bitmap)

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
        intent.putExtra("createdAt",note.createdAt)
        startActivity(intent)
    }

    override fun onDeleteButtonClicked(view: View?, pos: Int) {
        val note = notesList[pos]
        notesList.removeAt(pos)
        note.isDeleted = !note.isDeleted
        val ref = noteService.findNoteByNoteId(note.noteId).addOnCompleteListener {
            if (it.isComplete) {
                if (it.result!!.any()) {
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

    override fun onNotificationButtonClicked(
        view: View?,
        pos: Int,
        noteTitle: String,
        noteDescription: String
    ){
        selectReminderDateAndTimeDialog(noteTitle,noteDescription)
    }

    private fun selectReminderDateAndTimeDialog(noteTitle: String, noteDescription: String) {
        val reminderDialogue = Dialog(this)
        reminderDialogue.setContentView(layout.reminder_dialogue1)
        reminderDialogue.show()
        val btnDate = reminderDialogue.findViewById<Button>(R.id.btn_date)
        val btnTime = reminderDialogue.findViewById<Button>(R.id.btn_time)
        val inDate = reminderDialogue.findViewById<EditText>(R.id.in_date)
        val inTime = reminderDialogue.findViewById<EditText>(R.id.in_time)
        val cancelButton = reminderDialogue.findViewById<Button>(R.id.cancelButton)
        val addButton = reminderDialogue.findViewById<Button>(R.id.saveButton)
        val reminderHelper: ReminderHelper =
            ReminderHelper()

        btnDate.setOnClickListener {
            Toast.makeText(this, "Select Date", Toast.LENGTH_SHORT).show()
            reminderHelper.selectDate(this,inDate)
        }

        btnTime.setOnClickListener {
            Toast.makeText(this, "Select Time", Toast.LENGTH_SHORT).show()
            reminderHelper.selectTime(this,inTime)

        }

        addButton.setOnClickListener {
            Toast.makeText(this, "Reminder Is set", Toast.LENGTH_SHORT).show()
            val dateString = reminderDialogue.findViewById<EditText>(R.id.in_date).text.toString()
            val timeString = reminderDialogue.findViewById<EditText>(R.id.in_time).text.toString()
            if (dateString.isNullOrEmpty()) {
                Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (timeString.isNullOrEmpty()) {
                Toast.makeText(this, "Please select time", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            reminderHelper.setAlarm(this, dateString.trim(), timeString,noteTitle,noteDescription)
            reminderDialogue.dismiss()
        }

        cancelButton.setOnClickListener {
            Toast.makeText(this, "Reminder Not Saved", Toast.LENGTH_SHORT).show()
            reminderDialogue.dismiss()
        }
    }

    override fun onArchivedButtonClicked(view: View?, pos: Int) {
        val note = notesList[pos]
        notesList.removeAt(pos)
        note.isArchived = !note.isArchived
        val ref = noteService.findNoteByNoteId(note.noteId).addOnCompleteListener {
            if (it.isComplete) {
                if (it.result!!.any()) {
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean
    {
        when (item.itemId) {
            R.id.nav_logout -> {
                auth.signOut()
                Toast.makeText(baseContext, "Logged Out", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
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


