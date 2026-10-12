package com.example.myapplication.data.repo

import com.example.myapplication.data.model.MindMapDetail
import com.example.myapplication.data.model.MindMapNode
import com.example.myapplication.data.model.MindMapSummary
import kotlinx.coroutines.delay

/**
 * Repository interface for retrieving and managing educational Mind Maps.
 */
interface MindMapRepository {
    /**
     * Retrieves Mind Maps matching optional board and subject filters.
     */
    suspend fun list(board: String? = null, subject: String? = null): Result<List<MindMapSummary>>

    /**
     * Retrieves the hierarchical concept graph for a specific Mind Map.
     */
    suspend fun getMindMapDetail(id: String): Result<MindMapDetail>

    /**
     * Deletes a Mind Map by its unique identifier.
     */
    suspend fun delete(id: String): Result<Unit>

    /**
     * Generates a new Mind Map based on curriculum and user options.
     */
    suspend fun create(
        curriculum: String,
        depth: Int = 4,
        focusThemes: String? = null,
        title: String? = null
    ): Result<MindMapSummary>
}

/**
 * In-memory mock repository simulating network fetching of Mind Maps.
 *
 * @param withSamples Whether to populate initial mock data matching the UI designs.
 */
class FakeMindMapRepository(private val withSamples: Boolean = true) : MindMapRepository {

    private val samples = mutableListOf(
        MindMapSummary(
            id = "6ac4d3685a846603eaff4931",
            title = "Class 10 Mathematics (SEBA)",
            subject = "Mathematics",
            board = "SEBA",
            grade = "Class 10",
            nodeCount = 22,
            levelCount = 4,
            isRagEnabled = true
        ),
        MindMapSummary(
            id = "mm_001",
            title = "Class 10 Mathematics (SEBA)",
            subject = "Mathematics",
            board = "SEBA",
            grade = "Class 10",
            nodeCount = 2,
            levelCount = 2,
            isRagEnabled = true
        ),
        MindMapSummary(
            id = "mm_003",
            title = "Class 9 Science: Laws of Motion",
            subject = "Science",
            board = "CBSE",
            grade = "Class 9",
            nodeCount = 14,
            levelCount = 3,
            isRagEnabled = true
        ),
        MindMapSummary(
            id = "mm_004",
            title = "Class 10 Social Science: Nationalism",
            subject = "Social Science",
            board = "NCERT",
            grade = "Class 10",
            nodeCount = 19,
            levelCount = 4,
            isRagEnabled = true
        )
    )

    override suspend fun list(board: String?, subject: String?): Result<List<MindMapSummary>> {
        delay(350)
        if (!withSamples) return Result.success(emptyList())

        val filtered = samples.filter { item ->
            val matchesBoard = board.isNullOrBlank() || item.board.equals(board, ignoreCase = true)
            val matchesSubject = subject.isNullOrBlank() || item.subject.equals(subject, ignoreCase = true)
            matchesBoard && matchesSubject
        }
        return Result.success(filtered)
    }

    override suspend fun getMindMapDetail(id: String): Result<MindMapDetail> {
        delay(300)
        val sampleGraph = MindMapDetail(
            id = id,
            title = "Class 10 Mathematics (SEBA)",
            subject = "Mathematics",
            board = "SEBA",
            grade = "Class 10",
            nodeCount = 22,
            root = MindMapNode(
                id = "node_root",
                label = "Class 10 Mathematics (SEBA)",
                level = 0,
                children = listOf(
                    MindMapNode(
                        id = "node_real_numbers",
                        label = "Real Numbers",
                        level = 1,
                        children = listOf(
                            MindMapNode(
                                id = "node_fta",
                                label = "Fundamental Theorem of\nArithmetic",
                                level = 2,
                                children = listOf(
                                    MindMapNode("node_fta_1", "Unique Prime Factorisation", level = 3),
                                    MindMapNode("node_fta_2", "Example: 32760 = 2³ × 3² × 5 ×\n7 × 13", level = 3),
                                    MindMapNode("node_fta_3", "Historical Note: Gauss & Euclid", level = 3),
                                    MindMapNode("node_fta_4", "Application: 4^n never ends in\nzero", level = 3)
                                )
                            ),
                            MindMapNode(
                                id = "node_hcf_lcm",
                                label = "HCF and LCM via Prime\nFactorisation",
                                level = 2,
                                children = listOf(
                                    MindMapNode("node_hl_1", "HCF: smallest power of common\nprimes", level = 3),
                                    MindMapNode("node_hl_2", "LCM: greatest power of all\nprimes", level = 3),
                                    MindMapNode("node_hl_3", "Relation: HCF × LCM = a × b", level = 3),
                                    MindMapNode("node_hl_4", "Application: Seating problem\n(60, 84, 108)", level = 3)
                                )
                            ),
                            MindMapNode(
                                id = "node_irrational",
                                label = "Revisiting Irrational Numbers",
                                level = 2,
                                children = listOf(
                                    MindMapNode("node_irr_1", "Theorem: If p divides a², p divides a", level = 3),
                                    MindMapNode("node_irr_2", "Proof by contradiction: √2 is irrational", level = 3),
                                    MindMapNode("node_irr_3", "Properties: rational + irrational is irrational", level = 3)
                                )
                            ),
                            MindMapNode(
                                id = "node_decimal",
                                label = "Decimal Expansions of\nRational Numbers",
                                level = 2,
                                children = listOf(
                                    MindMapNode("node_dec_1", "Terminating: denominator of form 2^n × 5^m", level = 3),
                                    MindMapNode("node_dec_2", "Non-terminating repeating: prime factor not 2 or 5", level = 3)
                                )
                            )
                        )
                    )
                )
            )
        )
        return Result.success(sampleGraph)
    }

    override suspend fun delete(id: String): Result<Unit> {
        delay(150)
        samples.removeAll { it.id == id }
        return Result.success(Unit)
    }

    override suspend fun create(
        curriculum: String,
        depth: Int,
        focusThemes: String?,
        title: String?
    ): Result<MindMapSummary> {
        delay(600)
        val parts = curriculum.split("·").map { it.trim() }
        val grade = parts.getOrNull(0) ?: "Class 10"
        val subjectWithBoard = parts.getOrNull(1) ?: "Science"
        val subject = subjectWithBoard.substringBefore("(").trim()
        val board = if (subjectWithBoard.contains("(") && subjectWithBoard.contains(")")) {
            subjectWithBoard.substringAfter("(").substringBefore(")").trim()
        } else "SEBA"

        val effectiveTitle = if (!title.isNullOrBlank()) {
            title.trim()
        } else {
            "$grade $subject ($board)"
        }

        val id = "mm_${System.currentTimeMillis()}"
        val newMap = MindMapSummary(
            id = id,
            title = effectiveTitle,
            subject = subject,
            board = board,
            grade = grade,
            nodeCount = depth * 5 + 2,
            levelCount = depth,
            isRagEnabled = true,
            createdAt = "Oct 12, 2026"
        )
        samples.add(0, newMap)
        return Result.success(newMap)
    }
}
