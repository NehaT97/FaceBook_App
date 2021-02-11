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


    override fun getUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    /* fun DocumentId(){
         firebaseStore.collection("Notes").document().
     }*/

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

    override fun findNoteByNoteId(noteId: String): Task<QuerySnapshot> {
        return firebaseStore.collection("Notes").whereEqualTo("noteId", noteId).get()
    }

    override fun update(documentId:String, notes: Note) {
        firebaseStore.collection("Notes").document(documentId).set(notes)
    }
}