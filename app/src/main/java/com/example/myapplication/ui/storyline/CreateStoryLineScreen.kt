package com.example.myapplication.ui.storyline

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.repo.StoryLineRepository
import kotlinx.coroutines.launch

private val PageBackground = Color(0xFF070B16)
private val CardBackground = Color(0xFF0D162C)
private val CardBorder = Color(0xFF1C2C4E)

private val InputBoxBg = Color(0xFF080F22)
private val InputBoxBorder = Color(0xFF1E2D4E)
private val LabelColor = Color(0xFF6B7D9E)
private val PlaceholderColor = Color(0xFF536484)

private val AiCheckboxCardBg = Color(0xFF080E20)
private val AiCheckboxCardBorder = Color(0xFF182644)
private val AiSubtitleColor = Color(0xFF647596)
private val CreditsCostColor = Color(0xFF7587A6)

private val GenerateBtnBg = Color(0xFF1E2C48)
private val GenerateBtnBorder = Color(0xFF2C3E66)
private val GenerateBtnActiveBg = Color(0xFF243658)
private val GenerateBtnText = Color(0xFFD6E2FA)

private val NotesPillBg = Color(0xFF101A34)
private val NotesPillBorder = Color(0xFF223154)
private val NotesLavender = Color(0xFF8F9BD6)

/**
 * Stateful route for creating a new StoryLine.
 */
@Composable
fun CreateStoryLineRoute(
    repository: StoryLineRepository,
    onBackClick: () -> Unit,
    onStoryGenerated: (storyId: String) -> Unit,
    onNotesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var isGenerating by remember { mutableStateOf(false) }

    var topic by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf("Science") }
    var selectedGrade by remember { mutableStateOf("Class 10") }
    var selectedActCount by remember { mutableIntStateOf(4) }
    var generateAiIllustrations by remember { mutableStateOf(false) }

    val subjects = remember {
        listOf("Science", "Physics", "Chemistry", "Biology", "Mathematics", "Social Science", "English")
    }

    val grades = remember {
        listOf("Class 8", "Class 9", "Class 10", "Class 11", "Class 12")
    }

    val actOptions = remember {
        listOf(3, 4, 5, 6)
    }

    CreateStoryLineScreen(
        topic = topic,
        onTopicChange = { topic = it },
        selectedSubject = selectedSubject,
        subjectOptions = subjects,
        onSubjectSelect = { selectedSubject = it },
        selectedGrade = selectedGrade,
        gradeOptions = grades,
        onGradeSelect = { selectedGrade = it },
        selectedActCount = selectedActCount,
        actOptions = actOptions,
        onActCountSelect = { selectedActCount = it },
        generateAiIllustrations = generateAiIllustrations,
        onToggleAiIllustrations = { generateAiIllustrations = !generateAiIllustrations },
        isGenerating = isGenerating,
        onGenerateClick = {
            isGenerating = true
            scope.launch {
                val result = repository.create(
                    topic = topic,
                    subject = selectedSubject,
                    grade = selectedGrade,
                    actCount = selectedActCount,
                    generateIllustrations = generateAiIllustrations
                )
                isGenerating = false
                result.onSuccess { created ->
                    onStoryGenerated(created.id)
                }
            }
        },
        onBackClick = onBackClick,
        onNotesClick = onNotesClick,
        modifier = modifier
    )
}

/**
 * Pixel-accurate screen layout matching the "New StoryLine" specification.
 */
@Composable
fun CreateStoryLineScreen(
    topic: String,
    onTopicChange: (String) -> Unit,
    selectedSubject: String,
    subjectOptions: List<String>,
    onSubjectSelect: (String) -> Unit,
    selectedGrade: String,
    gradeOptions: List<String>,
    onGradeSelect: (String) -> Unit,
    selectedActCount: Int,
    actOptions: List<Int>,
    onActCountSelect: (Int) -> Unit,
    generateAiIllustrations: Boolean,
    onToggleAiIllustrations: () -> Unit,
    isGenerating: Boolean,
    onGenerateClick: () -> Unit,
    onBackClick: () -> Unit,
    onNotesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PageBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Header: ← New StoryLine
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "New StoryLine",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Main StoryLine Input Card Container
            Column(
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardBackground)
                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                    .padding(20.dp)
            ) {
                // Field 1: TOPIC
                Text(
                    text = "TOPIC",
                    color = LabelColor,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(InputBoxBg)
                        .border(1.dp, InputBoxBorder, RoundedCornerShape(6.dp))
                        .padding(horizontal = 14.dp, vertical = 11.dp)
                ) {
                    if (topic.isEmpty()) {
                        Text(
                            text = "e.g. Ohm's Law, Photosynthesis...",
                            color = PlaceholderColor,
                            fontSize = 12.5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    BasicTextField(
                        value = topic,
                        onValueChange = onTopicChange,
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 12.5.sp
                        ),
                        cursorBrush = SolidColor(NotesLavender),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Field 2: SUBJECT
                Text(
                    text = "SUBJECT",
                    color = LabelColor,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                StoryDropdownField(
                    value = selectedSubject,
                    options = subjectOptions,
                    onOptionSelect = onSubjectSelect
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Field 3: GRADE & NUMBER OF ACTS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Column 1: GRADE
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "GRADE",
                            color = LabelColor,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        StoryDropdownField(
                            value = selectedGrade,
                            options = gradeOptions,
                            onOptionSelect = onGradeSelect
                        )
                    }

                    // Column 2: NUMBER OF ACTS
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "NUMBER OF ACTS",
                            color = LabelColor,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        StoryDropdownField(
                            value = "$selectedActCount acts",
                            options = actOptions.map { "$it acts" },
                            onOptionSelect = { str ->
                                val count = str.filter { it.isDigit() }.toIntOrNull() ?: 4
                                onActCountSelect(count)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Field 4: AI Illustrations Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(AiCheckboxCardBg)
                        .border(1.dp, AiCheckboxCardBorder, RoundedCornerShape(8.dp))
                        .clickable(onClick = onToggleAiIllustrations)
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Square Checkbox
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(if (generateAiIllustrations) NotesLavender else Color.Transparent)
                                .border(
                                    1.dp,
                                    if (generateAiIllustrations) NotesLavender else Color.White,
                                    RoundedCornerShape(3.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (generateAiIllustrations) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF0B1428),
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = "Generate AI illustrations for each act",
                                color = Color.White,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "One image per act. Off by default — each image costs extra credits.",
                                color = AiSubtitleColor,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Cost: ₹25 credits",
                        color = CreditsCostColor,
                        fontSize = 11.5.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Field 5: Generate StoryLine CTA
                Button(
                    onClick = onGenerateClick,
                    enabled = !isGenerating,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (topic.isNotBlank()) GenerateBtnActiveBg else GenerateBtnBg,
                        contentColor = GenerateBtnText
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GenerateBtnBorder),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(
                            color = GenerateBtnText,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Generating...",
                            color = GenerateBtnText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Text(
                            text = "Generate StoryLine",
                            color = GenerateBtnText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }

        // Bottom-Right Pinned Notes Action
        com.example.myapplication.ui.common.NotesFloatingButton(
            onClick = onNotesClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(24.dp)
        )
    }
}

/**
 * Dropdown selector box for StoryLine fields with custom dark popup menu.
 */
@Composable
private fun StoryDropdownField(
    value: String,
    options: List<String>,
    onOptionSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(InputBoxBg)
                .border(1.dp, InputBoxBorder, RoundedCornerShape(6.dp))
                .clickable { isExpanded = true }
                .padding(horizontal = 14.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = value,
                color = Color.White,
                fontSize = 12.5.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = PlaceholderColor,
                modifier = Modifier.size(16.dp)
            )
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .background(CardBackground)
                .border(1.dp, CardBorder, RoundedCornerShape(8.dp))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = if (option == value) Color.White else LabelColor,
                            fontSize = 13.sp,
                            fontWeight = if (option == value) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onOptionSelect(option)
                        isExpanded = false
                    }
                )
            }
        }
    }
}
