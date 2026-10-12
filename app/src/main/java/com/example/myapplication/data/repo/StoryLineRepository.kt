package com.example.myapplication.data.repo

import com.example.myapplication.data.model.StoryLineSummary
import kotlinx.coroutines.delay

/**
 * Repository interface for managing StoryLine content.
 */
interface StoryLineRepository {
    /**
     * Retrieves all saved StoryLine modules.
     */
    suspend fun list(): Result<List<StoryLineSummary>>

    /**
     * Deletes a StoryLine module by its identifier.
     */
    suspend fun delete(id: String): Result<Unit>

    /**
     * Creates and saves a new StoryLine module.
     */
    suspend fun create(
        topic: String,
        subject: String,
        grade: String,
        actCount: Int = 4,
        generateIllustrations: Boolean = false
    ): Result<StoryLineSummary>

    /**
     * Fetches a StoryLine module by its identifier.
     */
    suspend fun getById(id: String): Result<StoryLineSummary?>
}

/**
 * In-memory repository implementation simulating network operations and sample StoryLines.
 *
 * @param withSamples Whether to populate initial mock data.
 */
class FakeStoryLineRepository(private val withSamples: Boolean = true) : StoryLineRepository {

    private val samples = mutableListOf(
        StoryLineSummary(
            id = "story_001",
            title = "Ohm's Law",
            subject = "Physics",
            board = "SEBA",
            grade = "Class 11",
            actCount = 4
        ),
        StoryLineSummary(
            id = "story_002",
            title = "Decomposition Reactions",
            subject = "Chemistry",
            board = "SEBA",
            grade = "Class 10",
            actCount = 3
        ),
        StoryLineSummary(
            id = "story_003",
            title = "Newton's Third Law",
            subject = "Physics",
            board = "CBSE",
            grade = "Class 9",
            actCount = 4
        )
    )

    override suspend fun list(): Result<List<StoryLineSummary>> {
        delay(300)
        return if (withSamples) {
            Result.success(samples.toList())
        } else {
            Result.success(emptyList())
        }
    }

    override suspend fun delete(id: String): Result<Unit> {
        delay(150)
        samples.removeAll { it.id == id }
        return Result.success(Unit)
    }

    override suspend fun create(
        topic: String,
        subject: String,
        grade: String,
        actCount: Int,
        generateIllustrations: Boolean
    ): Result<StoryLineSummary> {
        delay(600)
        val title = topic.trim().ifEmpty { "StoryLine: $subject" }
        val generated = StoryLineSummary(
            id = "story_${System.currentTimeMillis()}",
            title = title,
            subject = subject,
            board = "SEBA",
            grade = grade,
            actCount = actCount
        )
        samples.add(0, generated)
        return Result.success(generated)
    }

    override suspend fun getById(id: String): Result<StoryLineSummary?> {
        delay(150)
        return Result.success(samples.find { it.id == id })
    }
}
