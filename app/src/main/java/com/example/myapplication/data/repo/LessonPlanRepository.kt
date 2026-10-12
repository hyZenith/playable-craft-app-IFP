package com.example.myapplication.data.repo

import com.example.myapplication.data.mock.MockLessonPlansData
import com.example.myapplication.data.model.DifferentiationNotes
import com.example.myapplication.data.model.GenerateLessonPlanRequest
import com.example.myapplication.data.model.LessonObjective
import com.example.myapplication.data.model.LessonPlan
import com.example.myapplication.data.model.LessonPlanListData
import com.example.myapplication.data.model.LessonPlanSummary
import com.example.myapplication.data.model.McqMeta
import com.example.myapplication.data.model.PaginationMeta
import com.example.myapplication.data.model.SimulationMeta
import com.example.myapplication.data.model.TimeBlock
import com.example.myapplication.data.model.toSummary
import kotlinx.coroutines.delay

interface LessonPlanRepository {
    suspend fun list(
        board: String?,
        subject: String?,
        search: String? = null,
        page: Int = 1,
        limit: Int = 20
    ): Result<LessonPlanListData>

    suspend fun getById(id: String): Result<LessonPlan?>

    suspend fun create(request: GenerateLessonPlanRequest): Result<LessonPlanSummary>
}

/**
 * Repository implementation backed by client mock data (lesson_plans_mock.json).
 * withSamples = false  -> shows the empty state ("No lesson plans yet.")
 * withSamples = true   -> shows the list initialized with client mock items.
 */
class FakeLessonPlanRepository(private val withSamples: Boolean = true) : LessonPlanRepository {

    private val plansList: MutableList<LessonPlan> = if (withSamples) {
        MockLessonPlansData.items.toMutableList()
    } else {
        mutableListOf()
    }

    override suspend fun list(
        board: String?,
        subject: String?,
        search: String?,
        page: Int,
        limit: Int
    ): Result<LessonPlanListData> {
        delay(350) // simulate network
        val query = search?.trim()?.lowercase().orEmpty()
        val filtered = plansList
            .filter { board == null || it.board.equals(board, ignoreCase = true) }
            .filter { subject == null || it.subject.equals(subject, ignoreCase = true) }
            .filter {
                query.isEmpty() ||
                    it.topic.lowercase().contains(query) ||
                    it.subject.lowercase().contains(query) ||
                    it.grade.lowercase().contains(query) ||
                    it.board.lowercase().contains(query) ||
                    (it.chapterName?.lowercase()?.contains(query) == true)
            }
        val summaries = filtered.map { it.toSummary() }
        val pageItems = summaries.drop((page - 1) * limit).take(limit)
        return Result.success(
            LessonPlanListData(
                plans = pageItems,
                pagination = PaginationMeta(
                    total = filtered.size,
                    page = page,
                    limit = limit,
                    totalPages = maxOf(1, (filtered.size + limit - 1) / limit)
                )
            )
        )
    }

    override suspend fun getById(id: String): Result<LessonPlan?> {
        delay(200)
        val plan = plansList.find { it._id == id || it.id == id }
        return Result.success(plan)
    }

    override suspend fun create(request: GenerateLessonPlanRequest): Result<LessonPlanSummary> {
        delay(600)
        val now = "2026-10-12T00:00:00.000Z"
        val newId = "plan_${System.currentTimeMillis()}"
        val newPlan = LessonPlan(
            _id = newId,
            providerId = "prv_school_001",
            providerKind = "SCHOOL",
            createdByTeacherId = "tch_fake_001",
            board = request.board,
            subject = request.subject,
            grade = request.grade,
            chapterName = request.chapterName,
            topic = request.topic.trim(),
            definition = "${request.topic} core conceptual foundation and practical application.",
            objectives = listOf(
                LessonObjective("Students will be able to define key terms for ${request.topic}", "Remember"),
                LessonObjective("Students will be able to explain the principles of ${request.topic}", "Understand"),
                LessonObjective("Students will be able to apply concepts of ${request.topic} to problem solving", "Apply")
            ),
            advantages = listOf("Deepens conceptual retention", "Connects to real-world phenomena"),
            disadvantages = listOf("Requires guided scaffolding"),
            realWorldApplications = listOf("Practical engineering, technology and daily life examples"),
            difficultyLevel = "Medium",
            estimatedDurationMinutes = request.estimatedDurationMinutes ?: 45,
            priorKnowledgeRequired = listOf("Foundational prerequisite principles"),
            commonMisconceptions = listOf("Equating non-equivalent variables"),
            suggestedAnalogies = listOf("Relatable real world scenario analogy"),
            keyVocabulary = emptyList(),
            timeBlockPlan = listOf(
                TimeBlock(5, "Warm-up & Hook", "hook"),
                TimeBlock(20, "Concept Exploration", "concept"),
                TimeBlock(15, "Guided Activity", "activity"),
                TimeBlock(5, "Formative Wrap-up", "assessment")
            ),
            teachingStrategies = listOf("Interactive Lecture", "Inquiry-Based", "Think-Pair-Share"),
            differentiationNotes = DifferentiationNotes(
                slowLearners = "Provide step-by-step worked scaffolding",
                advancedLearners = "Offer real-world extension inquiry"
            ),
            boardWorkOutline = listOf("Title: ${request.topic}", "Core Definitions", "Key Formulas / Concepts"),
            exitTicketQuestions = listOf("Summarize the central idea in your own words."),
            formativeAssessmentPrompts = listOf("Quick thumbs up/down check for understanding."),
            hotsQuestions = listOf("What happens if boundary conditions are changed?"),
            crossSubjectConnections = listOf("Mathematical modeling and scientific inquiry"),
            previousChapterDependencies = emptyList(),
            nextChapterPreparation = emptyList(),
            simulation = SimulationMeta(feasible = true),
            mcq = McqMeta(feasible = true, suggestedCount = 10),
            notesGeneratable = true,
            isArchived = false,
            generationDurationMs = 8500L,
            createdAt = now,
            updatedAt = now
        )
        plansList.add(0, newPlan)
        return Result.success(newPlan.toSummary())
    }
}
