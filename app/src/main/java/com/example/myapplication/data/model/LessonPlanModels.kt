package com.example.myapplication.data.model

// Enums (kept as String on the wire - safe if backend adds new values later)

typealias SyllabusBoard = String   // "SEBA" | "NCERT" | "CBSE" | "ICSE"
typealias BloomLevel = String      // "Remember" | "Understand" | "Apply" | "Analyze" | "Evaluate" | "Create"
typealias LessonDifficultyLevel = String // "Easy" | "Medium" | "Hard"
typealias TeachingStrategy = String
typealias TimeBlockType = String   // "hook" | "concept" | "activity" | "assessment" | "wrap_up"
typealias ProviderKind = String    // "SCHOOL" | "TUTOR"

// Sub-types

data class LessonObjective(
    val text: String,
    val bloomLevel: BloomLevel
)

data class KeyVocabEntry(
    val term: String,
    val definition: String
)

data class TimeBlock(
    val duration: Int, // minutes
    val activity: String,
    val type: TimeBlockType
)

data class DifferentiationNotes(
    val slowLearners: String,
    val advancedLearners: String
)

data class SimulationMeta(
    val feasible: Boolean,
    val exposedVariables: List<String>? = null,
    val expectedOutcome: String? = null,
    val suggestedTool: String? = null
)

data class McqMeta(
    val feasible: Boolean,
    val seedThemes: List<String>? = null,
    val suggestedCount: Int? = null
)

// Full document (Create response / Read-one response)

data class LessonPlan(
    val _id: String,

    // Ownership
    val providerId: String,
    val providerKind: ProviderKind,
    val createdByTeacherId: String?,

    // Syllabus context
    val board: SyllabusBoard,
    val subject: String,
    val grade: String,
    val chapterName: String? = null,
    val topic: String,

    // Core content
    val definition: String,
    val objectives: List<LessonObjective>,
    val advantages: List<String>,
    val disadvantages: List<String>,
    val realWorldApplications: List<String>,
    val difficultyLevel: LessonDifficultyLevel,
    val estimatedDurationMinutes: Int,

    // Pedagogical
    val priorKnowledgeRequired: List<String>,
    val commonMisconceptions: List<String>,
    val suggestedAnalogies: List<String>,
    val keyVocabulary: List<KeyVocabEntry>,

    // Classroom execution
    val timeBlockPlan: List<TimeBlock>,
    val teachingStrategies: List<TeachingStrategy>,
    val differentiationNotes: DifferentiationNotes,
    val boardWorkOutline: List<String>,

    // Assessment
    val exitTicketQuestions: List<String>,
    val formativeAssessmentPrompts: List<String>,
    val hotsQuestions: List<String>,

    // Content linkage
    val crossSubjectConnections: List<String>,
    val previousChapterDependencies: List<String>,
    val nextChapterPreparation: List<String>,

    // Generation flags
    val simulation: SimulationMeta,
    val mcq: McqMeta,
    val notesGeneratable: Boolean,
    val handsOnActivity: String? = null,

    // Meta
    val isArchived: Boolean,
    val linkedSimulationId: String? = null,
    val linkedMcqId: String? = null,
    val generationDurationMs: Long? = null,
    val createdAt: String,
    val updatedAt: String
) {
    val id: String get() = _id
}

// ─── List item (lighter projection returned by GET /api/lesson-plans) ─────────

data class LessonPlanSummary(
    val _id: String,
    val topic: String,
    val subject: String,
    val grade: String,
    val board: SyllabusBoard,
    val chapterName: String? = null,
    val difficultyLevel: LessonDifficultyLevel,
    val estimatedDurationMinutes: Int,
    val simulation: SimulationFeasibleOnly,
    val mcq: McqFeasibleOnly,
    val notesGeneratable: Boolean,
    val createdAt: String
) {
    val id: String get() = _id
}

fun LessonPlan.toSummary(): LessonPlanSummary = LessonPlanSummary(
    _id = _id,
    topic = topic,
    subject = subject,
    grade = grade,
    board = board,
    chapterName = chapterName,
    difficultyLevel = difficultyLevel,
    estimatedDurationMinutes = estimatedDurationMinutes,
    simulation = SimulationFeasibleOnly(simulation.feasible),
    mcq = McqFeasibleOnly(mcq.feasible),
    notesGeneratable = notesGeneratable,
    createdAt = createdAt
)

data class SimulationFeasibleOnly(val feasible: Boolean)
data class McqFeasibleOnly(val feasible: Boolean)

data class PaginationMeta(
    val total: Int,
    val page: Int,
    val limit: Int,
    val totalPages: Int
)

// Request payloads

data class GenerateLessonPlanRequest(
    val board: SyllabusBoard,
    val subject: String,
    val grade: String,
    val topic: String,
    val chapterName: String? = null,
    val estimatedDurationMinutes: Int? = null
)

// API envelope wrappers (mirror backend's { success, data } shape)

data class ApiSuccess<T>(
    val success: Boolean,
    val data: T
)

data class LessonPlanListData(
    val plans: List<LessonPlanSummary>,
    val pagination: PaginationMeta
)

data class ApiErrorBody(
    val success: Boolean,
    val error: ApiErrorDetail
)

data class ApiErrorDetail(
    val code: String,
    val message: String,
    val details: Any? = null
)
