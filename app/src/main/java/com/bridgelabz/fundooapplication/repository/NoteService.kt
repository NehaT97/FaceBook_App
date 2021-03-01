package com.bridgelabz.fundooapplication.repository

import android.util.Log
import android.widget.Toast
import com.bridgelabz.fundooapplication.model.INoteService
import com.bridgelabz.fundooapplication.model.Note
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.messaging.FirebaseMessaging
import java.util.stream.Collectors

class NoteService : INoteService {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseStore: FirebaseFirestore = FirebaseFirestore.getInstance()



    override fun getUser(): FirebaseUser? {
        return firebaseAuth.currentUser
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

    override fun getLimitedNoteList(userId: String, isDeleted: Boolean, isArchived: Boolean, lastVisibleSnapshot: DocumentSnapshot?, pageSize: Long): Task<QuerySnapshot> {
        if (lastVisibleSnapshot == null) {
            return firebaseStore.collection("Notes").whereEqualTo("userId",userId)
                .whereEqualTo("deleted", isDeleted)
                .whereEqualTo("archived", isArchived)
                .startAt()
                .orderBy("title")
                .limit(pageSize).get()
        }
        return firebaseStore.collection("Notes").whereEqualTo("userId",userId)
            .whereEqualTo("deleted", isDeleted)
            .whereEqualTo("archived", isArchived)
            .orderBy("title")
            .startAfter(lastVisibleSnapshot)
            .limit(pageSize).get()
    }

    override fun findNoteByNoteId(noteId: String): Task<QuerySnapshot> {
        return firebaseStore.collection("Notes").whereEqualTo("noteId", noteId).get()
    }


    override fun update(documentId:String, notes: Note) {
        firebaseStore.collection("Notes").document(documentId).set(notes)
    }
}