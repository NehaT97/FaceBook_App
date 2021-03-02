package com.bridgelabz.fundooapplication.model
data class Note(
    val noteId: String = " ",
    val userId: String = " ",
    val title: String = " ",
    val description: String = " ",
    val createdAt: Long = System.currentTimeMillis(),
    var isDeleted: Boolean = false,
    var isArchived:Boolean = false
) {
}