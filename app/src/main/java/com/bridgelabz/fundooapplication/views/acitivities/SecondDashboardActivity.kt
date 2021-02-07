package com.bridgelabz.fundooapplication.views.acitivities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bridgelabz.fundooapplication.R
import com.bridgelabz.fundooapplication.model.Note
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SecondDashboardActivity : AppCompatActivity() {

    private val mDocRef: DocumentReference =
        FirebaseFirestore.getInstance().collection("Notes").document()

    private val TITLE_KEY: String = "title"
    private val DESCRIPTION_KEY: String = "note"
    private val NOTE_ID: String = "id"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_dashboard)

        saveNoteByBackArrow()
        retrievedNoteFromFirestore()

    }

    private fun retrievedNoteFromFirestore() {
        val resultData = findViewById<TextView>(R.id.textView_displayData)
        mDocRef.collection("Notes").get().addOnCompleteListener {
            val result1 = StringBuffer()
            if (it.isSuccessful) {
                Log.i("Retrieved", "Document retrieved successfuly!!!")
                for (document in it.result!!)
                    result1.append(document.data.getValue("TITLE_KEY")).append("\n").append(document.data.getValue("DESCRIPTION_KEY")).append("\n\n")
                Log.i("____________________________", "________________________________________")
                Log.i("******RESULT*****", "{$resultData.text = $result1}")
                Log.i("____________________________", "________________________________________")
            }
        }
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
        // val noteId = mDocRef.id
        val userTitle = findViewById<EditText>(R.id.UserTitle).text.toString()
        val userDescription = findViewById<EditText>(R.id.UserNote).text.toString()

        Log.i("Title Found", "{$userTitle}")
        Log.i("Note Found", "{$userDescription}")

         if (userTitle.isEmpty() || userDescription.isEmpty()) {
             return
         }
         val dataToSave: HashMap<String, String> = HashMap()
         dataToSave[TITLE_KEY] = userTitle
         dataToSave[DESCRIPTION_KEY] = userDescription
        // dataToSave[NOTE_ID] = noteId
         mDocRef.set(dataToSave).addOnSuccessListener {
             Log.i("SUCCESS", "Documents has been Saved!!!")
         }.addOnFailureListener {
             Log.i("FAILED", "Documents Not Saved!!!")
         }
    }
}
