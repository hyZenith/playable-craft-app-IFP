package com.example.myapplication.data.model

/**
 * Lifecycle archive status of a study or pedagogical note.
 */
enum class NoteStatus(val label: String) {
    ACTIVE("Active"),
    ARCHIVED("Archived")
}

/**
 * Representation of an individual note taken on a page or dock.
 *
 * @property id Unique identifier of the note.
 * @property title Concise title or header.
 * @property content Full body text or thoughts.
 * @property pageContext Screen context where the note was taken (e.g., "Lesson Plans", "Simulations").
 * @property status Active or archived state.
 * @property createdAtFormatted Formatted creation timestamp (e.g., "Oct 6, 2026").
 */
data class NoteItem(
    val id: String,
    val title: String,
    val content: String,
    val pageContext: String = "General",
    val status: NoteStatus = NoteStatus.ACTIVE,
    val createdAtFormatted: String = ""
)
