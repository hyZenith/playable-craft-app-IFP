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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.GenerateMcqRequest
import com.example.myapplication.data.model.MCQ_MAX_QUESTIONS
import com.example.myapplication.data.model.MCQ_MIN_QUESTIONS
import com.example.myapplication.data.model.McqTimer
import com.example.myapplication.data.repo.McqRepository
import com.example.myapplication.ui.theme.Accent
import com.example.myapplication.ui.theme.AccentLink
import com.example.myapplication.ui.theme.FieldFill
import com.example.myapplication.ui.theme.FieldStroke
import com.example.myapplication.ui.theme.StatusGreen
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary
import kotlinx.coroutines.launch

private val Lavender = Color(0xFF8F9BD6)
private val CardBg = Color(0xFF0B1530)
private val SectionBorder = Color(0xFF1E2B4B)
private val BadgeBg = Color(0xFF13203E)
private val VibrantButtonBlue = Color(0xFF3875FF)

@Composable
fun CreateMcqRoute(
    repository: McqRepository,
    onBackClick: () -> Unit,
    onMcqGenerated: (setId: String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var isGenerating by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var selectedBoard by remember { mutableStateOf("SEBA") }
    var selectedSubject by remember { mutableStateOf("Science") }
    var selectedGrade by remember { mutableStateOf("Class 10") }
    var questionCount by remember { mutableIntStateOf(15) }
    var optionsPerQuestion by remember { mutableIntStateOf(4) }
    var difficulty by remember { mutableStateOf("mixed") }
    var timerEnabled by remember { mutableStateOf(true) }
    var secondsPerQuestion by remember { mutableIntStateOf(60) }
    var customTitle by remember { mutableStateOf("") }

    val boards = listOf("SEBA", "NCERT", "CBSE", "ICSE")
    val subjects = listOf("Science", "Mathematics", "Physics", "Chemistry", "Biology", "English")
    val grades = listOf("Class 8", "Class 9", "Class 10", "Class 11", "Class 12")
    val difficulties = listOf("easy", "medium", "hard", "mixed")

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
                    text = "Generate MCQ Set",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Configure curriculum parameters & AI options",
                    color = Lavender,
                    fontSize = 13.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Configuration Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardBg)
                    .border(1.dp, SectionBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Board selector
                    Column {
                        Text("BOARD / CURRICULUM", color = Lavender, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        SelectorRow(items = boards, selected = selectedBoard, onSelect = { selectedBoard = it })
                    }

                    // Subject selector
                    Column {
                        Text("SUBJECT", color = Lavender, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        SelectorRow(items = subjects, selected = selectedSubject, onSelect = { selectedSubject = it })
                    }

                    // Grade selector
                    Column {
                        Text("GRADE LEVEL", color = Lavender, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        SelectorRow(items = grades, selected = selectedGrade, onSelect = { selectedGrade = it })
                    }

                    // Question count slider (10..45)
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("NUMBER OF QUESTIONS", color = Lavender, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Text("$questionCount Questions", color = AccentLink, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Slider(
                            value = questionCount.toFloat(),
                            onValueChange = { questionCount = it.toInt() },
                            valueRange = MCQ_MIN_QUESTIONS.toFloat()..MCQ_MAX_QUESTIONS.toFloat(),
                            steps = 34,
                            colors = SliderDefaults.colors(thumbColor = AccentLink, activeTrackColor = AccentLink)
                        )
                        Text(
                            text = "Backend validation limits: $MCQ_MIN_QUESTIONS to $MCQ_MAX_QUESTIONS questions",
                            color = Lavender.copy(alpha = 0.7f),
                            fontSize = 11.sp
                        )
                    }

                    // Options per question (4 or 5)
                    Column {
                        Text("OPTIONS PER QUESTION", color = Lavender, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            listOf(4, 5).forEach { opt ->
                                ChoiceChip(
                                    label = "$opt Options",
                                    selected = optionsPerQuestion == opt,
                                    onClick = { optionsPerQuestion = opt }
                                )
                            }
                        }
                    }

                    // Difficulty
                    Column {
                        Text("DIFFICULTY LEVEL", color = Lavender, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            difficulties.forEach { diff ->
                                ChoiceChip(
                                    label = diff.replaceFirstChar { it.uppercase() },
                                    selected = difficulty == diff,
                                    onClick = { difficulty = diff }
                                )
                            }
                        }
                    }

                    // Timer config
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { timerEnabled = !timerEnabled }
                        ) {
                            Checkbox(
                                checked = timerEnabled,
                                onCheckedChange = { timerEnabled = it },
                                colors = CheckboxDefaults.colors(checkedColor = AccentLink)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Enable Question Timer", color = TextPrimary, fontSize = 14.sp)
                        }

                        if (timerEnabled) {
                            Spacer(Modifier.height(6.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(start = 12.dp)
                            ) {
                                listOf(30, 45, 60, 90).forEach { s ->
                                    ChoiceChip(
                                        label = "${s}s",
                                        selected = secondsPerQuestion == s,
                                        onClick = { secondsPerQuestion = s }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (errorMessage != null) {
                Text(errorMessage.orEmpty(), color = Color(0xFFFF6B6B), fontSize = 13.sp)
            }

            // Generate Button
            Button(
                onClick = {
                    isGenerating = true
                    errorMessage = null
                    scope.launch {
                        val req = GenerateMcqRequest(
                            board = selectedBoard,
                            subject = selectedSubject,
                            grade = selectedGrade,
                            chapterNumbers = listOf(1),
                            questionCount = questionCount,
                            optionsPerQuestion = optionsPerQuestion,
                            difficulty = difficulty,
                            timer = McqTimer(enabled = timerEnabled, secondsPerQuestion = if (timerEnabled) secondsPerQuestion else null),
                            title = customTitle.ifBlank { null },
                            sourceType = "rag"
                        )
                        repository.create(req)
                            .onSuccess { created ->
                                isGenerating = false
                                onMcqGenerated(created._id)
                            }
                            .onFailure {
                                isGenerating = false
                                errorMessage = "Failed to generate MCQ set. Please retry."
                            }
                    }
                },
                enabled = !isGenerating,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VibrantButtonBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Icon(imageVector = Icons.Outlined.AutoAwesome, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Generate MCQ ($questionCount Questions)",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun SelectorRow(items: List<String>, selected: String, onSelect: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEach { item ->
            ChoiceChip(
                label = item,
                selected = item.equals(selected, ignoreCase = true),
                onClick = { onSelect(item) }
            )
        }
    }
}

@Composable
private fun ChoiceChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) AccentLink else BadgeBg)
            .border(1.dp, if (selected) AccentLink else SectionBorder, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else Lavender,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
