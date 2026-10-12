package com.example.myapplication.data.model

/**
 * Representation of a saved StoryLine module.
 *
 * @property id Unique identifier for the StoryLine.
 * @property title Story topic title (e.g., "Ohm's Law").
 * @property subject Academic discipline (e.g., "Physics").
 * @property board Educational board (e.g., "SEBA").
 * @property grade Educational level or class (e.g., "Class 11").
 * @property actCount Number of story acts or interactive narrative segments.
 * @property createdAt Timestamp when this story was created.
 */
data class StoryLineSummary(
    val id: String,
    val title: String,
    val subject: String,
    val board: String,
    val grade: String,
    val actCount: Int = 4,
    val createdAt: String = ""
) {
    /**
     * Formats the breadcrumb string matching the app design:
     * e.g. "PHYSICS  ·  SEBA  ·  CLASS 11  ·  4 ACTS"
     */
    val breadcrumb: String
        get() = "${subject.uppercase()}  ·  ${board.uppercase()}  ·  ${grade.uppercase()}  ·  $actCount ACTS"
}
