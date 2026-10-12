package com.example.myapplication.data.model

/**
 * Representation of an individual node in the Mind Map hierarchy tree.
 *
 * @property id Unique node identifier.
 * @property label Node textual label or mathematical expression.
 * @property level Depth level (0 = root, 1 = chapter/topic, 2 = subtopic, 3 = leaf detail).
 * @property children Nested sub-nodes.
 */
data class MindMapNode(
    val id: String,
    val label: String,
    val level: Int = 0,
    val children: List<MindMapNode> = emptyList()
)

/**
 * Complete document structure of an interactive concept Mind Map.
 *
 * @property id Unique identifier.
 * @property title Mind Map title.
 * @property subject Academic discipline.
 * @property board Syllabus board.
 * @property grade Educational grade.
 * @property nodeCount Total number of nodes.
 * @property root Root concept node.
 */
data class MindMapDetail(
    val id: String,
    val title: String,
    val subject: String,
    val board: String,
    val grade: String,
    val nodeCount: Int,
    val root: MindMapNode
) {
    /**
     * Uppercase breadcrumb context:
     * e.g. "SEBA  ·  MATHEMATICS  ·  CLASS 10  ·  22 NODES"
     */
    val breadcrumb: String
        get() = "${board.uppercase()}  ·  ${subject.uppercase()}  ·  ${grade.uppercase()}  ·  $nodeCount NODES"
}

/**
 * Representation of a saved concept Mind Map summary.
 *
 * @property id Unique identifier of the Mind Map.
 * @property title Mind Map title (e.g., "Class 10 Mathematics (SEBA)").
 * @property subject Academic discipline (e.g., "Mathematics").
 * @property board Syllabus board (e.g., "SEBA").
 * @property grade Educational grade or class level (e.g., "Class 10").
 * @property nodeCount Total number of concept nodes in the graph.
 * @property levelCount Depth or hierarchy levels of the concept map.
 * @property isRagEnabled Whether Retrieval-Augmented Generation context from syllabus docs was used.
 * @property createdAt Timestamp when this Mind Map was generated.
 */
data class MindMapSummary(
    val id: String,
    val title: String,
    val subject: String,
    val board: String,
    val grade: String,
    val nodeCount: Int,
    val levelCount: Int,
    val isRagEnabled: Boolean = true,
    val createdAt: String = ""
) {
    /**
     * Uppercase breadcrumb context:
     * e.g. "SEBA  ·  MATHEMATICS  ·  CLASS 10"
     */
    val breadcrumb: String
        get() = "${board.uppercase()}  ·  ${subject.uppercase()}  ·  ${grade.uppercase()}"

    /**
     * Metric description:
     * e.g. "22 nodes · 4 levels"
     */
    val statsDescription: String
        get() = "$nodeCount nodes · $levelCount levels"
}

/**
 * Filter criteria for querying saved Mind Maps.
 */
data class MindMapFilter(
    val board: String? = null,
    val subject: String? = null
)
