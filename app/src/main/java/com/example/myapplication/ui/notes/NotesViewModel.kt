package com.example.myapplication.ui.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.NoteItem
import com.example.myapplication.data.model.NoteStatus
import com.example.myapplication.data.repo.NoteRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

val notePageOptions = listOf(
    "Lesson Plans",
    "MCQ Sets",
    "PlayableCraft AI",
    "StoryLine",
    "Mind Maps",
    "General"
)

/**
 * Immutable UI state for the Notes screen.
 *
 * @property statusTab Active toggle filter (Active or Archived).
 * @property searchQuery Text entered in search input.
 * @property pageFilter Selected origin page filter (or null for "All pages").
 * @property notes List of loaded notes.
 * @property isLoading Indicates whether a fetch request is pending.
 * @property showCreateDialog Whether the quick note creation sheet is visible.
 * @property errorMessage Non-null if an error occurred during loading.
 */
data class NotesUiState(
    val statusTab: NoteStatus = NoteStatus.ACTIVE,
    val searchQuery: String = "",
    val pageFilter: String? = null,
    val notes: List<NoteItem> = emptyList(),
    val isLoading: Boolean = true,
    val showCreateDialog: Boolean = false,
    val errorMessage: String? = null
) {
    val isSearchOrFilterActive: Boolean
        get() = searchQuery.isNotBlank() || pageFilter != null
}

/**
 * ViewModel managing state transitions, filtering, and note operations.
 */
class NotesViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    var uiState by mutableStateOf(NotesUiState())
        private set

    private var loadJob: Job? = null

    init {
        loadNotes()
    }

    /**
     * Changes between Active and Archived views.
     */
    fun onStatusTabSelected(status: NoteStatus) {
        uiState = uiState.copy(statusTab = status)
        loadNotes()
    }

    /**
     * Updates search query text.
     */
    fun onSearchQueryChanged(query: String) {
        uiState = uiState.copy(searchQuery = query)
        loadNotes()
    }

    /**
     * Updates page origin filter.
     */
    fun onPageFilterSelected(page: String?) {
        uiState = uiState.copy(pageFilter = page)
        loadNotes()
    }

    /**
     * Displays the quick note modal.
     */
    fun openCreateDialog() {
        uiState = uiState.copy(showCreateDialog = true)
    }

    /**
     * Dismisses the quick note modal.
     */
    fun closeCreateDialog() {
        uiState = uiState.copy(showCreateDialog = false)
    }

    /**
     * Creates and saves a new note item.
     */
    fun createNote(title: String, content: String, pageContext: String) {
        viewModelScope.launch {
            repository.create(title, content, pageContext)
                .onSuccess {
                    uiState = uiState.copy(showCreateDialog = false)
                    loadNotes()
                }
        }
    }

    /**
     * Moves a note between active and archived states.
     */
    fun toggleArchive(id: String) {
        viewModelScope.launch {
            repository.toggleArchive(id)
                .onSuccess { loadNotes() }
        }
    }

    /**
     * Deletes a note permanently.
     */
    fun deleteNote(id: String) {
        viewModelScope.launch {
            repository.delete(id)
                .onSuccess {
                    uiState = uiState.copy(
                        notes = uiState.notes.filterNot { it.id == id }
                    )
                }
        }
    }

    /**
     * Retries loading data.
     */
    fun retry() {
        loadNotes()
    }

    private fun loadNotes() {
        loadJob?.cancel()
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        loadJob = viewModelScope.launch {
            repository.list(
                query = uiState.searchQuery,
                pageContext = uiState.pageFilter,
                status = uiState.statusTab
            )
                .onSuccess { list ->
                    uiState = uiState.copy(
                        isLoading = false,
                        notes = list
                    )
                }
                .onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = "Unable to load notes. Please try again."
                    )
                }
        }
    }
}
