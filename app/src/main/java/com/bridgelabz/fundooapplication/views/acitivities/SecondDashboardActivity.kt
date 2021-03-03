package com.bridgelabz.fundooapplication.views.acitivities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import com.bridgelabz.fundooapplication.R
import com.bridgelabz.fundooapplication.model.Note
import com.bridgelabz.fundooapplication.repository.NoteService
import com.bridgelabz.fundooapplication.service.GoogleMapService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SecondDashboardActivity : AppCompatActivity() {

    val noteService: NoteService = NoteService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_dashboard)
        receivingDataFromIntent()
        saveNoteByBackArrow()
    }

    private fun receivingDataFromIntent() {
        val title = intent.getStringExtra("title")
        val desc = intent.getStringExtra("desc")
        if (!title.isNullOrEmpty() || !desc.isNullOrEmpty()) {
            findViewById<EditText>(R.id.NoteTitle).setText(title)
            findViewById<EditText>(R.id.NoteDescription).setText(desc)
        }
        Log.i("NoteData", "title : $title, desc: $desc")
    }

    private fun saveNoteByBackArrow() {
        val toolbar2 = findViewById<Toolbar>(R.id.toolbarSecond)
        toolbar2.setNavigationOnClickListener {
            Log.i("Arrow ", "Clicked")
            saveNote()
            val navigateBackToHomeDashboard = Intent(this, HomeDashboardActivity::class.java)
            startActivity(navigateBackToHomeDashboard)
            finish()
        }
    }

    private fun saveNote() {
        val isUpdateOperation= intent.getBooleanExtra("isUpdateOperation", false)
        if (isUpdateOperation) {
            val noteId = intent.getStringExtra("noteId")
            val userId = intent.getStringExtra("userId")
            val title = findViewById<EditText>(R.id.NoteTitle).text.toString()
            val description = findViewById<EditText>(R.id.NoteDescription).text.toString()
            val createdAt = intent.getLongExtra("createdAt",0)
            val note= Note(noteId, userId, title, description,createdAt)
            val id = noteService.findNoteByNoteId(noteId).addOnCompleteListener {
                if (it.isComplete) {
                    val isResult = it.result!!.any()
                    if (isResult) {
                        val id = it.result!!.documents[0].id
                        noteService.update(id, note)
                    }
                }
            }
            Log.i("Update note operation", "$note")
            Log.i("Updated Date Check","$createdAt")
        } else {
            val title = findViewById<EditText>(R.id.NoteTitle).text.toString()
            val description = findViewById<EditText>(R.id.NoteDescription).text.toString()
            val emailId = noteService.getUser()?.email
            if (!emailId.isNullOrEmpty() && title.isNotEmpty()) {
                val note = Note(UUID.randomUUID().toString(), emailId, title, description)
                noteService.addNote(note)
            }
        }
    }

    @Override
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.second_toolbar_menu, menu)
        return true
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //return super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.setLocation -> {
                setLocationDialogue()
                return true
            }

            R.id.setReminder -> {
                Toast.makeText(this, "Set Reminder", Toast.LENGTH_LONG).show()
                return true
            }
        }
        return true
    }

    private fun setLocationDialogue() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Reminder")
        val view = layoutInflater.inflate(R.layout.location_dialogue, null)
        builder.setView(view)
        builder.show()
        val selectLocationValue = view.findViewById<EditText>(R.id.editLocation)
        selectLocationValue.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i("Search OnTextChanged", "$s, $start, $before, $count")
                val call = GoogleMapService.getInstance()
                    .getSearchLocationsByName(
                        "AIzaSyDEXFnmPN3K-vSfF5HGGASkF0TUvFGQsxg",
                        s.toString(),
                        "textquery"
                    )
                call.enqueue(object : Callback<Any> {
                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        Log.e("Request Failed", "${t.message}", t)
                    }

                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        Log.i("Response", "${response.body()}")
                    }
                })
            }

        })
    }

}
