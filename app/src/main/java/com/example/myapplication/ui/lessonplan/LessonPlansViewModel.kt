package com.example.myapplication.ui.lessonplan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.LessonPlanSummary
import com.example.myapplication.data.repo.LessonPlanRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

val boardOptions = listOf("SEBA", "NCERT", "CBSE", "ICSE")
val subjectOptions = listOf("Science", "Mathematics", "Physics", "Chemistry", "Biology", "Computer Science", "AI")

enum class PlanSortOrder(val label: String) {
    NEWEST_FIRST("Newest first"),
    OLDEST_FIRST("Oldest first"),
    TITLE_ASC("Title (A-Z)"),
    TITLE_DESC("Title (Z-A)")
}

data class LessonPlansUiState(
    val boardFilter: String? = null,
    val subjectFilter: String? = null,
    val searchQuery: String = "",
    val sortOrder: PlanSortOrder = PlanSortOrder.NEWEST_FIRST,
    val plans: List<LessonPlanSummary> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
) {
    val hasFilters get() = boardFilter != null || subjectFilter != null || searchQuery.isNotBlank()
}

class LessonPlansViewModel(
    private val repository: LessonPlanRepository
) : ViewModel() {

    var uiState by mutableStateOf(LessonPlansUiState())
        private set

    private var loadJob: Job? = null

    init { load() }

    fun onBoardSelected(board: String?) {
        uiState = uiState.copy(boardFilter = board)
        load()
    }

    fun onSubjectSelected(subject: String?) {
        uiState = uiState.copy(subjectFilter = subject)
        load()
    }

    fun onSearchQueryChanged(query: String) {
        uiState = uiState.copy(searchQuery = query)
        load()
    }

    fun onSortOrderSelected(order: PlanSortOrder) {
        uiState = uiState.copy(
            sortOrder = order,
            plans = sortPlans(uiState.plans, order)
        )
    }

    fun clearFilters() {
        uiState = uiState.copy(boardFilter = null, subjectFilter = null, searchQuery = "")
        load()
    }

    fun retry() = load()

    private fun sortPlans(list: List<LessonPlanSummary>, order: PlanSortOrder): List<LessonPlanSummary> {
        return when (order) {
            PlanSortOrder.NEWEST_FIRST -> list.sortedByDescending { it.createdAt }
            PlanSortOrder.OLDEST_FIRST -> list.sortedBy { it.createdAt }
            PlanSortOrder.TITLE_ASC -> list.sortedBy { it.topic.lowercase() }
            PlanSortOrder.TITLE_DESC -> list.sortedByDescending { it.topic.lowercase() }
        }
    }

    private fun load() {
        loadJob?.cancel()
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        loadJob = viewModelScope.launch {
            repository.list(
                board = uiState.boardFilter,
                subject = uiState.subjectFilter,
                search = uiState.searchQuery
            )
                .onSuccess { data ->
                    val sorted = sortPlans(data.plans, uiState.sortOrder)
                    uiState = uiState.copy(isLoading = false, plans = sorted)
                }
                .onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = "Couldn't load your lesson plans. Please try again."
                    )
                }
        }
    }
}
