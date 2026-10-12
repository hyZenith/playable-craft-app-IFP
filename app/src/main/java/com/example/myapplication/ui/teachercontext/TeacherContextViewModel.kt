package com.example.myapplication.ui.teachercontext

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.PromptGuide
import com.example.myapplication.data.model.PromptRule
import com.example.myapplication.data.model.TeacherContextTab
import com.example.myapplication.data.model.VideoWalkthrough
import com.example.myapplication.data.repo.TeacherContextRepository
import kotlinx.coroutines.launch

/**
 * Immutable UI state for the Teacher Context guide page.
 *
 * @property selectedTab Current active tab view (Videos or Prompt Engineering).
 * @property videos List of video walkthrough guides.
 * @property promptGuides List of pedagogical prompt engineering recommendations.
 * @property promptRules List of structured weak vs better comparison rules.
 * @property expandedRuleIds Set of prompt rule IDs currently expanded.
 * @property isLoading Indicates whether data is being fetched.
 * @property errorMessage Non-null if an error occurred during loading.
 */
data class TeacherContextUiState(
    val selectedTab: TeacherContextTab = TeacherContextTab.VIDEOS,
    val videos: List<VideoWalkthrough> = emptyList(),
    val promptGuides: List<PromptGuide> = emptyList(),
    val promptRules: List<PromptRule> = emptyList(),
    val expandedRuleIds: Set<String> = emptySet(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

/**
 * ViewModel managing Teacher Context instructions, active view tabs, and repository fetching.
 */
class TeacherContextViewModel(
    private val repository: TeacherContextRepository
) : ViewModel() {

    var uiState by mutableStateOf(TeacherContextUiState())
        private set

    init {
        loadData()
    }

    /**
     * Changes the current active tab.
     */
    fun onTabSelected(tab: TeacherContextTab) {
        uiState = uiState.copy(selectedTab = tab)
    }

    /**
     * Toggles expansion state of a prompt engineering rule card.
     */
    fun toggleRuleExpanded(ruleId: String) {
        val currentExpanded = uiState.expandedRuleIds
        val nextExpanded = if (currentExpanded.contains(ruleId)) {
            currentExpanded - ruleId
        } else {
            currentExpanded + ruleId
        }
        uiState = uiState.copy(expandedRuleIds = nextExpanded)
    }

    /**
     * Retries loading data.
     */
    fun retry() {
        loadData()
    }

    private fun loadData() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val videoResult = repository.getVideos()
            val promptResult = repository.getPromptGuides()
            val rulesResult = repository.getPromptRules()

            if (videoResult.isSuccess && promptResult.isSuccess && rulesResult.isSuccess) {
                val rules = rulesResult.getOrDefault(emptyList())
                uiState = uiState.copy(
                    isLoading = false,
                    videos = videoResult.getOrDefault(emptyList()),
                    promptGuides = promptResult.getOrDefault(emptyList()),
                    promptRules = rules,
                    expandedRuleIds = rules.map { it.id }.toSet()
                )
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Unable to load Teacher Context instructions. Please try again."
                )
            }
        }
    }
}
