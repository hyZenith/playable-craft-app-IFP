package com.example.myapplication.ui.lessonplan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.GenerateLessonPlanRequest
import com.example.myapplication.data.repo.LessonPlanRepository
import kotlinx.coroutines.launch

/**
 * Creation source mode for generating lesson plans.
 */
enum class LessonPlanCreationMode {
    FROM_TEXTBOOK,
    MANUAL
}

/**
 * Immutable UI state for the Create Lesson Plan generation interface.
 *
 * @property mode Current generation source mode (From Textbook vs Manual).
 * @property selectedGrade Active grade level selection.
 * @property selectedSubject Active subject selection.
 * @property selectedChapter Active textbook chapter selection.
 * @property selectedDurationMinutes Target duration of the lesson period in minutes.
 * @property selectedBoard Target examination or syllabus board (e.g., SEBA, NCERT, CBSE).
 * @property topicPrompt User's custom focus/topic input string.
 * @property isGenerating Indicates whether generation is underway.
 * @property createdPlanId Generated lesson plan ID upon successful completion.
 * @property errorMessage Non-null if an error occurred during generation.
 */
data class CreateLessonPlanUiState(
    val mode: LessonPlanCreationMode = LessonPlanCreationMode.FROM_TEXTBOOK,
    val selectedGrade: String? = null,
    val selectedSubject: String? = null,
    val selectedChapter: String? = null,
    val selectedDurationMinutes: Int = 5,
    val selectedBoard: String = "SEBA",
    val topicPrompt: String = "",
    val isGenerating: Boolean = false,
    val createdPlanId: String? = null,
    val errorMessage: String? = null
)

/**
 * ViewModel managing the interactive controls and generation workflow for new lesson plans.
 */
class CreateLessonPlanViewModel(
    private val repository: LessonPlanRepository
) : ViewModel() {

    var uiState by mutableStateOf(CreateLessonPlanUiState())
        private set

    val availableGrades = listOf(
        "Class 6", "Class 7", "Class 8", "Class 9", "Class 10", "Class 11", "Class 12"
    )

    val availableSubjects = listOf(
        "Physics", "Chemistry", "Biology", "Science", "Mathematics", "English", "Social Science"
    )

    val availableChapters = listOf(
        "Light – Reflection and Refraction",
        "Electricity",
        "Magnetic Effects of Electric Current",
        "Chemical Reactions and Equations",
        "Acids, Bases and Salts",
        "Life Processes",
        "Control and Coordination",
        "Our Environment"
    )

    val availableDurations = listOf(5, 15, 30, 45, 60, 90)

    val availableBoards = listOf("SEBA", "NCERT", "CBSE", "ICSE")

    /**
     * Toggles between From Textbook and Manual input modes.
     */
    fun onModeSelected(mode: LessonPlanCreationMode) {
        uiState = uiState.copy(mode = mode)
    }

    /**
     * Updates selected grade.
     */
    fun onGradeSelected(grade: String?) {
        uiState = uiState.copy(selectedGrade = grade)
    }

    /**
     * Updates selected subject.
     */
    fun onSubjectSelected(subject: String?) {
        uiState = uiState.copy(selectedSubject = subject)
    }

    /**
     * Updates selected chapter.
     */
    fun onChapterSelected(chapter: String?) {
        uiState = uiState.copy(selectedChapter = chapter)
    }

    /**
     * Updates lesson duration.
     */
    fun onDurationSelected(durationMinutes: Int) {
        uiState = uiState.copy(selectedDurationMinutes = durationMinutes)
    }

    /**
     * Updates selected syllabus board.
     */
    fun onBoardSelected(board: String) {
        uiState = uiState.copy(selectedBoard = board)
    }

    /**
     * Updates free-form topic input.
     */
    fun onTopicPromptChange(prompt: String) {
        uiState = uiState.copy(topicPrompt = prompt)
    }

    /**
     * Initiates generation of a new lesson plan.
     */
    fun generatePlan(onSuccess: (planId: String) -> Unit) {
        val topic = uiState.topicPrompt.trim().ifEmpty {
            uiState.selectedChapter ?: "Core Concepts"
        }
        val subject = uiState.selectedSubject ?: "Science"
        val grade = uiState.selectedGrade ?: "Class 10"

        uiState = uiState.copy(isGenerating = true, errorMessage = null)

        viewModelScope.launch {
            val request = GenerateLessonPlanRequest(
                board = uiState.selectedBoard,
                subject = subject,
                grade = grade,
                topic = topic,
                chapterName = uiState.selectedChapter,
                estimatedDurationMinutes = uiState.selectedDurationMinutes
            )

            val result = repository.create(request)
            if (result.isSuccess) {
                val created = result.getOrNull()
                val id = created?._id ?: "plan_${System.currentTimeMillis()}"
                uiState = uiState.copy(
                    isGenerating = false,
                    createdPlanId = id
                )
                onSuccess(id)
            } else {
                uiState = uiState.copy(
                    isGenerating = false,
                    errorMessage = "Failed to generate lesson plan. Please check inputs and try again."
                )
            }
        }
    }
}
