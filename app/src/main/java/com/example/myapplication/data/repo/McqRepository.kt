package com.example.myapplication.data.repo

import com.example.myapplication.data.mock.MockMcqsData
import com.example.myapplication.data.model.GenerateMcqRequest
import com.example.myapplication.data.model.Mcq
import com.example.myapplication.data.model.McqQuestion
import com.example.myapplication.data.model.McqSummary
import com.example.myapplication.data.model.toSummary
import kotlinx.coroutines.delay

/**
 * Repository interface for managing and querying generated MCQ sets matching client McqModels.kt.
 */
interface McqRepository {
    /**
     * Retrieves saved MCQ sets matching optional board, subject, and text filters.
     * Corresponds to GET /api/mcqs
     */
    suspend fun list(board: String? = null, subject: String? = null, search: String? = null): Result<List<McqSummary>>

    /**
     * Retrieves full MCQ details including questions[] and timer.
     * Corresponds to GET /api/mcqs/:mcqId
     */
    suspend fun getById(id: String): Result<Mcq?>

    /**
     * Generates a new MCQ set matching GenerateMcqRequest.
     * Corresponds to POST /api/mcqs/generate
     */
    suspend fun create(request: GenerateMcqRequest): Result<Mcq>

    /**
     * Deletes an MCQ set by its unique identifier.
     */
    suspend fun delete(id: String): Result<Unit>
}

/**
 * Repository implementation initialized with client mock data (mcqs_mock.json).
 *
 * @param withSamples When true, seeds initial items matching the client specifications.
 */
class FakeMcqRepository(private val withSamples: Boolean = true) : McqRepository {

    private val mcqsList: MutableList<Mcq> = if (withSamples) {
        MockMcqsData.items.toMutableList()
    } else {
        mutableListOf()
    }

    override suspend fun list(board: String?, subject: String?, search: String?): Result<List<McqSummary>> {
        delay(350)
        val query = search?.trim()?.lowercase().orEmpty()
        val filtered = mcqsList.filter { item ->
            val matchesBoard = board.isNullOrBlank() || item.board.equals(board, ignoreCase = true)
            val matchesSubject = subject.isNullOrBlank() || item.subject.equals(subject, ignoreCase = true)
            val matchesQuery = query.isEmpty() ||
                item.title.lowercase().contains(query) ||
                item.subject.lowercase().contains(query) ||
                item.grade.lowercase().contains(query) ||
                item.board.lowercase().contains(query) ||
                item.chapterNames.any { it.lowercase().contains(query) }
            matchesBoard && matchesSubject && matchesQuery
        }
        return Result.success(filtered.map { it.toSummary() })
    }

    override suspend fun getById(id: String): Result<Mcq?> {
        delay(200)
        val item = mcqsList.find { it._id == id || it.id == id }
        return Result.success(item)
    }

    override suspend fun create(request: GenerateMcqRequest): Result<Mcq> {
        delay(800)
        val now = "2026-10-12T00:00:00.000Z"
        val count = request.questionCount.coerceIn(10, 45)
        val optsCount = request.optionsPerQuestion.coerceIn(4, 5)
        val title = request.title?.trim()?.ifEmpty { null }
            ?: "${request.subject} — ${request.grade} MCQ ($count Q)"

        val generatedQuestions = (1..count).map { i ->
            val options = (1..optsCount).map { optIdx -> "Option ${('A' + optIdx - 1)} for question $i" }
            McqQuestion(
                question = "Regarding ${request.subject} key concepts, which statement is correct? (Q$i)",
                options = options,
                correctIndex = (i - 1) % optsCount,
                explanation = "Option ${('A' + ((i - 1) % optsCount))} is the verified correct answer based on syllabus standards.",
                difficulty = if (request.difficulty == "mixed") {
                    when (i % 3) { 0 -> "easy"; 1 -> "medium"; else -> "hard" }
                } else request.difficulty,
                sourceChunkRef = "${request.subject} — reference chunk $i"
            )
        }

        val newMcq = Mcq(
            _id = "mcq_${System.currentTimeMillis()}",
            board = request.board,
            subject = request.subject,
            grade = request.grade,
            chapterNumbers = request.chapterNumbers,
            chapterNames = request.chapterNumbers.map { "Chapter $it" },
            sourceType = request.sourceType ?: "rag",
            lessonPlanId = request.lessonPlanId,
            ragGrounded = true,
            seedThemes = request.seedThemes ?: listOf(request.subject),
            title = title,
            questionCount = count,
            optionsPerQuestion = optsCount,
            difficulty = request.difficulty,
            timer = request.timer,
            questions = generatedQuestions,
            aiModel = "claude-sonnet-4-6",
            generationDurationMs = 12500L,
            createdAt = now,
            updatedAt = now
        )

        mcqsList.add(0, newMcq)
        return Result.success(newMcq)
    }

    override suspend fun delete(id: String): Result<Unit> {
        delay(200)
        mcqsList.removeAll { it._id == id || it.id == id }
        return Result.success(Unit)
    }
}
