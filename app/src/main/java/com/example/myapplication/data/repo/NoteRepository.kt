package com.example.myapplication.data.repo

import com.example.myapplication.data.model.NoteItem
import com.example.myapplication.data.model.NoteStatus
import kotlinx.coroutines.delay

/**
 * Repository interface for managing teacher ideas, explanation tips, and reminders.
 */
interface NoteRepository {
    /**
     * Lists notes filtered by optional text query, page context, and archive status.
     */
    suspend fun list(
        query: String? = null,
        pageContext: String? = null,
        status: NoteStatus = NoteStatus.ACTIVE
    ): Result<List<NoteItem>>

    /**
     * Creates and stores a new note.
     */
    suspend fun create(
        title: String,
        content: String,
        pageContext: String
    ): Result<NoteItem>

    /**
     * Toggles note status between active and archived.
     */
    suspend fun toggleArchive(id: String): Result<Unit>

    /**
     * Deletes a note by its identifier.
     */
    suspend fun delete(id: String): Result<Unit>
}

/**
 * In-memory repository implementation simulating note creation and querying.
 */
class FakeNoteRepository : NoteRepository {

    private val storedNotes = mutableListOf<NoteItem>()

    override suspend fun list(
        query: String?,
        pageContext: String?,
        status: NoteStatus
    ): Result<List<NoteItem>> {
        delay(250)
        val sanitizedQuery = query?.trim()?.lowercase().orEmpty()

        val filtered = storedNotes.filter { note ->
            val matchesStatus = note.status == status
            val matchesPage = pageContext.isNullOrBlank() || note.pageContext.equals(pageContext, ignoreCase = true)
            val matchesQuery = sanitizedQuery.isEmpty() ||
                note.title.lowercase().contains(sanitizedQuery) ||
                note.content.lowercase().contains(sanitizedQuery) ||
                note.pageContext.lowercase().contains(sanitizedQuery)

            matchesStatus && matchesPage && matchesQuery
        }
        return Result.success(filtered)
    }

    override suspend fun create(
        title: String,
        content: String,
        pageContext: String
    ): Result<NoteItem> {
        delay(150)
        val newItem = NoteItem(
            id = "note_${System.currentTimeMillis()}",
            title = title.ifBlank { "Untitled Note" },
            content = content,
            pageContext = pageContext.ifBlank { "General" },
            status = NoteStatus.ACTIVE,
            createdAtFormatted = "Today"
        )
        storedNotes.add(0, newItem)
        return Result.success(newItem)
    }

    override suspend fun toggleArchive(id: String): Result<Unit> {
        delay(100)
        val index = storedNotes.indexOfFirst { it.id == id }
        if (index != -1) {
            val current = storedNotes[index]
            val newStatus = if (current.status == NoteStatus.ACTIVE) NoteStatus.ARCHIVED else NoteStatus.ACTIVE
            storedNotes[index] = current.copy(status = newStatus)
        }
        return Result.success(Unit)
    }

    override suspend fun delete(id: String): Result<Unit> {
        delay(100)
        storedNotes.removeAll { it.id == id }
        return Result.success(Unit)
    }
}
