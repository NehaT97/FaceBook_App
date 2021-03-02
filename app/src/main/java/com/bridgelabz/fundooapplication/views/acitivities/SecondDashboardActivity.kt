package com.bridgelabz.fundooapplication.views.acitivities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bridgelabz.fundooapplication.R
import com.bridgelabz.fundooapplication.model.Note
import com.bridgelabz.fundooapplication.repository.NoteService
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
            val createdAt = intent.getLongExtra("createdAt",0)
            val title = findViewById<EditText>(R.id.NoteTitle).text.toString()
            val description = findViewById<EditText>(R.id.NoteDescription).text.toString()
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


}
