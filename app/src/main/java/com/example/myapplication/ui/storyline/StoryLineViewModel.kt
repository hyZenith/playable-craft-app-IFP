package com.example.myapplication.ui.storyline

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.StoryLineSummary
import com.example.myapplication.data.repo.StoryLineRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Immutable UI state for the StoryLine screen.
 *
 * @property storylines List of saved StoryLine modules.
 * @property isLoading Indicates whether a fetch operation is active.
 * @property errorMessage Non-null if an error occurred during loading.
 */
data class StoryLineUiState(
    val storylines: List<StoryLineSummary> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

/**
 * ViewModel managing state and operations for the StoryLine screen.
 */
class StoryLineViewModel(
    private val repository: StoryLineRepository
) : ViewModel() {

    var uiState by mutableStateOf(StoryLineUiState())
        private set

    private var loadJob: Job? = null

    init {
        loadStoryLines()
    }

    /**
     * Retries loading data.
     */
    fun retry() {
        loadStoryLines()
    }

    /**
     * Deletes a StoryLine item by ID.
     */
    fun deleteStoryLine(id: String) {
        viewModelScope.launch {
            repository.delete(id)
                .onSuccess {
                    uiState = uiState.copy(
                        storylines = uiState.storylines.filterNot { it.id == id }
                    )
                }
        }
    }

    private fun loadStoryLines() {
        loadJob?.cancel()
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        loadJob = viewModelScope.launch {
            repository.list()
                .onSuccess { items ->
                    uiState = uiState.copy(
                        isLoading = false,
                        storylines = items
                    )
                }
                .onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = "Unable to load StoryLines. Please try again."
                    )
                }
        }
    }
}
