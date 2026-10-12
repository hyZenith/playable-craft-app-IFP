package com.example.myapplication.data.model

/**
 * Top-level view category selected on the Teacher Context page.
 */
enum class TeacherContextTab(val label: String) {
    VIDEOS("Videos"),
    PROMPT_ENGINEERING("Prompt Engineering")
}

/**
 * Highlight badge displayed on instructional walkthrough cards.
 */
enum class VideoBadgeType(val label: String) {
    START_HERE("START HERE"),
    EVERYDAY("EVERYDAY")
}

/**
 * Video walkthrough guide teaching syllabus alignment and generator controls.
 *
 * @property id Unique identifier.
 * @property title Heading of the walkthrough module.
 * @property description Detailed explanation of syllabus scope or generator behaviour.
 * @property featureCategory Target platform feature (e.g., "PlayableCraft AI", "Lesson Plan").
 * @property badge Classification badge (e.g., START_HERE, EVERYDAY).
 * @property duration Formatted video duration (e.g., "6:20", "4:45").
 * @property isPlayable Whether the recording has landed and is playable.
 */
data class VideoWalkthrough(
    val id: String,
    val title: String,
    val description: String,
    val featureCategory: String,
    val badge: VideoBadgeType,
    val duration: String,
    val isPlayable: Boolean = false
)

/**
 * Practical prompt engineering template and explanation for classroom generation.
 *
 * @property id Unique identifier.
 * @property title Guide topic (e.g., "Scoping Chapter Boundaries").
 * @property category Feature category.
 * @property bestPractice Guidance on how to frame teacher prompts.
 * @property examplePrompt Concrete prompt syntax.
 */
data class PromptGuide(
    val id: String,
    val title: String,
    val category: String,
    val bestPractice: String,
    val examplePrompt: String
)

/**
 * Concrete prompt engineering rule card comparing weak vs better phrasing.
 *
 * @property id Unique identifier.
 * @property index Formatted sequence number (e.g., "01", "02").
 * @property title Core rule title.
 * @property subtitle Concise guideline explanation.
 * @property weakText Anti-pattern prompt example.
 * @property betterText Recommended focused prompt example.
 * @property whyExplanation Pedagogical and generator reasoning for the rule.
 */
data class PromptRule(
    val id: String,
    val index: String,
    val title: String,
    val subtitle: String,
    val weakText: String,
    val betterText: String,
    val whyExplanation: String
)

