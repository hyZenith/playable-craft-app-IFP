package com.example.myapplication.ui.mcq

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
import androidx.compose.material.icons.outlined.Timer
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
import com.example.myapplication.data.model.Mcq
import com.example.myapplication.data.repo.McqRepository
import com.example.myapplication.ui.theme.AccentLink
import com.example.myapplication.ui.theme.StatusGreen
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary

private val Lavender = Color(0xFF8F9BD6)
private val CardBg = Color(0xFF0B1530)
private val SectionBorder = Color(0xFF1E2B4B)
private val BadgeBg = Color(0xFF13203E)
private val OptionCorrectBg = Color(0xFF0D281E)
private val OptionCorrectBorder = Color(0xFF1E6F4B)
private val OptionCorrectText = Color(0xFF38E59D)

@Composable
fun McqDetailRoute(
    setId: String,
    repository: McqRepository,
    onBackClick: () -> Unit
) {
    var mcq by remember { mutableStateOf<Mcq?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(setId) {
        isLoading = true
        errorMessage = null
        repository.getById(setId)
            .onSuccess {
                mcq = it
                isLoading = false
                if (it == null) errorMessage = "MCQ set not found."
            }
            .onFailure {
                errorMessage = "Failed to load MCQ set details."
                isLoading = false
            }
    }

    McqDetailScreen(
        mcq = mcq,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onBackClick = onBackClick
    )
}

@Composable
fun McqDetailScreen(
    mcq: Mcq?,
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
        // Top bar
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
                    text = mcq?.title ?: "MCQ Details",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "${mcq?.board ?: ""} · ${mcq?.subject ?: ""} · ${mcq?.grade ?: ""}",
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

        if (errorMessage != null || mcq == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(errorMessage ?: "MCQ set unavailable.", color = Lavender, fontSize = 16.sp)
            }
            return
        }

        // Questions List
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header stats
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardBg)
                    .border(1.dp, SectionBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DetailBadge("${mcq.questionCount} Questions", AccentLink)
                        DetailBadge("${mcq.optionsPerQuestion} Options/Q", Color(0xFFC7D2FE))
                        DetailBadge(mcq.difficulty.replaceFirstChar { it.uppercase() }, StatusGreen)
                        if (mcq.ragGrounded) DetailBadge("RAG Grounded", Color(0xFF2EE59D))
                    }
                    if (mcq.timer.enabled) {
                        Spacer(Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Timer,
                                contentDescription = null,
                                tint = Lavender,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Timer enabled: ${mcq.timer.secondsPerQuestion ?: 60} seconds per question",
                                color = Lavender,
                                fontSize = 13.sp
                            )
                        }
                    }
                    if (mcq.chapterNames.isNotEmpty()) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Chapters: ${mcq.chapterNames.joinToString(", ")}",
                            color = Lavender,
                            fontSize = 12.5.sp
                        )
                    }
                }
            }

            // Question Items
            mcq.questions.forEachIndexed { index, q ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardBg)
                        .border(1.dp, SectionBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Question ${index + 1} of ${mcq.questionCount}",
                                color = AccentLink,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            DetailBadge(q.difficulty.replaceFirstChar { it.uppercase() }, Color(0xFF90CDF4))
                        }

                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = q.question,
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 22.sp
                        )

                        Spacer(Modifier.height(12.dp))

                        // Options
                        q.options.forEachIndexed { optIndex, optionText ->
                            val isCorrect = optIndex == q.correctIndex
                            val bg = if (isCorrect) OptionCorrectBg else BadgeBg
                            val border = if (isCorrect) OptionCorrectBorder else SectionBorder
                            val textColor = if (isCorrect) OptionCorrectText else TextPrimary

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(bg)
                                    .border(1.dp, border, RoundedCornerShape(10.dp))
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${('A' + optIndex)}. ",
                                    color = if (isCorrect) OptionCorrectText else Lavender,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = optionText,
                                    color = textColor,
                                    fontSize = 14.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                if (isCorrect) {
                                    Icon(
                                        imageVector = Icons.Outlined.CheckCircle,
                                        contentDescription = "Correct",
                                        tint = OptionCorrectText,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        if (!q.explanation.isNullOrBlank()) {
                            Spacer(Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(BadgeBg)
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text("Explanation:", color = AccentLink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text(q.explanation, color = TextSecondary, fontSize = 13.sp)
                                }
                            }
                        }

                        if (!q.sourceChunkRef.isNullOrBlank()) {
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Source: ${q.sourceChunkRef}",
                                color = Lavender.copy(alpha = 0.7f),
                                fontSize = 11.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailBadge(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        fontSize = 11.5.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, color.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 2.5.dp)
    )
}
