package com.example.myapplication.ui.mcq

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.McqSummary
import com.example.myapplication.data.repo.McqRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

val mcqBoardOptions = listOf("SEBA", "NCERT", "CBSE", "ICSE")
val mcqSubjectOptions = listOf(
    "Science",
    "Mathematics",
    "Physics",
    "Chemistry",
    "Biology",
    "English",
    "Computer Science"
)

/**
 * Immutable UI state for the MCQ Sets management screen.
 *
 * @property boardFilter Currently selected board filter (or null for all).
 * @property subjectFilter Currently selected subject filter (or null for all).
 * @property mcqSets List of matching MCQ sets (McqSummary items matching client models).
 * @property isLoading Indicates whether a fetch operation is active.
 * @property errorMessage Non-null if an error occurred during loading.
 */
data class McqUiState(
    val boardFilter: String? = null,
    val subjectFilter: String? = null,
    val searchQuery: String = "",
    val mcqSets: List<McqSummary> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
) {
    val hasFilters: Boolean
        get() = boardFilter != null || subjectFilter != null || searchQuery.isNotBlank()
}

/**
 * ViewModel managing the business logic, state mutations, and repository calls for the MCQ screen.
 */
class McqViewModel(
    private val repository: McqRepository
) : ViewModel() {

    var uiState by mutableStateOf(McqUiState())
        private set

    private var loadJob: Job? = null

    init {
        loadSets()
    }

    /**
     * Updates the board filter selection and triggers a reload.
     */
    fun onBoardSelected(board: String?) {
        uiState = uiState.copy(boardFilter = board)
        loadSets()
    }

    /**
     * Updates the subject filter selection and triggers a reload.
     */
    fun onSubjectSelected(subject: String?) {
        uiState = uiState.copy(subjectFilter = subject)
        loadSets()
    }

    /**
     * Updates the search query.
     */
    fun onSearchQueryChanged(query: String) {
        uiState = uiState.copy(searchQuery = query)
        loadSets()
    }

    /**
     * Resets all applied filters back to their default "all" state.
     */
    fun clearFilters() {
        uiState = uiState.copy(boardFilter = null, subjectFilter = null, searchQuery = "")
        loadSets()
    }

    /**
     * Retries fetching data after an error.
     */
    fun retry() {
        loadSets()
    }

    /**
     * Deletes an MCQ set and updates the list.
     */
    fun deleteSet(id: String) {
        viewModelScope.launch {
            repository.delete(id)
                .onSuccess {
                    uiState = uiState.copy(
                        mcqSets = uiState.mcqSets.filterNot { it._id == id || it.id == id }
                    )
                }
        }
    }

    private fun loadSets() {
        loadJob?.cancel()
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        loadJob = viewModelScope.launch {
            repository.list(
                board = uiState.boardFilter,
                subject = uiState.subjectFilter,
                search = uiState.searchQuery
            )
                .onSuccess { sets ->
                    uiState = uiState.copy(
                        isLoading = false,
                        mcqSets = sets
                    )
                }
                .onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = "Unable to load MCQ sets. Please check your connection."
                    )
                }
        }
    }
}
