// McqModels.kt
// Matches the response shape of:
//   POST /api/mcqs/generate   (Create)
//   GET  /api/mcqs/:mcqId     (Read - detail, includes full questions[])
//   GET  /api/mcqs            (Read - list, uses McqSummary — no questions[])
//
// Field names and nullability mirror the backend's mcq.types.ts exactly.

package com.example.myapplication.data.model

const val MCQ_MIN_QUESTIONS = 10
const val MCQ_MAX_QUESTIONS = 45

typealias McqDifficulty = String        // "easy" | "medium" | "hard"
typealias McqOverallDifficulty = String // McqDifficulty | "mixed"
typealias McqSourceType = String        // "rag" | "lesson-plan" | "document"

data class McqTimer(
    val enabled: Boolean,
    val secondsPerQuestion: Int? = null // required by backend validation only when enabled = true
)

data class McqQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String? = null,
    val difficulty: McqDifficulty,
    val sourceChunkRef: String? = null
)

// ─── Full document (Create response / Read-one response) ──────────────────────

data class Mcq(
    val _id: String,
    val board: SyllabusBoard,
    val subject: String,
    val grade: String,
    val chapterNumbers: List<Int>,
    val chapterNames: List<String>,
    val sourceType: McqSourceType,
    val lessonPlanId: String? = null,
    val ragGrounded: Boolean,
    val seedThemes: List<String>,
    val title: String,
    val questionCount: Int,
    val optionsPerQuestion: Int, // 4 or 5
    val difficulty: McqOverallDifficulty,
    val timer: McqTimer,
    val questions: List<McqQuestion>,
    val aiModel: String,
    val generationDurationMs: Long? = null,
    val createdAt: String,
    val updatedAt: String
) {
    val id: String get() = _id
}

// ─── List item (lighter projection — no questions[]) ───────────────────────────

data class McqSummary(
    val _id: String,
    val title: String,
    val subject: String,
    val board: SyllabusBoard,
    val grade: String,
    val chapterNumbers: List<Int>,
    val chapterNames: List<String>,
    val questionCount: Int,
    val optionsPerQuestion: Int,
    val difficulty: McqOverallDifficulty,
    val timer: McqTimer,
    val sourceType: McqSourceType,
    val ragGrounded: Boolean,
    val createdAt: String
) {
    val id: String get() = _id
}

data class McqPaginationMeta(
    val page: Int,
    val limit: Int,
    val total: Int,
    val pages: Int
)

// ─── Request payload ────────────────────────────────────────────────────────────

data class GenerateMcqRequest(
    val board: SyllabusBoard,
    val subject: String,
    val grade: String,
    val chapterNumbers: List<Int>,
    val questionCount: Int,       // 10..45
    val optionsPerQuestion: Int,  // 4 or 5
    val difficulty: McqOverallDifficulty,
    val timer: McqTimer,
    val title: String? = null,
    val seedThemes: List<String>? = null,
    val lessonPlanId: String? = null,
    val sourceType: McqSourceType? = null
)

// ─── API envelope wrapper for the list endpoint ────────────────────────────────
// Note: the key is "mcqs" here, NOT "plans" like the lesson-plan list endpoint.

data class McqListData(
    val mcqs: List<McqSummary>,
    val pagination: McqPaginationMeta
)

fun Mcq.toSummary(): McqSummary = McqSummary(
    _id = _id,
    title = title,
    subject = subject,
    board = board,
    grade = grade,
    chapterNumbers = chapterNumbers,
    chapterNames = chapterNames,
    questionCount = questionCount,
    optionsPerQuestion = optionsPerQuestion,
    difficulty = difficulty,
    timer = timer,
    sourceType = sourceType,
    ragGrounded = ragGrounded,
    createdAt = createdAt
)
