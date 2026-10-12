package com.example.myapplication.data.model

/**
 * Educational content artifact being delivered to a student cohort or batch.
 */
enum class DeliveryContentType(val label: String) {
    SIMULATION("Simulation"),
    LESSON_PLAN("Lesson Plan"),
    MCQ("MCQ Set"),
    STORYLINE("StoryLine")
}

/**
 * Delivery fulfillment status.
 */
enum class DeliveryStatus(val label: String) {
    SCHEDULED("Scheduled"),
    SENT("Sent"),
    COMPLETED("Completed")
}

/**
 * Representation of a scheduled or delivered educational content package.
 *
 * @property id Unique delivery identifier.
 * @property title Name of the simulation, lesson plan, or assessment.
 * @property contentType Type of content artifact.
 * @property targetBatch Target class or cohort (e.g., "Class 10 - Batch A").
 * @property scheduledDateFormatted Date string representing delivery schedule.
 * @property status Current delivery lifecycle status.
 */
data class DeliveryItem(
    val id: String,
    val title: String,
    val contentType: DeliveryContentType,
    val targetBatch: String,
    val scheduledDateFormatted: String,
    val status: DeliveryStatus = DeliveryStatus.SCHEDULED
)
