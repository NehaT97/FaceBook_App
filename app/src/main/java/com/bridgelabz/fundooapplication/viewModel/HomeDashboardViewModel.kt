package com.bridgelabz.fundooapplication.viewModel

import androidx.lifecycle.ViewModel
import com.bridgelabz.fundooapplication.model.Note
import com.bridgelabz.fundooapplication.repository.NoteService

class HomeDashboardViewModel(var noteService: NoteService) : ViewModel() {

    public var notes: List<Note> = getAllNotes()

    fun getAllNotes(): List<Note> {
        val user = noteService.getUser()
        val userEmail = user?.email
        if (userEmail != null) {
            val it = noteService.getNoteList(userEmail)
            return it.result!!.toObjects(Note::class.java)
        }
        return ArrayList()
    }

}