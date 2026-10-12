package com.example.myapplication.ui.schedule

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.DeliveryContentType
import com.example.myapplication.data.model.DeliveryItem
import com.example.myapplication.data.repo.DeliveryRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

val batchOptions = listOf(
    "All batches",
    "Class 10 - Batch A",
    "Class 10 - Batch B",
    "Class 9 - Batch A",
    "Batch 2026-2027"
)

/**
 * Immutable UI state for the Schedule / Deliveries screen.
 *
 * @property batchFilter Currently active batch filter (or null for all).
 * @property dateRangeText Active calendar evaluation window.
 * @property deliveries List of scheduled or delivered items.
 * @property isLoading Indicates whether a fetch request is pending.
 * @property showCreateDialog Whether the delivery scheduling modal is visible.
 * @property errorMessage Non-null if an error occurred during loading.
 */
data class ScheduleUiState(
    val batchFilter: String? = null,
    val dateRangeText: String = "9/6/2026 — 9/6/2027",
    val deliveries: List<DeliveryItem> = emptyList(),
    val isLoading: Boolean = true,
    val showCreateDialog: Boolean = false,
    val errorMessage: String? = null
)

/**
 * ViewModel managing state transitions, batch filtering, and delivery scheduling.
 */
class ScheduleViewModel(
    private val repository: DeliveryRepository
) : ViewModel() {

    var uiState by mutableStateOf(ScheduleUiState())
        private set

    private var loadJob: Job? = null

    init {
        loadDeliveries()
    }

    /**
     * Updates batch filter and triggers filtered loading.
     */
    fun onBatchSelected(batch: String?) {
        val normalized = if (batch == "All batches") null else batch
        uiState = uiState.copy(batchFilter = normalized)
        loadDeliveries()
    }

    /**
     * Opens the new delivery scheduling dialog.
     */
    fun openCreateDialog() {
        uiState = uiState.copy(showCreateDialog = true)
    }

    /**
     * Dismisses the delivery scheduling dialog.
     */
    fun closeCreateDialog() {
        uiState = uiState.copy(showCreateDialog = false)
    }

    /**
     * Creates and schedules a new content delivery to a student cohort.
     */
    fun createDelivery(
        title: String,
        contentType: DeliveryContentType,
        targetBatch: String,
        scheduledDate: String
    ) {
        viewModelScope.launch {
            repository.create(title, contentType, targetBatch, scheduledDate)
                .onSuccess {
                    uiState = uiState.copy(showCreateDialog = false)
                    loadDeliveries()
                }
        }
    }

    /**
     * Cancels or deletes a scheduled delivery.
     */
    fun deleteDelivery(id: String) {
        viewModelScope.launch {
            repository.delete(id)
                .onSuccess {
                    uiState = uiState.copy(
                        deliveries = uiState.deliveries.filterNot { it.id == id }
                    )
                }
        }
    }

    /**
     * Retries loading data.
     */
    fun retry() {
        loadDeliveries()
    }

    private fun loadDeliveries() {
        loadJob?.cancel()
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        loadJob = viewModelScope.launch {
            repository.list(batch = uiState.batchFilter)
                .onSuccess { list ->
                    uiState = uiState.copy(
                        isLoading = false,
                        deliveries = list
                    )
                }
                .onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = "Unable to load deliveries. Please try again."
                    )
                }
        }
    }
}
