package com.example.myapplication.data.mock

import com.example.myapplication.data.model.*

object MockMcqsData {
    val items: List<Mcq> = listOf(
        Mcq(
            _id = "cccccccccccccccccccccccc",
            board = "SEBA",
            subject = "Science",
            grade = "Class 8",
            chapterNumbers = listOf(1),
            chapterNames = listOf("Chapter 1 — Motion"),
            sourceType = "rag",
            lessonPlanId = null,
            ragGrounded = true,
            seedThemes = listOf("Motion"),
            title = "Science — Class 8 MCQ (20 Q) — Motion",
            questionCount = 20,
            optionsPerQuestion = 5,
            difficulty = "hard",
            timer = McqTimer(enabled = true, secondsPerQuestion = 60),
            questions = listOf(
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q1)",
                    options = listOf("Option A for question 1", "Option B for question 1", "Option C for question 1", "Option D for question 1", "Option E for question 1"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of motion.",
                    difficulty = "easy",
                    sourceChunkRef = "Motion — para 1"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q2)",
                    options = listOf("Option A for question 2", "Option B for question 2", "Option C for question 2", "Option D for question 2", "Option E for question 2"),
                    correctIndex = 4,
                    explanation = "Option E correctly reflects the concept of motion.",
                    difficulty = "medium",
                    sourceChunkRef = "Motion — para 2"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q3)",
                    options = listOf("Option A for question 3", "Option B for question 3", "Option C for question 3", "Option D for question 3", "Option E for question 3"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of motion.",
                    difficulty = "easy",
                    sourceChunkRef = "Motion — para 3"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q4)",
                    options = listOf("Option A for question 4", "Option B for question 4", "Option C for question 4", "Option D for question 4", "Option E for question 4"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of motion.",
                    difficulty = "easy",
                    sourceChunkRef = "Motion — para 4"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q5)",
                    options = listOf("Option A for question 5", "Option B for question 5", "Option C for question 5", "Option D for question 5", "Option E for question 5"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of motion.",
                    difficulty = "hard",
                    sourceChunkRef = "Motion — para 5"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q6)",
                    options = listOf("Option A for question 6", "Option B for question 6", "Option C for question 6", "Option D for question 6", "Option E for question 6"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of motion.",
                    difficulty = "hard",
                    sourceChunkRef = "Motion — para 6"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q7)",
                    options = listOf("Option A for question 7", "Option B for question 7", "Option C for question 7", "Option D for question 7", "Option E for question 7"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of motion.",
                    difficulty = "medium",
                    sourceChunkRef = "Motion — para 7"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q8)",
                    options = listOf("Option A for question 8", "Option B for question 8", "Option C for question 8", "Option D for question 8", "Option E for question 8"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of motion.",
                    difficulty = "hard",
                    sourceChunkRef = "Motion — para 8"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q9)",
                    options = listOf("Option A for question 9", "Option B for question 9", "Option C for question 9", "Option D for question 9", "Option E for question 9"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of motion.",
                    difficulty = "hard",
                    sourceChunkRef = "Motion — para 9"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q10)",
                    options = listOf("Option A for question 10", "Option B for question 10", "Option C for question 10", "Option D for question 10", "Option E for question 10"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of motion.",
                    difficulty = "hard",
                    sourceChunkRef = "Motion — para 10"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q11)",
                    options = listOf("Option A for question 11", "Option B for question 11", "Option C for question 11", "Option D for question 11", "Option E for question 11"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of motion.",
                    difficulty = "hard",
                    sourceChunkRef = "Motion — para 11"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q12)",
                    options = listOf("Option A for question 12", "Option B for question 12", "Option C for question 12", "Option D for question 12", "Option E for question 12"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of motion.",
                    difficulty = "hard",
                    sourceChunkRef = "Motion — para 12"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q13)",
                    options = listOf("Option A for question 13", "Option B for question 13", "Option C for question 13", "Option D for question 13", "Option E for question 13"),
                    correctIndex = 4,
                    explanation = "Option E correctly reflects the concept of motion.",
                    difficulty = "medium",
                    sourceChunkRef = "Motion — para 13"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q14)",
                    options = listOf("Option A for question 14", "Option B for question 14", "Option C for question 14", "Option D for question 14", "Option E for question 14"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of motion.",
                    difficulty = "easy",
                    sourceChunkRef = "Motion — para 14"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q15)",
                    options = listOf("Option A for question 15", "Option B for question 15", "Option C for question 15", "Option D for question 15", "Option E for question 15"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of motion.",
                    difficulty = "medium",
                    sourceChunkRef = "Motion — para 15"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q16)",
                    options = listOf("Option A for question 16", "Option B for question 16", "Option C for question 16", "Option D for question 16", "Option E for question 16"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of motion.",
                    difficulty = "medium",
                    sourceChunkRef = "Motion — para 16"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q17)",
                    options = listOf("Option A for question 17", "Option B for question 17", "Option C for question 17", "Option D for question 17", "Option E for question 17"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of motion.",
                    difficulty = "hard",
                    sourceChunkRef = "Motion — para 17"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q18)",
                    options = listOf("Option A for question 18", "Option B for question 18", "Option C for question 18", "Option D for question 18", "Option E for question 18"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of motion.",
                    difficulty = "hard",
                    sourceChunkRef = "Motion — para 18"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q19)",
                    options = listOf("Option A for question 19", "Option B for question 19", "Option C for question 19", "Option D for question 19", "Option E for question 19"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of motion.",
                    difficulty = "hard",
                    sourceChunkRef = "Motion — para 19"
                ),
                McqQuestion(
                    question = "Regarding Motion, which statement is correct? (Q20)",
                    options = listOf("Option A for question 20", "Option B for question 20", "Option C for question 20", "Option D for question 20", "Option E for question 20"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of motion.",
                    difficulty = "hard",
                    sourceChunkRef = "Motion — para 20"
                ),
            ),
            aiModel = "claude-sonnet-4-6",
            generationDurationMs = 17779L,
            createdAt = "2026-09-10T12:00:00.000Z",
            updatedAt = "2026-09-10T12:00:00.000Z"
        ),
        Mcq(
            _id = "555555555555555555555555",
            board = "NCERT",
            subject = "Mathematics",
            grade = "Class 9",
            chapterNumbers = listOf(2),
            chapterNames = listOf("Chapter 2 — Gravitation"),
            sourceType = "rag",
            lessonPlanId = null,
            ragGrounded = true,
            seedThemes = listOf("Gravitation"),
            title = "Mathematics — Class 9 MCQ (10 Q) — Gravitation",
            questionCount = 10,
            optionsPerQuestion = 4,
            difficulty = "medium",
            timer = McqTimer(enabled = false, secondsPerQuestion = null),
            questions = listOf(
                McqQuestion(
                    question = "Regarding Gravitation, which statement is correct? (Q1)",
                    options = listOf("Option A for question 1", "Option B for question 1", "Option C for question 1", "Option D for question 1"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of gravitation.",
                    difficulty = "hard",
                    sourceChunkRef = "Gravitation — para 1"
                ),
                McqQuestion(
                    question = "Regarding Gravitation, which statement is correct? (Q2)",
                    options = listOf("Option A for question 2", "Option B for question 2", "Option C for question 2", "Option D for question 2"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of gravitation.",
                    difficulty = "medium",
                    sourceChunkRef = "Gravitation — para 2"
                ),
                McqQuestion(
                    question = "Regarding Gravitation, which statement is correct? (Q3)",
                    options = listOf("Option A for question 3", "Option B for question 3", "Option C for question 3", "Option D for question 3"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of gravitation.",
                    difficulty = "easy",
                    sourceChunkRef = "Gravitation — para 3"
                ),
                McqQuestion(
                    question = "Regarding Gravitation, which statement is correct? (Q4)",
                    options = listOf("Option A for question 4", "Option B for question 4", "Option C for question 4", "Option D for question 4"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of gravitation.",
                    difficulty = "medium",
                    sourceChunkRef = "Gravitation — para 4"
                ),
                McqQuestion(
                    question = "Regarding Gravitation, which statement is correct? (Q5)",
                    options = listOf("Option A for question 5", "Option B for question 5", "Option C for question 5", "Option D for question 5"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of gravitation.",
                    difficulty = "easy",
                    sourceChunkRef = "Gravitation — para 5"
                ),
                McqQuestion(
                    question = "Regarding Gravitation, which statement is correct? (Q6)",
                    options = listOf("Option A for question 6", "Option B for question 6", "Option C for question 6", "Option D for question 6"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of gravitation.",
                    difficulty = "hard",
                    sourceChunkRef = "Gravitation — para 6"
                ),
                McqQuestion(
                    question = "Regarding Gravitation, which statement is correct? (Q7)",
                    options = listOf("Option A for question 7", "Option B for question 7", "Option C for question 7", "Option D for question 7"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of gravitation.",
                    difficulty = "easy",
                    sourceChunkRef = "Gravitation — para 7"
                ),
                McqQuestion(
                    question = "Regarding Gravitation, which statement is correct? (Q8)",
                    options = listOf("Option A for question 8", "Option B for question 8", "Option C for question 8", "Option D for question 8"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of gravitation.",
                    difficulty = "easy",
                    sourceChunkRef = "Gravitation — para 8"
                ),
                McqQuestion(
                    question = "Regarding Gravitation, which statement is correct? (Q9)",
                    options = listOf("Option A for question 9", "Option B for question 9", "Option C for question 9", "Option D for question 9"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of gravitation.",
                    difficulty = "easy",
                    sourceChunkRef = "Gravitation — para 9"
                ),
                McqQuestion(
                    question = "Regarding Gravitation, which statement is correct? (Q10)",
                    options = listOf("Option A for question 10", "Option B for question 10", "Option C for question 10", "Option D for question 10"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of gravitation.",
                    difficulty = "hard",
                    sourceChunkRef = "Gravitation — para 10"
                ),
            ),
            aiModel = "claude-sonnet-4-6",
            generationDurationMs = 13410L,
            createdAt = "2026-09-12T11:00:00.000Z",
            updatedAt = "2026-09-12T11:00:00.000Z"
        ),
        Mcq(
            _id = "dddddddddddddddddddddddd",
            board = "CBSE",
            subject = "Physics",
            grade = "Class 10",
            chapterNumbers = listOf(3),
            chapterNames = listOf("Chapter 3 — Work and Energy"),
            sourceType = "rag",
            lessonPlanId = null,
            ragGrounded = true,
            seedThemes = listOf("Work and Energy"),
            title = "Physics — Class 10 MCQ (20 Q) — Work and Energy",
            questionCount = 20,
            optionsPerQuestion = 5,
            difficulty = "mixed",
            timer = McqTimer(enabled = true, secondsPerQuestion = 60),
            questions = listOf(
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q1)",
                    options = listOf("Option A for question 1", "Option B for question 1", "Option C for question 1", "Option D for question 1", "Option E for question 1"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of work and energy.",
                    difficulty = "hard",
                    sourceChunkRef = "Work and Energy — para 1"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q2)",
                    options = listOf("Option A for question 2", "Option B for question 2", "Option C for question 2", "Option D for question 2", "Option E for question 2"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of work and energy.",
                    difficulty = "hard",
                    sourceChunkRef = "Work and Energy — para 2"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q3)",
                    options = listOf("Option A for question 3", "Option B for question 3", "Option C for question 3", "Option D for question 3", "Option E for question 3"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of work and energy.",
                    difficulty = "medium",
                    sourceChunkRef = "Work and Energy — para 3"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q4)",
                    options = listOf("Option A for question 4", "Option B for question 4", "Option C for question 4", "Option D for question 4", "Option E for question 4"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of work and energy.",
                    difficulty = "medium",
                    sourceChunkRef = "Work and Energy — para 4"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q5)",
                    options = listOf("Option A for question 5", "Option B for question 5", "Option C for question 5", "Option D for question 5", "Option E for question 5"),
                    correctIndex = 4,
                    explanation = "Option E correctly reflects the concept of work and energy.",
                    difficulty = "medium",
                    sourceChunkRef = "Work and Energy — para 5"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q6)",
                    options = listOf("Option A for question 6", "Option B for question 6", "Option C for question 6", "Option D for question 6", "Option E for question 6"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of work and energy.",
                    difficulty = "easy",
                    sourceChunkRef = "Work and Energy — para 6"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q7)",
                    options = listOf("Option A for question 7", "Option B for question 7", "Option C for question 7", "Option D for question 7", "Option E for question 7"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of work and energy.",
                    difficulty = "easy",
                    sourceChunkRef = "Work and Energy — para 7"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q8)",
                    options = listOf("Option A for question 8", "Option B for question 8", "Option C for question 8", "Option D for question 8", "Option E for question 8"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of work and energy.",
                    difficulty = "easy",
                    sourceChunkRef = "Work and Energy — para 8"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q9)",
                    options = listOf("Option A for question 9", "Option B for question 9", "Option C for question 9", "Option D for question 9", "Option E for question 9"),
                    correctIndex = 4,
                    explanation = "Option E correctly reflects the concept of work and energy.",
                    difficulty = "hard",
                    sourceChunkRef = "Work and Energy — para 9"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q10)",
                    options = listOf("Option A for question 10", "Option B for question 10", "Option C for question 10", "Option D for question 10", "Option E for question 10"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of work and energy.",
                    difficulty = "hard",
                    sourceChunkRef = "Work and Energy — para 10"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q11)",
                    options = listOf("Option A for question 11", "Option B for question 11", "Option C for question 11", "Option D for question 11", "Option E for question 11"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of work and energy.",
                    difficulty = "easy",
                    sourceChunkRef = "Work and Energy — para 11"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q12)",
                    options = listOf("Option A for question 12", "Option B for question 12", "Option C for question 12", "Option D for question 12", "Option E for question 12"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of work and energy.",
                    difficulty = "hard",
                    sourceChunkRef = "Work and Energy — para 12"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q13)",
                    options = listOf("Option A for question 13", "Option B for question 13", "Option C for question 13", "Option D for question 13", "Option E for question 13"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of work and energy.",
                    difficulty = "easy",
                    sourceChunkRef = "Work and Energy — para 13"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q14)",
                    options = listOf("Option A for question 14", "Option B for question 14", "Option C for question 14", "Option D for question 14", "Option E for question 14"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of work and energy.",
                    difficulty = "easy",
                    sourceChunkRef = "Work and Energy — para 14"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q15)",
                    options = listOf("Option A for question 15", "Option B for question 15", "Option C for question 15", "Option D for question 15", "Option E for question 15"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of work and energy.",
                    difficulty = "easy",
                    sourceChunkRef = "Work and Energy — para 15"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q16)",
                    options = listOf("Option A for question 16", "Option B for question 16", "Option C for question 16", "Option D for question 16", "Option E for question 16"),
                    correctIndex = 4,
                    explanation = "Option E correctly reflects the concept of work and energy.",
                    difficulty = "easy",
                    sourceChunkRef = "Work and Energy — para 16"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q17)",
                    options = listOf("Option A for question 17", "Option B for question 17", "Option C for question 17", "Option D for question 17", "Option E for question 17"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of work and energy.",
                    difficulty = "hard",
                    sourceChunkRef = "Work and Energy — para 17"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q18)",
                    options = listOf("Option A for question 18", "Option B for question 18", "Option C for question 18", "Option D for question 18", "Option E for question 18"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of work and energy.",
                    difficulty = "easy",
                    sourceChunkRef = "Work and Energy — para 18"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q19)",
                    options = listOf("Option A for question 19", "Option B for question 19", "Option C for question 19", "Option D for question 19", "Option E for question 19"),
                    correctIndex = 4,
                    explanation = "Option E correctly reflects the concept of work and energy.",
                    difficulty = "easy",
                    sourceChunkRef = "Work and Energy — para 19"
                ),
                McqQuestion(
                    question = "Regarding Work and Energy, which statement is correct? (Q20)",
                    options = listOf("Option A for question 20", "Option B for question 20", "Option C for question 20", "Option D for question 20", "Option E for question 20"),
                    correctIndex = 4,
                    explanation = "Option E correctly reflects the concept of work and energy.",
                    difficulty = "hard",
                    sourceChunkRef = "Work and Energy — para 20"
                ),
            ),
            aiModel = "claude-sonnet-4-6",
            generationDurationMs = 23488L,
            createdAt = "2026-09-14T19:00:00.000Z",
            updatedAt = "2026-09-14T19:00:00.000Z"
        ),
        Mcq(
            _id = "444444444444444444444444",
            board = "ICSE",
            subject = "Chemistry",
            grade = "Class 11",
            chapterNumbers = listOf(4),
            chapterNames = listOf("Chapter 4 — Chemical Reactions"),
            sourceType = "rag",
            lessonPlanId = null,
            ragGrounded = true,
            seedThemes = listOf("Chemical Reactions"),
            title = "Chemistry — Class 11 MCQ (15 Q) — Chemical Reactions",
            questionCount = 15,
            optionsPerQuestion = 4,
            difficulty = "medium",
            timer = McqTimer(enabled = false, secondsPerQuestion = null),
            questions = listOf(
                McqQuestion(
                    question = "Regarding Chemical Reactions, which statement is correct? (Q1)",
                    options = listOf("Option A for question 1", "Option B for question 1", "Option C for question 1", "Option D for question 1"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of chemical reactions.",
                    difficulty = "easy",
                    sourceChunkRef = "Chemical Reactions — para 1"
                ),
                McqQuestion(
                    question = "Regarding Chemical Reactions, which statement is correct? (Q2)",
                    options = listOf("Option A for question 2", "Option B for question 2", "Option C for question 2", "Option D for question 2"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of chemical reactions.",
                    difficulty = "medium",
                    sourceChunkRef = "Chemical Reactions — para 2"
                ),
                McqQuestion(
                    question = "Regarding Chemical Reactions, which statement is correct? (Q3)",
                    options = listOf("Option A for question 3", "Option B for question 3", "Option C for question 3", "Option D for question 3"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of chemical reactions.",
                    difficulty = "medium",
                    sourceChunkRef = "Chemical Reactions — para 3"
                ),
                McqQuestion(
                    question = "Regarding Chemical Reactions, which statement is correct? (Q4)",
                    options = listOf("Option A for question 4", "Option B for question 4", "Option C for question 4", "Option D for question 4"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of chemical reactions.",
                    difficulty = "hard",
                    sourceChunkRef = "Chemical Reactions — para 4"
                ),
                McqQuestion(
                    question = "Regarding Chemical Reactions, which statement is correct? (Q5)",
                    options = listOf("Option A for question 5", "Option B for question 5", "Option C for question 5", "Option D for question 5"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of chemical reactions.",
                    difficulty = "hard",
                    sourceChunkRef = "Chemical Reactions — para 5"
                ),
                McqQuestion(
                    question = "Regarding Chemical Reactions, which statement is correct? (Q6)",
                    options = listOf("Option A for question 6", "Option B for question 6", "Option C for question 6", "Option D for question 6"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of chemical reactions.",
                    difficulty = "easy",
                    sourceChunkRef = "Chemical Reactions — para 6"
                ),
                McqQuestion(
                    question = "Regarding Chemical Reactions, which statement is correct? (Q7)",
                    options = listOf("Option A for question 7", "Option B for question 7", "Option C for question 7", "Option D for question 7"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of chemical reactions.",
                    difficulty = "hard",
                    sourceChunkRef = "Chemical Reactions — para 7"
                ),
                McqQuestion(
                    question = "Regarding Chemical Reactions, which statement is correct? (Q8)",
                    options = listOf("Option A for question 8", "Option B for question 8", "Option C for question 8", "Option D for question 8"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of chemical reactions.",
                    difficulty = "easy",
                    sourceChunkRef = "Chemical Reactions — para 8"
                ),
                McqQuestion(
                    question = "Regarding Chemical Reactions, which statement is correct? (Q9)",
                    options = listOf("Option A for question 9", "Option B for question 9", "Option C for question 9", "Option D for question 9"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of chemical reactions.",
                    difficulty = "easy",
                    sourceChunkRef = "Chemical Reactions — para 9"
                ),
                McqQuestion(
                    question = "Regarding Chemical Reactions, which statement is correct? (Q10)",
                    options = listOf("Option A for question 10", "Option B for question 10", "Option C for question 10", "Option D for question 10"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of chemical reactions.",
                    difficulty = "hard",
                    sourceChunkRef = "Chemical Reactions — para 10"
                ),
                McqQuestion(
                    question = "Regarding Chemical Reactions, which statement is correct? (Q11)",
                    options = listOf("Option A for question 11", "Option B for question 11", "Option C for question 11", "Option D for question 11"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of chemical reactions.",
                    difficulty = "easy",
                    sourceChunkRef = "Chemical Reactions — para 11"
                ),
                McqQuestion(
                    question = "Regarding Chemical Reactions, which statement is correct? (Q12)",
                    options = listOf("Option A for question 12", "Option B for question 12", "Option C for question 12", "Option D for question 12"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of chemical reactions.",
                    difficulty = "easy",
                    sourceChunkRef = "Chemical Reactions — para 12"
                ),
                McqQuestion(
                    question = "Regarding Chemical Reactions, which statement is correct? (Q13)",
                    options = listOf("Option A for question 13", "Option B for question 13", "Option C for question 13", "Option D for question 13"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of chemical reactions.",
                    difficulty = "medium",
                    sourceChunkRef = "Chemical Reactions — para 13"
                ),
                McqQuestion(
                    question = "Regarding Chemical Reactions, which statement is correct? (Q14)",
                    options = listOf("Option A for question 14", "Option B for question 14", "Option C for question 14", "Option D for question 14"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of chemical reactions.",
                    difficulty = "easy",
                    sourceChunkRef = "Chemical Reactions — para 14"
                ),
                McqQuestion(
                    question = "Regarding Chemical Reactions, which statement is correct? (Q15)",
                    options = listOf("Option A for question 15", "Option B for question 15", "Option C for question 15", "Option D for question 15"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of chemical reactions.",
                    difficulty = "hard",
                    sourceChunkRef = "Chemical Reactions — para 15"
                ),
            ),
            aiModel = "claude-sonnet-4-6",
            generationDurationMs = 11208L,
            createdAt = "2026-09-16T13:00:00.000Z",
            updatedAt = "2026-09-16T13:00:00.000Z"
        ),
        Mcq(
            _id = "888888888888888888888888",
            board = "SEBA",
            subject = "Biology",
            grade = "Class 12",
            chapterNumbers = listOf(5),
            chapterNames = listOf("Chapter 5 — Cell Structure"),
            sourceType = "rag",
            lessonPlanId = null,
            ragGrounded = true,
            seedThemes = listOf("Cell Structure"),
            title = "Biology — Class 12 MCQ (20 Q) — Cell Structure",
            questionCount = 20,
            optionsPerQuestion = 4,
            difficulty = "easy",
            timer = McqTimer(enabled = true, secondsPerQuestion = 60),
            questions = listOf(
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q1)",
                    options = listOf("Option A for question 1", "Option B for question 1", "Option C for question 1", "Option D for question 1"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of cell structure.",
                    difficulty = "easy",
                    sourceChunkRef = "Cell Structure — para 1"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q2)",
                    options = listOf("Option A for question 2", "Option B for question 2", "Option C for question 2", "Option D for question 2"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of cell structure.",
                    difficulty = "medium",
                    sourceChunkRef = "Cell Structure — para 2"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q3)",
                    options = listOf("Option A for question 3", "Option B for question 3", "Option C for question 3", "Option D for question 3"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of cell structure.",
                    difficulty = "medium",
                    sourceChunkRef = "Cell Structure — para 3"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q4)",
                    options = listOf("Option A for question 4", "Option B for question 4", "Option C for question 4", "Option D for question 4"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of cell structure.",
                    difficulty = "medium",
                    sourceChunkRef = "Cell Structure — para 4"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q5)",
                    options = listOf("Option A for question 5", "Option B for question 5", "Option C for question 5", "Option D for question 5"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of cell structure.",
                    difficulty = "easy",
                    sourceChunkRef = "Cell Structure — para 5"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q6)",
                    options = listOf("Option A for question 6", "Option B for question 6", "Option C for question 6", "Option D for question 6"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of cell structure.",
                    difficulty = "easy",
                    sourceChunkRef = "Cell Structure — para 6"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q7)",
                    options = listOf("Option A for question 7", "Option B for question 7", "Option C for question 7", "Option D for question 7"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of cell structure.",
                    difficulty = "medium",
                    sourceChunkRef = "Cell Structure — para 7"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q8)",
                    options = listOf("Option A for question 8", "Option B for question 8", "Option C for question 8", "Option D for question 8"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of cell structure.",
                    difficulty = "medium",
                    sourceChunkRef = "Cell Structure — para 8"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q9)",
                    options = listOf("Option A for question 9", "Option B for question 9", "Option C for question 9", "Option D for question 9"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of cell structure.",
                    difficulty = "hard",
                    sourceChunkRef = "Cell Structure — para 9"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q10)",
                    options = listOf("Option A for question 10", "Option B for question 10", "Option C for question 10", "Option D for question 10"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of cell structure.",
                    difficulty = "easy",
                    sourceChunkRef = "Cell Structure — para 10"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q11)",
                    options = listOf("Option A for question 11", "Option B for question 11", "Option C for question 11", "Option D for question 11"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of cell structure.",
                    difficulty = "medium",
                    sourceChunkRef = "Cell Structure — para 11"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q12)",
                    options = listOf("Option A for question 12", "Option B for question 12", "Option C for question 12", "Option D for question 12"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of cell structure.",
                    difficulty = "easy",
                    sourceChunkRef = "Cell Structure — para 12"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q13)",
                    options = listOf("Option A for question 13", "Option B for question 13", "Option C for question 13", "Option D for question 13"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of cell structure.",
                    difficulty = "hard",
                    sourceChunkRef = "Cell Structure — para 13"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q14)",
                    options = listOf("Option A for question 14", "Option B for question 14", "Option C for question 14", "Option D for question 14"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of cell structure.",
                    difficulty = "easy",
                    sourceChunkRef = "Cell Structure — para 14"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q15)",
                    options = listOf("Option A for question 15", "Option B for question 15", "Option C for question 15", "Option D for question 15"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of cell structure.",
                    difficulty = "hard",
                    sourceChunkRef = "Cell Structure — para 15"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q16)",
                    options = listOf("Option A for question 16", "Option B for question 16", "Option C for question 16", "Option D for question 16"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of cell structure.",
                    difficulty = "hard",
                    sourceChunkRef = "Cell Structure — para 16"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q17)",
                    options = listOf("Option A for question 17", "Option B for question 17", "Option C for question 17", "Option D for question 17"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of cell structure.",
                    difficulty = "easy",
                    sourceChunkRef = "Cell Structure — para 17"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q18)",
                    options = listOf("Option A for question 18", "Option B for question 18", "Option C for question 18", "Option D for question 18"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of cell structure.",
                    difficulty = "easy",
                    sourceChunkRef = "Cell Structure — para 18"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q19)",
                    options = listOf("Option A for question 19", "Option B for question 19", "Option C for question 19", "Option D for question 19"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of cell structure.",
                    difficulty = "hard",
                    sourceChunkRef = "Cell Structure — para 19"
                ),
                McqQuestion(
                    question = "Regarding Cell Structure, which statement is correct? (Q20)",
                    options = listOf("Option A for question 20", "Option B for question 20", "Option C for question 20", "Option D for question 20"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of cell structure.",
                    difficulty = "hard",
                    sourceChunkRef = "Cell Structure — para 20"
                ),
            ),
            aiModel = "claude-sonnet-4-6",
            generationDurationMs = 15707L,
            createdAt = "2026-09-18T17:00:00.000Z",
            updatedAt = "2026-09-18T17:00:00.000Z"
        ),
        Mcq(
            _id = "bbbbbbbbbbbbbbbbbbbbbbbb",
            board = "NCERT",
            subject = "Computer Science",
            grade = "Class 8",
            chapterNumbers = listOf(6),
            chapterNames = listOf("Chapter 6 — Algebraic Expressions"),
            sourceType = "rag",
            lessonPlanId = null,
            ragGrounded = true,
            seedThemes = listOf("Algebraic Expressions"),
            title = "Computer Science — Class 8 MCQ (10 Q) — Algebraic Expressions",
            questionCount = 10,
            optionsPerQuestion = 5,
            difficulty = "medium",
            timer = McqTimer(enabled = false, secondsPerQuestion = null),
            questions = listOf(
                McqQuestion(
                    question = "Regarding Algebraic Expressions, which statement is correct? (Q1)",
                    options = listOf("Option A for question 1", "Option B for question 1", "Option C for question 1", "Option D for question 1", "Option E for question 1"),
                    correctIndex = 4,
                    explanation = "Option E correctly reflects the concept of algebraic expressions.",
                    difficulty = "hard",
                    sourceChunkRef = "Algebraic Expressions — para 1"
                ),
                McqQuestion(
                    question = "Regarding Algebraic Expressions, which statement is correct? (Q2)",
                    options = listOf("Option A for question 2", "Option B for question 2", "Option C for question 2", "Option D for question 2", "Option E for question 2"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of algebraic expressions.",
                    difficulty = "hard",
                    sourceChunkRef = "Algebraic Expressions — para 2"
                ),
                McqQuestion(
                    question = "Regarding Algebraic Expressions, which statement is correct? (Q3)",
                    options = listOf("Option A for question 3", "Option B for question 3", "Option C for question 3", "Option D for question 3", "Option E for question 3"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of algebraic expressions.",
                    difficulty = "medium",
                    sourceChunkRef = "Algebraic Expressions — para 3"
                ),
                McqQuestion(
                    question = "Regarding Algebraic Expressions, which statement is correct? (Q4)",
                    options = listOf("Option A for question 4", "Option B for question 4", "Option C for question 4", "Option D for question 4", "Option E for question 4"),
                    correctIndex = 4,
                    explanation = "Option E correctly reflects the concept of algebraic expressions.",
                    difficulty = "hard",
                    sourceChunkRef = "Algebraic Expressions — para 4"
                ),
                McqQuestion(
                    question = "Regarding Algebraic Expressions, which statement is correct? (Q5)",
                    options = listOf("Option A for question 5", "Option B for question 5", "Option C for question 5", "Option D for question 5", "Option E for question 5"),
                    correctIndex = 4,
                    explanation = "Option E correctly reflects the concept of algebraic expressions.",
                    difficulty = "medium",
                    sourceChunkRef = "Algebraic Expressions — para 5"
                ),
                McqQuestion(
                    question = "Regarding Algebraic Expressions, which statement is correct? (Q6)",
                    options = listOf("Option A for question 6", "Option B for question 6", "Option C for question 6", "Option D for question 6", "Option E for question 6"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of algebraic expressions.",
                    difficulty = "easy",
                    sourceChunkRef = "Algebraic Expressions — para 6"
                ),
                McqQuestion(
                    question = "Regarding Algebraic Expressions, which statement is correct? (Q7)",
                    options = listOf("Option A for question 7", "Option B for question 7", "Option C for question 7", "Option D for question 7", "Option E for question 7"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of algebraic expressions.",
                    difficulty = "easy",
                    sourceChunkRef = "Algebraic Expressions — para 7"
                ),
                McqQuestion(
                    question = "Regarding Algebraic Expressions, which statement is correct? (Q8)",
                    options = listOf("Option A for question 8", "Option B for question 8", "Option C for question 8", "Option D for question 8", "Option E for question 8"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of algebraic expressions.",
                    difficulty = "medium",
                    sourceChunkRef = "Algebraic Expressions — para 8"
                ),
                McqQuestion(
                    question = "Regarding Algebraic Expressions, which statement is correct? (Q9)",
                    options = listOf("Option A for question 9", "Option B for question 9", "Option C for question 9", "Option D for question 9", "Option E for question 9"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of algebraic expressions.",
                    difficulty = "hard",
                    sourceChunkRef = "Algebraic Expressions — para 9"
                ),
                McqQuestion(
                    question = "Regarding Algebraic Expressions, which statement is correct? (Q10)",
                    options = listOf("Option A for question 10", "Option B for question 10", "Option C for question 10", "Option D for question 10", "Option E for question 10"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of algebraic expressions.",
                    difficulty = "medium",
                    sourceChunkRef = "Algebraic Expressions — para 10"
                ),
            ),
            aiModel = "claude-sonnet-4-6",
            generationDurationMs = 18360L,
            createdAt = "2026-09-20T18:00:00.000Z",
            updatedAt = "2026-09-20T18:00:00.000Z"
        ),
        Mcq(
            _id = "555555555555555555555501",
            board = "CBSE",
            subject = "AI",
            grade = "Class 9",
            chapterNumbers = listOf(7),
            chapterNames = listOf("Chapter 7 — Trigonometry Basics"),
            sourceType = "rag",
            lessonPlanId = null,
            ragGrounded = true,
            seedThemes = listOf("Trigonometry Basics"),
            title = "AI — Class 9 MCQ (10 Q) — Trigonometry Basics",
            questionCount = 10,
            optionsPerQuestion = 4,
            difficulty = "easy",
            timer = McqTimer(enabled = true, secondsPerQuestion = 60),
            questions = listOf(
                McqQuestion(
                    question = "Regarding Trigonometry Basics, which statement is correct? (Q1)",
                    options = listOf("Option A for question 1", "Option B for question 1", "Option C for question 1", "Option D for question 1"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of trigonometry basics.",
                    difficulty = "hard",
                    sourceChunkRef = "Trigonometry Basics — para 1"
                ),
                McqQuestion(
                    question = "Regarding Trigonometry Basics, which statement is correct? (Q2)",
                    options = listOf("Option A for question 2", "Option B for question 2", "Option C for question 2", "Option D for question 2"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of trigonometry basics.",
                    difficulty = "hard",
                    sourceChunkRef = "Trigonometry Basics — para 2"
                ),
                McqQuestion(
                    question = "Regarding Trigonometry Basics, which statement is correct? (Q3)",
                    options = listOf("Option A for question 3", "Option B for question 3", "Option C for question 3", "Option D for question 3"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of trigonometry basics.",
                    difficulty = "easy",
                    sourceChunkRef = "Trigonometry Basics — para 3"
                ),
                McqQuestion(
                    question = "Regarding Trigonometry Basics, which statement is correct? (Q4)",
                    options = listOf("Option A for question 4", "Option B for question 4", "Option C for question 4", "Option D for question 4"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of trigonometry basics.",
                    difficulty = "easy",
                    sourceChunkRef = "Trigonometry Basics — para 4"
                ),
                McqQuestion(
                    question = "Regarding Trigonometry Basics, which statement is correct? (Q5)",
                    options = listOf("Option A for question 5", "Option B for question 5", "Option C for question 5", "Option D for question 5"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of trigonometry basics.",
                    difficulty = "medium",
                    sourceChunkRef = "Trigonometry Basics — para 5"
                ),
                McqQuestion(
                    question = "Regarding Trigonometry Basics, which statement is correct? (Q6)",
                    options = listOf("Option A for question 6", "Option B for question 6", "Option C for question 6", "Option D for question 6"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of trigonometry basics.",
                    difficulty = "easy",
                    sourceChunkRef = "Trigonometry Basics — para 6"
                ),
                McqQuestion(
                    question = "Regarding Trigonometry Basics, which statement is correct? (Q7)",
                    options = listOf("Option A for question 7", "Option B for question 7", "Option C for question 7", "Option D for question 7"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of trigonometry basics.",
                    difficulty = "hard",
                    sourceChunkRef = "Trigonometry Basics — para 7"
                ),
                McqQuestion(
                    question = "Regarding Trigonometry Basics, which statement is correct? (Q8)",
                    options = listOf("Option A for question 8", "Option B for question 8", "Option C for question 8", "Option D for question 8"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of trigonometry basics.",
                    difficulty = "hard",
                    sourceChunkRef = "Trigonometry Basics — para 8"
                ),
                McqQuestion(
                    question = "Regarding Trigonometry Basics, which statement is correct? (Q9)",
                    options = listOf("Option A for question 9", "Option B for question 9", "Option C for question 9", "Option D for question 9"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of trigonometry basics.",
                    difficulty = "hard",
                    sourceChunkRef = "Trigonometry Basics — para 9"
                ),
                McqQuestion(
                    question = "Regarding Trigonometry Basics, which statement is correct? (Q10)",
                    options = listOf("Option A for question 10", "Option B for question 10", "Option C for question 10", "Option D for question 10"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of trigonometry basics.",
                    difficulty = "hard",
                    sourceChunkRef = "Trigonometry Basics — para 10"
                ),
            ),
            aiModel = "claude-sonnet-4-6",
            generationDurationMs = 11394L,
            createdAt = "2026-09-22T14:00:00.000Z",
            updatedAt = "2026-09-22T14:00:00.000Z"
        ),
        Mcq(
            _id = "333333333333333333333333",
            board = "ICSE",
            subject = "Science",
            grade = "Class 10",
            chapterNumbers = listOf(8),
            chapterNames = listOf("Chapter 8 — Data Types in Programming"),
            sourceType = "rag",
            lessonPlanId = null,
            ragGrounded = true,
            seedThemes = listOf("Data Types in Programming"),
            title = "Science — Class 10 MCQ (15 Q) — Data Types in Programming",
            questionCount = 15,
            optionsPerQuestion = 4,
            difficulty = "easy",
            timer = McqTimer(enabled = false, secondsPerQuestion = null),
            questions = listOf(
                McqQuestion(
                    question = "Regarding Data Types in Programming, which statement is correct? (Q1)",
                    options = listOf("Option A for question 1", "Option B for question 1", "Option C for question 1", "Option D for question 1"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of data types in programming.",
                    difficulty = "medium",
                    sourceChunkRef = "Data Types in Programming — para 1"
                ),
                McqQuestion(
                    question = "Regarding Data Types in Programming, which statement is correct? (Q2)",
                    options = listOf("Option A for question 2", "Option B for question 2", "Option C for question 2", "Option D for question 2"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of data types in programming.",
                    difficulty = "hard",
                    sourceChunkRef = "Data Types in Programming — para 2"
                ),
                McqQuestion(
                    question = "Regarding Data Types in Programming, which statement is correct? (Q3)",
                    options = listOf("Option A for question 3", "Option B for question 3", "Option C for question 3", "Option D for question 3"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of data types in programming.",
                    difficulty = "hard",
                    sourceChunkRef = "Data Types in Programming — para 3"
                ),
                McqQuestion(
                    question = "Regarding Data Types in Programming, which statement is correct? (Q4)",
                    options = listOf("Option A for question 4", "Option B for question 4", "Option C for question 4", "Option D for question 4"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of data types in programming.",
                    difficulty = "easy",
                    sourceChunkRef = "Data Types in Programming — para 4"
                ),
                McqQuestion(
                    question = "Regarding Data Types in Programming, which statement is correct? (Q5)",
                    options = listOf("Option A for question 5", "Option B for question 5", "Option C for question 5", "Option D for question 5"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of data types in programming.",
                    difficulty = "hard",
                    sourceChunkRef = "Data Types in Programming — para 5"
                ),
                McqQuestion(
                    question = "Regarding Data Types in Programming, which statement is correct? (Q6)",
                    options = listOf("Option A for question 6", "Option B for question 6", "Option C for question 6", "Option D for question 6"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of data types in programming.",
                    difficulty = "medium",
                    sourceChunkRef = "Data Types in Programming — para 6"
                ),
                McqQuestion(
                    question = "Regarding Data Types in Programming, which statement is correct? (Q7)",
                    options = listOf("Option A for question 7", "Option B for question 7", "Option C for question 7", "Option D for question 7"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of data types in programming.",
                    difficulty = "easy",
                    sourceChunkRef = "Data Types in Programming — para 7"
                ),
                McqQuestion(
                    question = "Regarding Data Types in Programming, which statement is correct? (Q8)",
                    options = listOf("Option A for question 8", "Option B for question 8", "Option C for question 8", "Option D for question 8"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of data types in programming.",
                    difficulty = "medium",
                    sourceChunkRef = "Data Types in Programming — para 8"
                ),
                McqQuestion(
                    question = "Regarding Data Types in Programming, which statement is correct? (Q9)",
                    options = listOf("Option A for question 9", "Option B for question 9", "Option C for question 9", "Option D for question 9"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of data types in programming.",
                    difficulty = "easy",
                    sourceChunkRef = "Data Types in Programming — para 9"
                ),
                McqQuestion(
                    question = "Regarding Data Types in Programming, which statement is correct? (Q10)",
                    options = listOf("Option A for question 10", "Option B for question 10", "Option C for question 10", "Option D for question 10"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of data types in programming.",
                    difficulty = "easy",
                    sourceChunkRef = "Data Types in Programming — para 10"
                ),
                McqQuestion(
                    question = "Regarding Data Types in Programming, which statement is correct? (Q11)",
                    options = listOf("Option A for question 11", "Option B for question 11", "Option C for question 11", "Option D for question 11"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of data types in programming.",
                    difficulty = "easy",
                    sourceChunkRef = "Data Types in Programming — para 11"
                ),
                McqQuestion(
                    question = "Regarding Data Types in Programming, which statement is correct? (Q12)",
                    options = listOf("Option A for question 12", "Option B for question 12", "Option C for question 12", "Option D for question 12"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of data types in programming.",
                    difficulty = "hard",
                    sourceChunkRef = "Data Types in Programming — para 12"
                ),
                McqQuestion(
                    question = "Regarding Data Types in Programming, which statement is correct? (Q13)",
                    options = listOf("Option A for question 13", "Option B for question 13", "Option C for question 13", "Option D for question 13"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of data types in programming.",
                    difficulty = "hard",
                    sourceChunkRef = "Data Types in Programming — para 13"
                ),
                McqQuestion(
                    question = "Regarding Data Types in Programming, which statement is correct? (Q14)",
                    options = listOf("Option A for question 14", "Option B for question 14", "Option C for question 14", "Option D for question 14"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of data types in programming.",
                    difficulty = "easy",
                    sourceChunkRef = "Data Types in Programming — para 14"
                ),
                McqQuestion(
                    question = "Regarding Data Types in Programming, which statement is correct? (Q15)",
                    options = listOf("Option A for question 15", "Option B for question 15", "Option C for question 15", "Option D for question 15"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of data types in programming.",
                    difficulty = "hard",
                    sourceChunkRef = "Data Types in Programming — para 15"
                ),
            ),
            aiModel = "claude-sonnet-4-6",
            generationDurationMs = 12884L,
            createdAt = "2026-09-24T03:00:00.000Z",
            updatedAt = "2026-09-24T03:00:00.000Z"
        ),
        Mcq(
            _id = "eeeeeeeeeeeeeeeeeeeeeeee",
            board = "SEBA",
            subject = "Mathematics",
            grade = "Class 11",
            chapterNumbers = listOf(9),
            chapterNames = listOf("Chapter 9 — Photosynthesis"),
            sourceType = "rag",
            lessonPlanId = null,
            ragGrounded = true,
            seedThemes = listOf("Photosynthesis"),
            title = "Mathematics — Class 11 MCQ (15 Q) — Photosynthesis",
            questionCount = 15,
            optionsPerQuestion = 4,
            difficulty = "medium",
            timer = McqTimer(enabled = true, secondsPerQuestion = 60),
            questions = listOf(
                McqQuestion(
                    question = "Regarding Photosynthesis, which statement is correct? (Q1)",
                    options = listOf("Option A for question 1", "Option B for question 1", "Option C for question 1", "Option D for question 1"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of photosynthesis.",
                    difficulty = "easy",
                    sourceChunkRef = "Photosynthesis — para 1"
                ),
                McqQuestion(
                    question = "Regarding Photosynthesis, which statement is correct? (Q2)",
                    options = listOf("Option A for question 2", "Option B for question 2", "Option C for question 2", "Option D for question 2"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of photosynthesis.",
                    difficulty = "medium",
                    sourceChunkRef = "Photosynthesis — para 2"
                ),
                McqQuestion(
                    question = "Regarding Photosynthesis, which statement is correct? (Q3)",
                    options = listOf("Option A for question 3", "Option B for question 3", "Option C for question 3", "Option D for question 3"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of photosynthesis.",
                    difficulty = "easy",
                    sourceChunkRef = "Photosynthesis — para 3"
                ),
                McqQuestion(
                    question = "Regarding Photosynthesis, which statement is correct? (Q4)",
                    options = listOf("Option A for question 4", "Option B for question 4", "Option C for question 4", "Option D for question 4"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of photosynthesis.",
                    difficulty = "easy",
                    sourceChunkRef = "Photosynthesis — para 4"
                ),
                McqQuestion(
                    question = "Regarding Photosynthesis, which statement is correct? (Q5)",
                    options = listOf("Option A for question 5", "Option B for question 5", "Option C for question 5", "Option D for question 5"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of photosynthesis.",
                    difficulty = "hard",
                    sourceChunkRef = "Photosynthesis — para 5"
                ),
                McqQuestion(
                    question = "Regarding Photosynthesis, which statement is correct? (Q6)",
                    options = listOf("Option A for question 6", "Option B for question 6", "Option C for question 6", "Option D for question 6"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of photosynthesis.",
                    difficulty = "medium",
                    sourceChunkRef = "Photosynthesis — para 6"
                ),
                McqQuestion(
                    question = "Regarding Photosynthesis, which statement is correct? (Q7)",
                    options = listOf("Option A for question 7", "Option B for question 7", "Option C for question 7", "Option D for question 7"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of photosynthesis.",
                    difficulty = "hard",
                    sourceChunkRef = "Photosynthesis — para 7"
                ),
                McqQuestion(
                    question = "Regarding Photosynthesis, which statement is correct? (Q8)",
                    options = listOf("Option A for question 8", "Option B for question 8", "Option C for question 8", "Option D for question 8"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of photosynthesis.",
                    difficulty = "easy",
                    sourceChunkRef = "Photosynthesis — para 8"
                ),
                McqQuestion(
                    question = "Regarding Photosynthesis, which statement is correct? (Q9)",
                    options = listOf("Option A for question 9", "Option B for question 9", "Option C for question 9", "Option D for question 9"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of photosynthesis.",
                    difficulty = "easy",
                    sourceChunkRef = "Photosynthesis — para 9"
                ),
                McqQuestion(
                    question = "Regarding Photosynthesis, which statement is correct? (Q10)",
                    options = listOf("Option A for question 10", "Option B for question 10", "Option C for question 10", "Option D for question 10"),
                    correctIndex = 3,
                    explanation = "Option D correctly reflects the concept of photosynthesis.",
                    difficulty = "easy",
                    sourceChunkRef = "Photosynthesis — para 10"
                ),
                McqQuestion(
                    question = "Regarding Photosynthesis, which statement is correct? (Q11)",
                    options = listOf("Option A for question 11", "Option B for question 11", "Option C for question 11", "Option D for question 11"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of photosynthesis.",
                    difficulty = "hard",
                    sourceChunkRef = "Photosynthesis — para 11"
                ),
                McqQuestion(
                    question = "Regarding Photosynthesis, which statement is correct? (Q12)",
                    options = listOf("Option A for question 12", "Option B for question 12", "Option C for question 12", "Option D for question 12"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of photosynthesis.",
                    difficulty = "medium",
                    sourceChunkRef = "Photosynthesis — para 12"
                ),
                McqQuestion(
                    question = "Regarding Photosynthesis, which statement is correct? (Q13)",
                    options = listOf("Option A for question 13", "Option B for question 13", "Option C for question 13", "Option D for question 13"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of photosynthesis.",
                    difficulty = "medium",
                    sourceChunkRef = "Photosynthesis — para 13"
                ),
                McqQuestion(
                    question = "Regarding Photosynthesis, which statement is correct? (Q14)",
                    options = listOf("Option A for question 14", "Option B for question 14", "Option C for question 14", "Option D for question 14"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of photosynthesis.",
                    difficulty = "hard",
                    sourceChunkRef = "Photosynthesis — para 14"
                ),
                McqQuestion(
                    question = "Regarding Photosynthesis, which statement is correct? (Q15)",
                    options = listOf("Option A for question 15", "Option B for question 15", "Option C for question 15", "Option D for question 15"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of photosynthesis.",
                    difficulty = "medium",
                    sourceChunkRef = "Photosynthesis — para 15"
                ),
            ),
            aiModel = "claude-sonnet-4-6",
            generationDurationMs = 9268L,
            createdAt = "2026-09-26T18:00:00.000Z",
            updatedAt = "2026-09-26T18:00:00.000Z"
        ),
        Mcq(
            _id = "999999999999999999999999",
            board = "NCERT",
            subject = "Physics",
            grade = "Class 12",
            chapterNumbers = listOf(10),
            chapterNames = listOf("Chapter 10 — Electricity and Circuits"),
            sourceType = "rag",
            lessonPlanId = null,
            ragGrounded = true,
            seedThemes = listOf("Electricity and Circuits"),
            title = "Physics — Class 12 MCQ (10 Q) — Electricity and Circuits",
            questionCount = 10,
            optionsPerQuestion = 5,
            difficulty = "mixed",
            timer = McqTimer(enabled = false, secondsPerQuestion = null),
            questions = listOf(
                McqQuestion(
                    question = "Regarding Electricity and Circuits, which statement is correct? (Q1)",
                    options = listOf("Option A for question 1", "Option B for question 1", "Option C for question 1", "Option D for question 1", "Option E for question 1"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of electricity and circuits.",
                    difficulty = "medium",
                    sourceChunkRef = "Electricity and Circuits — para 1"
                ),
                McqQuestion(
                    question = "Regarding Electricity and Circuits, which statement is correct? (Q2)",
                    options = listOf("Option A for question 2", "Option B for question 2", "Option C for question 2", "Option D for question 2", "Option E for question 2"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of electricity and circuits.",
                    difficulty = "easy",
                    sourceChunkRef = "Electricity and Circuits — para 2"
                ),
                McqQuestion(
                    question = "Regarding Electricity and Circuits, which statement is correct? (Q3)",
                    options = listOf("Option A for question 3", "Option B for question 3", "Option C for question 3", "Option D for question 3", "Option E for question 3"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of electricity and circuits.",
                    difficulty = "hard",
                    sourceChunkRef = "Electricity and Circuits — para 3"
                ),
                McqQuestion(
                    question = "Regarding Electricity and Circuits, which statement is correct? (Q4)",
                    options = listOf("Option A for question 4", "Option B for question 4", "Option C for question 4", "Option D for question 4", "Option E for question 4"),
                    correctIndex = 1,
                    explanation = "Option B correctly reflects the concept of electricity and circuits.",
                    difficulty = "medium",
                    sourceChunkRef = "Electricity and Circuits — para 4"
                ),
                McqQuestion(
                    question = "Regarding Electricity and Circuits, which statement is correct? (Q5)",
                    options = listOf("Option A for question 5", "Option B for question 5", "Option C for question 5", "Option D for question 5", "Option E for question 5"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of electricity and circuits.",
                    difficulty = "medium",
                    sourceChunkRef = "Electricity and Circuits — para 5"
                ),
                McqQuestion(
                    question = "Regarding Electricity and Circuits, which statement is correct? (Q6)",
                    options = listOf("Option A for question 6", "Option B for question 6", "Option C for question 6", "Option D for question 6", "Option E for question 6"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of electricity and circuits.",
                    difficulty = "medium",
                    sourceChunkRef = "Electricity and Circuits — para 6"
                ),
                McqQuestion(
                    question = "Regarding Electricity and Circuits, which statement is correct? (Q7)",
                    options = listOf("Option A for question 7", "Option B for question 7", "Option C for question 7", "Option D for question 7", "Option E for question 7"),
                    correctIndex = 2,
                    explanation = "Option C correctly reflects the concept of electricity and circuits.",
                    difficulty = "hard",
                    sourceChunkRef = "Electricity and Circuits — para 7"
                ),
                McqQuestion(
                    question = "Regarding Electricity and Circuits, which statement is correct? (Q8)",
                    options = listOf("Option A for question 8", "Option B for question 8", "Option C for question 8", "Option D for question 8", "Option E for question 8"),
                    correctIndex = 4,
                    explanation = "Option E correctly reflects the concept of electricity and circuits.",
                    difficulty = "medium",
                    sourceChunkRef = "Electricity and Circuits — para 8"
                ),
                McqQuestion(
                    question = "Regarding Electricity and Circuits, which statement is correct? (Q9)",
                    options = listOf("Option A for question 9", "Option B for question 9", "Option C for question 9", "Option D for question 9", "Option E for question 9"),
                    correctIndex = 4,
                    explanation = "Option E correctly reflects the concept of electricity and circuits.",
                    difficulty = "medium",
                    sourceChunkRef = "Electricity and Circuits — para 9"
                ),
                McqQuestion(
                    question = "Regarding Electricity and Circuits, which statement is correct? (Q10)",
                    options = listOf("Option A for question 10", "Option B for question 10", "Option C for question 10", "Option D for question 10", "Option E for question 10"),
                    correctIndex = 0,
                    explanation = "Option A correctly reflects the concept of electricity and circuits.",
                    difficulty = "easy",
                    sourceChunkRef = "Electricity and Circuits — para 10"
                ),
            ),
            aiModel = "claude-sonnet-4-6",
            generationDurationMs = 16559L,
            createdAt = "2026-09-28T06:00:00.000Z",
            updatedAt = "2026-09-28T06:00:00.000Z"
        ),
    )
}