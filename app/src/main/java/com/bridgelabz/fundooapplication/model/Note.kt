package com.bridgelabz.fundooapplication.model

import java.time.ZonedDateTime

data class Note(
    val noteId: String = " ",
    val userId: String = " ",
    val title: String = " ",
    val description: String = " ",
<<<<<<< HEAD
    val createdAt: Long = System.currentTimeMillis(),
=======
    val createdAt:Long = System.currentTimeMillis(),
>>>>>>> Development
    var isDeleted: Boolean = false,
    var isArchived:Boolean = false
) {
}