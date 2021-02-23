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

    /*override fun getNoteList2(userId: String): MutableList<Note>? {
        val snapShots = firebaseStore.collection("Notes").whereEqualTo("userId", userId).get().addOnCanceledListener {
            if (i.isComplete) {
                val documents = it.result!!.documents
                return documents.stream().map {
                    val dataMap = it.data
                    val noteId = it.id
                    val usersId = dataMap?.get("userId").toString()
                    val title = dataMap?.get("title").toString()
                    val description = dataMap?.get("description").toString()
                    val isDeleted = dataMap?.get("isDeleted") as Boolean
                    val isArchived = dataMap["isArchived"] as Boolean
                    return@map Note(noteId, usersId, title, description, isDeleted, isArchived)
                }.collect(Collectors.toList())
            }
        }
        return ArrayList()
    }*/

    override fun findNoteByNoteId(noteId: String): Task<QuerySnapshot> {
        return firebaseStore.collection("Notes").whereEqualTo("noteId", noteId).get()
    }

    override fun update(documentId:String, notes: Note) {
        firebaseStore.collection("Notes").document(documentId).set(notes)
    }
}