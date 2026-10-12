package com.example.myapplication.ui.lessonplan

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.LessonPlan
import com.example.myapplication.data.repo.LessonPlanRepository
import com.example.myapplication.ui.theme.AccentLink
import com.example.myapplication.ui.theme.FieldStroke
import com.example.myapplication.ui.theme.StatusGreen
import com.example.myapplication.ui.theme.TextLabel
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary

private val Lavender = Color(0xFF8F9BD6)
private val CardBg = Color(0xFF0B1530)
private val SectionBorder = Color(0xFF1E2B4B)
private val BadgeBg = Color(0xFF13203E)

@Composable
fun LessonPlanDetailRoute(
    planId: String,
    repository: LessonPlanRepository,
    onBackClick: () -> Unit
) {
    var plan by remember { mutableStateOf<LessonPlan?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(planId) {
        isLoading = true
        errorMessage = null
        repository.getById(planId)
            .onSuccess {
                plan = it
                isLoading = false
                if (it == null) errorMessage = "Lesson plan not found."
            }
            .onFailure {
                errorMessage = "Failed to load lesson plan details."
                isLoading = false
            }
    }

    LessonPlanDetailScreen(
        plan = plan,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onBackClick = onBackClick
    )
}

@Composable
fun LessonPlanDetailScreen(
    plan: LessonPlan?,
    isLoading: Boolean,
    errorMessage: String?,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp)
    ) {
        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BadgeBg)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = plan?.topic ?: "Lesson Plan",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "${plan?.board ?: ""} · ${plan?.subject ?: ""} · ${plan?.grade ?: ""}",
                    color = Lavender,
                    fontSize = 13.sp
                )
            }
        }

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AccentLink)
            }
            return
        }

        if (errorMessage != null || plan == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(errorMessage ?: "Lesson plan unavailable.", color = Lavender, fontSize = 16.sp)
            }
            return
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardBg)
                    .border(1.dp, SectionBorder, RoundedCornerShape(16.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DetailBadge("${plan.difficultyLevel} Level", StatusGreen)
                        DetailBadge("${plan.estimatedDurationMinutes} mins", AccentLink)
                        if (plan.simulation.feasible) DetailBadge("Simulation Feasible", Color(0xFF6B8EFF))
                        if (plan.mcq.feasible) DetailBadge("MCQs Ready", Color(0xFF2EE59D))
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = plan.definition,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                    if (!plan.chapterName.isNullOrBlank()) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Chapter: ${plan.chapterName}",
                            color = Lavender,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Learning Objectives with Bloom's Levels
            SectionCard("Learning Objectives") {
                plan.objectives.forEachIndexed { i, obj ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text("${i + 1}. ", color = AccentLink, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(obj.text, color = TextPrimary, fontSize = 14.sp)
                            Text(
                                "Bloom Taxonomy: ${obj.bloomLevel}",
                                color = Lavender,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Time Block Plan (Classroom Execution)
            SectionCard("Time Block Plan (${plan.estimatedDurationMinutes} min)") {
                plan.timeBlockPlan.forEach { block ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(BadgeBg)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("${block.duration}m", color = AccentLink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(block.activity, color = TextPrimary, fontSize = 14.sp)
                            Text(block.type.uppercase(), color = Lavender, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // Teaching Strategies & Board Work
            SectionCard("Teaching Strategies") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    plan.teachingStrategies.forEach { strategy ->
                        DetailBadge(strategy, Color(0xFFC7D2FE))
                    }
                }
                if (plan.boardWorkOutline.isNotEmpty()) {
                    Spacer(Modifier.height(10.dp))
                    Text("Board Work Outline:", color = Lavender, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    plan.boardWorkOutline.forEach { line ->
                        Text("• $line", color = TextPrimary, fontSize = 13.5.sp, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }

            // Differentiation Notes
            SectionCard("Differentiation Support") {
                Text("Slow Learners:", color = Color(0xFFF5C451), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(plan.differentiationNotes.slowLearners, color = TextPrimary, fontSize = 13.5.sp)
                Spacer(Modifier.height(8.dp))
                Text("Advanced Learners:", color = Color(0xFF38B2AC), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(plan.differentiationNotes.advancedLearners, color = TextPrimary, fontSize = 13.5.sp)
            }

            // Assessments: Exit Tickets & HOTS
            SectionCard("Formative Assessment & HOTS") {
                if (plan.exitTicketQuestions.isNotEmpty()) {
                    Text("Exit Ticket Question:", color = Lavender, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    plan.exitTicketQuestions.forEach { q ->
                        Text("• $q", color = TextPrimary, fontSize = 13.5.sp, modifier = Modifier.padding(top = 2.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                }
                if (plan.hotsQuestions.isNotEmpty()) {
                    Text("Higher Order Thinking (HOTS):", color = Lavender, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    plan.hotsQuestions.forEach { q ->
                        Text("• $q", color = TextPrimary, fontSize = 13.5.sp, modifier = Modifier.padding(top = 2.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBg)
            .border(1.dp, SectionBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = title,
                color = AccentLink,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.2.sp
            )
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
private fun DetailBadge(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, color.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    )
}
