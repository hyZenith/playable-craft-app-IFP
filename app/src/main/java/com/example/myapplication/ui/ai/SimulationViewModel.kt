package com.example.myapplication.ui.ai

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.SimulationSummary
import com.example.myapplication.data.repo.SimulationRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Immutable UI state for the PlayableCraft AI simulations screen.
 *
 * @property searchQuery Current text entered in the search field.
 * @property simulations List of loaded simulation summaries.
 * @property isLoading Indicates if a fetch operation is ongoing.
 * @property errorMessage Non-null if an error occurred during loading.
 */
data class SimulationUiState(
    val searchQuery: String = "",
    val simulations: List<SimulationSummary> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
) {
    val isSearchActive: Boolean
        get() = searchQuery.isNotBlank()
}

/**
 * ViewModel managing simulation querying, state mutations, and user events for the AI screen.
 */
class SimulationViewModel(
    private val repository: SimulationRepository
) : ViewModel() {

    var uiState by mutableStateOf(SimulationUiState())
        private set

    private var searchJob: Job? = null

    init {
        loadSimulations()
    }

    /**
     * Updates search query and triggers filtered loading.
     */
    fun onSearchQueryChanged(query: String) {
        uiState = uiState.copy(searchQuery = query)
        loadSimulations()
    }

    /**
     * Clears any active query filter.
     */
    fun clearSearch() {
        uiState = uiState.copy(searchQuery = "")
        loadSimulations()
    }

    /**
     * Retries loading data following an error.
     */
    fun retry() {
        loadSimulations()
    }

    /**
     * Deletes a simulation by its identifier.
     */
    fun deleteSimulation(id: String) {
        viewModelScope.launch {
            repository.delete(id)
                .onSuccess {
                    uiState = uiState.copy(
                        simulations = uiState.simulations.filterNot { it.id == id }
                    )
                }
        }
    }

    private fun loadSimulations() {
        searchJob?.cancel()
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        searchJob = viewModelScope.launch {
            repository.list(query = uiState.searchQuery)
                .onSuccess { list ->
                    uiState = uiState.copy(
                        isLoading = false,
                        simulations = list
                    )
                }
                .onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = "Couldn't retrieve simulations. Please try again."
                    )
                }
        }
    }
}
