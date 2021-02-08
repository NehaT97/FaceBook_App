package com.bridgelabz.fundooapplication.views.acitivities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bridgelabz.fundooapplication.R
import com.bridgelabz.fundooapplication.model.Note
import com.bridgelabz.fundooapplication.repository.FirebaseRepository

class SecondDashboardActivity : AppCompatActivity() {

    val firebaseRepository: FirebaseRepository = FirebaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_dashboard)

        saveNoteByBackArrow()

    }


    private fun saveNoteByBackArrow() {
        val toolbar2 = findViewById<Toolbar>(R.id.toolbarSecond)
        toolbar2.setNavigationOnClickListener {
            Log.i("Arrow ", "Clicked")
            saveNoteToFirestore()
            val navigateBackToHomeDashboard = Intent(this, HomeDashboardActivity::class.java)
            startActivity(navigateBackToHomeDashboard)
        }
    }

    private fun saveNoteToFirestore() {
        Log.i("SAVE_NOTE ", "You In")
        val title = findViewById<EditText>(R.id.UserTitle).text.toString()
        val description = findViewById<EditText>(R.id.UserNote).text.toString()
        val emailId = firebaseRepository.getUser()?.email

        if (!emailId.isNullOrEmpty()) {
            Log.i("Title Found", "{$title}")
            Log.i("Note Found", "{$description}")

            if (title.isEmpty() || description.isEmpty()) {
                return
            }
            val note = Note(emailId, title, description)
            firebaseRepository.addNote(note)
        }
    }
}
