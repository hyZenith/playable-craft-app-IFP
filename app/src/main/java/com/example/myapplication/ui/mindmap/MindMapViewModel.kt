package com.example.myapplication.ui.mindmap

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.MindMapSummary
import com.example.myapplication.data.repo.MindMapRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

val mindMapBoardOptions = listOf("SEBA", "NCERT", "CBSE", "ICSE")
val mindMapSubjectOptions = listOf(
    "Mathematics",
    "Science",
    "Social Science",
    "Physics",
    "Chemistry",
    "English"
)

/**
 * Immutable UI state for the Mind Maps screen.
 *
 * @property boardFilter Currently selected educational board filter.
 * @property subjectFilter Currently selected subject filter.
 * @property mindMaps List of loaded Mind Map summaries.
 * @property isLoading Indicates whether a fetch request is pending.
 * @property errorMessage Non-null if an error occurred during loading.
 */
data class MindMapUiState(
    val boardFilter: String? = null,
    val subjectFilter: String? = null,
    val mindMaps: List<MindMapSummary> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
) {
    val hasFilters: Boolean
        get() = boardFilter != null || subjectFilter != null
}

/**
 * ViewModel managing state transitions, filtering, and data operations for the Mind Maps screen.
 */
class MindMapViewModel(
    private val repository: MindMapRepository
) : ViewModel() {

    var uiState by mutableStateOf(MindMapUiState())
        private set

    private var loadJob: Job? = null

    init {
        loadMindMaps()
    }

    /**
     * Updates the board filter and triggers a reload.
     */
    fun onBoardSelected(board: String?) {
        uiState = uiState.copy(boardFilter = board)
        loadMindMaps()
    }

    /**
     * Updates the subject filter and triggers a reload.
     */
    fun onSubjectSelected(subject: String?) {
        uiState = uiState.copy(subjectFilter = subject)
        loadMindMaps()
    }

    /**
     * Resets all applied filters.
     */
    fun clearFilters() {
        uiState = uiState.copy(boardFilter = null, subjectFilter = null)
        loadMindMaps()
    }

    /**
     * Retries loading data.
     */
    fun retry() {
        loadMindMaps()
    }

    /**
     * Deletes a Mind Map by identifier.
     */
    fun deleteMindMap(id: String) {
        viewModelScope.launch {
            repository.delete(id)
                .onSuccess {
                    uiState = uiState.copy(
                        mindMaps = uiState.mindMaps.filterNot { it.id == id }
                    )
                }
        }
    }

    private fun loadMindMaps() {
        loadJob?.cancel()
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        loadJob = viewModelScope.launch {
            repository.list(
                board = uiState.boardFilter,
                subject = uiState.subjectFilter
            )
                .onSuccess { items ->
                    uiState = uiState.copy(
                        isLoading = false,
                        mindMaps = items
                    )
                }
                .onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = "Unable to load Mind Maps. Please check your connection."
                    )
                }
        }
    }
}
