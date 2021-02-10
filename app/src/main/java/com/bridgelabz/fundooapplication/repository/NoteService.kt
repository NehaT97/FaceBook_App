package com.bridgelabz.fundooapplication.repository

import android.util.Log
import com.bridgelabz.fundooapplication.model.INoteService
import com.bridgelabz.fundooapplication.model.Note
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class NoteService : INoteService {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseStore: FirebaseFirestore = FirebaseFirestore.getInstance()

   /* fun update(notes: Note){
        return firebaseStore.collection("Notes")
    }*/

    override fun getUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun DocumentId(){
    }

    override fun addNote(notes: Note): Task<DocumentReference> {
        return firebaseStore.collection("Notes").add(notes)
            .addOnCompleteListener {
                Log.i("SUCCESS", "Note has been Saved!!!")
            }.addOnFailureListener {
                Log.i("FAILED", "Note Not Saved!!!")
            }
    }

     override fun getNoteList(userId: String): Task<QuerySnapshot> {
        return firebaseStore.collection("Notes").whereEqualTo("userId", userId).get()
    }

    override fun update(notes: Note): DocumentReference {
        //firebaseStore.collection("Notes").document().update()
        val specificDocument =  firebaseStore.collection("Notes").document()
        Log.i("Document Id", specificDocument.id)
        return specificDocument
    }


}