package com.example.myapplication.data.model

/**
 * Visual graphic style rendered on the simulation card thumbnail preview.
 */
enum class SimulationVisualType {
    MOLECULE,
    PHYSICS_ORBIT,
    WAVE_OPTICS,
    GENERIC
}

/**
 * Representation of a saved playable interactive simulation.
 *
 * @property id Unique identifier of the simulation.
 * @property title Name of the simulation topic (e.g., "Decomposition Reactions").
 * @property board Syllabus board (e.g., "SEBA", "CBSE").
 * @property chapter Context chapter or syllabus unit (e.g., "Chemical Reactions and Equations").
 * @property subject Academic discipline (e.g., "Chemistry", "Physics").
 * @property formattedDate Display-friendly creation or modification date (e.g., "Sep 30, 2026").
 * @property visualType Type of graphic illustration displayed in the preview thumbnail.
 */
data class SimulationSummary(
    val id: String,
    val title: String,
    val board: String,
    val chapter: String,
    val subject: String,
    val formattedDate: String,
    val visualType: SimulationVisualType = SimulationVisualType.MOLECULE
)
