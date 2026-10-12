package com.example.myapplication.data.repo

import com.example.myapplication.data.model.SimulationSummary
import com.example.myapplication.data.model.SimulationVisualType
import kotlinx.coroutines.delay

/**
 * Repository contract for fetching and mutating PlayableCraft AI simulations.
 */
interface SimulationRepository {
    /**
     * Lists simulations matching an optional search term.
     */
    suspend fun list(query: String? = null): Result<List<SimulationSummary>>

    /**
     * Removes a simulation by its identifier.
     */
    suspend fun delete(id: String): Result<Unit>

    /**
     * Generates and persists a new simulation based on user prompts.
     */
    suspend fun create(
        grade: String,
        subject: String,
        chapter: String,
        topicPrompt: String? = null
    ): Result<SimulationSummary>

    /**
     * Finds a simulation summary by its unique identifier.
     */
    suspend fun getById(id: String): Result<SimulationSummary?>
}

/**
 * In-memory repository implementation simulating backend responses for interactive simulations.
 *
 * @param withSamples Whether to populate initial mock data matching the UI designs.
 */
class FakeSimulationRepository(private val withSamples: Boolean = true) : SimulationRepository {

    private val samples = mutableListOf(
        SimulationSummary(
            id = "sim_001",
            title = "Decomposition Reactions",
            board = "SEBA",
            chapter = "Chemical Reactions and Equations",
            subject = "Chemistry",
            formattedDate = "Sep 30, 2026",
            visualType = SimulationVisualType.MOLECULE
        ),
        SimulationSummary(
            id = "sim_002",
            title = "Projectile Motion & Gravity",
            board = "CBSE",
            chapter = "Laws of Motion",
            subject = "Physics",
            formattedDate = "Sep 28, 2026",
            visualType = SimulationVisualType.PHYSICS_ORBIT
        ),
        SimulationSummary(
            id = "sim_003",
            title = "Refraction & Lens Focal Length",
            board = "NCERT",
            chapter = "Light: Reflection and Refraction",
            subject = "Physics",
            formattedDate = "Sep 24, 2026",
            visualType = SimulationVisualType.WAVE_OPTICS
        )
    )

    override suspend fun list(query: String?): Result<List<SimulationSummary>> {
        delay(350)
        if (!withSamples) return Result.success(emptyList())

        val sanitizedQuery = query?.trim()?.lowercase().orEmpty()
        val filtered = if (sanitizedQuery.isEmpty()) {
            samples.toList()
        } else {
            samples.filter {
                it.title.lowercase().contains(sanitizedQuery) ||
                    it.chapter.lowercase().contains(sanitizedQuery) ||
                    it.subject.lowercase().contains(sanitizedQuery) ||
                    it.board.lowercase().contains(sanitizedQuery)
            }
        }
        return Result.success(filtered)
    }

    override suspend fun delete(id: String): Result<Unit> {
        delay(200)
        samples.removeAll { it.id == id }
        return Result.success(Unit)
    }

    override suspend fun create(
        grade: String,
        subject: String,
        chapter: String,
        topicPrompt: String?
    ): Result<SimulationSummary> {
        delay(600)
        val visual = when {
            subject.contains("Chem", ignoreCase = true) || chapter.contains("Chem", ignoreCase = true) -> SimulationVisualType.MOLECULE
            chapter.contains("Light", ignoreCase = true) || chapter.contains("Wave", ignoreCase = true) || chapter.contains("Refraction", ignoreCase = true) -> SimulationVisualType.WAVE_OPTICS
            subject.contains("Phys", ignoreCase = true) || chapter.contains("Motion", ignoreCase = true) || chapter.contains("Gravit", ignoreCase = true) -> SimulationVisualType.PHYSICS_ORBIT
            else -> SimulationVisualType.GENERIC
        }

        val title = if (!topicPrompt.isNullOrBlank()) {
            topicPrompt.trim().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        } else {
            chapter
        }

        val generated = SimulationSummary(
            id = "sim_${System.currentTimeMillis()}",
            title = title,
            board = grade,
            chapter = chapter,
            subject = subject,
            formattedDate = "Oct 12, 2026",
            visualType = visual
        )
        samples.add(0, generated)
        return Result.success(generated)
    }

    override suspend fun getById(id: String): Result<SimulationSummary?> {
        delay(150)
        return Result.success(samples.find { it.id == id })
    }
}
