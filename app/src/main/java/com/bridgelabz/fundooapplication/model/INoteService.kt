package com.bridgelabz.fundooapplication.model

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface INoteService {
    fun getUser(): FirebaseUser?
    fun addNote(notes: Note): Task<DocumentReference>
    fun getNoteList(userId: String): Task<QuerySnapshot>
    fun update(documentId:String, note: Note)
    fun findNoteByNoteId(noteId: String): Task<QuerySnapshot>
    fun getLimitedNoteList(
        userId: String,
        isDeleted: Boolean,
        isArchived: Boolean,
        limit: Long,
        lastCreationTimestamp: Long
    ): Task<QuerySnapshot>
}