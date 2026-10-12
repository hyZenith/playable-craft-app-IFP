package com.example.myapplication.ui.ai

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.repo.SimulationRepository
import kotlinx.coroutines.launch

private val PageBackground = Color(0xFF070B16)
private val TopBarPillBg = Color(0xFF131D38)
private val TopBarPillBorder = Color(0xFF23355E)
private val TopBarPillText = Color(0xFF9FB0D9)

private val CenterSquircleBg = Color(0xFF142142)
private val CenterSquircleBorder = Color(0xFF233560)
private val CenterSparkleTint = Color(0xFFA5B8FC)
private val SubtitleColor = Color(0xFF8596B8)

private val CardBackground = Color(0xFF0F1830)
private val CardBorder = Color(0xFF22355E)
private val DropdownBoxBg = Color(0xFF0C1326)
private val DropdownBoxBorder = Color(0xFF213259)
private val DropdownLabelColor = Color(0xFF7A8CAE)
private val DropdownPlaceholderColor = Color(0xFF7B8DAF)

private val PromptBoxBg = Color(0xFF0A1022)
private val PromptBoxBorder = Color(0xFF1E2E52)
private val PromptPlaceholderColor = Color(0xFF566788)

private val StatusHintColor = Color(0xFF637494)
private val StatusHintReadyColor = Color(0xFF9FB0D9)
private val GenerateButtonColor = Color(0xFF635BFF)

/**
 * Stateful route for creating a new PlayableCraft AI simulation.
 */
@Composable
fun NewSimulationRoute(
    repository: SimulationRepository,
    onCancelClick: () -> Unit,
    onSimulationGenerated: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var isGenerating by remember { mutableStateOf(false) }
    var isPromptBarOpen by remember { mutableStateOf(true) }

    var selectedGrade by remember { mutableStateOf<String?>("Class 10") }
    var selectedSubject by remember { mutableStateOf<String?>("Physics") }
    var selectedChapter by remember { mutableStateOf<String?>("Laws of Motion") }
    var topicPrompt by remember { mutableStateOf("") }

    val gradeOptions = remember {
        listOf("Class 8", "Class 9", "Class 10", "Class 11", "Class 12")
    }

    val subjectOptions = remember {
        listOf("Physics", "Chemistry", "Biology", "Science", "Mathematics")
    }

    val chapterOptionsMap = remember {
        mapOf(
            "Physics" to listOf(
                "Laws of Motion",
                "Gravitation",
                "Work and Energy",
                "Light: Reflection & Refraction",
                "Electricity & Circuits",
                "Magnetic Effects of Current",
                "Sound & Waves"
            ),
            "Chemistry" to listOf(
                "Chemical Reactions & Equations",
                "Acids, Bases & Salts",
                "Metals and Non-metals",
                "Carbon & its Compounds",
                "Periodic Classification",
                "Structure of the Atom"
            ),
            "Biology" to listOf(
                "Life Processes",
                "Control & Coordination",
                "Reproduction in Organisms",
                "Heredity & Evolution",
                "Our Environment"
            ),
            "Science" to listOf(
                "Chemical Reactions & Equations",
                "Life Processes",
                "Light: Reflection & Refraction",
                "Electricity",
                "Laws of Motion",
                "Acids, Bases & Salts"
            ),
            "Mathematics" to listOf(
                "Coordinate Geometry",
                "Quadratic Equations",
                "Trigonometry & Heights",
                "Circles & Tangents",
                "Surface Areas and Volumes"
            )
        )
    }

    val availableChapters = selectedSubject?.let { chapterOptionsMap[it] }
        ?: chapterOptionsMap["Physics"] ?: emptyList()

    NewSimulationScreen(
        isPromptBarOpen = isPromptBarOpen,
        onTogglePromptBar = { isPromptBarOpen = !isPromptBarOpen },
        onCancelClick = onCancelClick,
        selectedGrade = selectedGrade,
        gradeOptions = gradeOptions,
        onGradeSelect = { selectedGrade = it },
        selectedSubject = selectedSubject,
        subjectOptions = subjectOptions,
        onSubjectSelect = { subject ->
            selectedSubject = subject
            val chapters = chapterOptionsMap[subject]
            if (chapters != null && (selectedChapter == null || selectedChapter !in chapters)) {
                selectedChapter = chapters.firstOrNull()
            }
        },
        selectedChapter = selectedChapter,
        chapterOptions = availableChapters,
        onChapterSelect = { selectedChapter = it },
        topicPrompt = topicPrompt,
        onTopicPromptChange = { topicPrompt = it },
        isGenerating = isGenerating,
        onGenerateClick = {
            val chapter = selectedChapter ?: return@NewSimulationScreen
            val grade = selectedGrade ?: "Class 10"
            val subject = selectedSubject ?: "Physics"

            isGenerating = true
            scope.launch {
                val result = repository.create(
                    grade = grade,
                    subject = subject,
                    chapter = chapter,
                    topicPrompt = topicPrompt.ifBlank { null }
                )
                isGenerating = false
                result.onSuccess { created ->
                    onSimulationGenerated(created.id)
                }
            }
        },
        modifier = modifier
    )
}

/**
 * Pixel-accurate screen layout matching the PlayableCraft AI generation design.
 */
@Composable
fun NewSimulationScreen(
    isPromptBarOpen: Boolean,
    onTogglePromptBar: () -> Unit,
    onCancelClick: () -> Unit,
    selectedGrade: String?,
    gradeOptions: List<String>,
    onGradeSelect: (String) -> Unit,
    selectedSubject: String?,
    subjectOptions: List<String>,
    onSubjectSelect: (String) -> Unit,
    selectedChapter: String?,
    chapterOptions: List<String>,
    onChapterSelect: (String) -> Unit,
    topicPrompt: String,
    onTopicPromptChange: (String) -> Unit,
    isGenerating: Boolean,
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PageBackground)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Wordmark
            Text(
                text = "Playable Craft",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            // Actions: Close Prompt Bar toggle & Cancel
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Toggle Prompt Bar button
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = TopBarPillBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, TopBarPillBorder),
                    modifier = Modifier.clickable(onClick = onTogglePromptBar)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = if (isPromptBarOpen) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                            contentDescription = null,
                            tint = TopBarPillText,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = if (isPromptBarOpen) "close prompt bar" else "open prompt bar",
                            color = TopBarPillText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Cancel button
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = TopBarPillBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, TopBarPillBorder),
                    modifier = Modifier.clickable(onClick = onCancelClick)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel",
                            tint = TopBarPillText,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "Cancel",
                            color = TopBarPillText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Center Empty State Canvas
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Squircle Box with Sparkles Icon
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(CenterSquircleBg)
                    .border(1.dp, CenterSquircleBorder, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = CenterSparkleTint,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No simulation yet",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Pick a chapter from your ingested books and generate a\nlive, grounded simulation.",
                color = SubtitleColor,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 19.sp
            )

            // Visual balance spacing when prompt bar is open
            if (isPromptBarOpen) {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // Bottom Floating Prompt Bar
        AnimatedVisibility(
            visible = isPromptBarOpen,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .navigationBarsPadding(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 780.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    // Row 1: GRADE, SUBJECT, CHAPTER Selectors
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        DropdownSelectorField(
                            label = "GRADE",
                            value = selectedGrade,
                            placeholder = "Select grade",
                            options = gradeOptions,
                            onOptionSelected = onGradeSelect,
                            modifier = Modifier.weight(1f)
                        )

                        DropdownSelectorField(
                            label = "SUBJECT",
                            value = selectedSubject,
                            placeholder = "Select subject",
                            options = subjectOptions,
                            onOptionSelected = onSubjectSelect,
                            modifier = Modifier.weight(1f)
                        )

                        DropdownSelectorField(
                            label = "CHAPTER",
                            value = selectedChapter,
                            placeholder = "Select chapter",
                            options = chapterOptions,
                            onOptionSelected = onChapterSelect,
                            modifier = Modifier.weight(1.3f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Row 2: Prompt / Narrowing Input Field
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(PromptBoxBg)
                            .border(1.dp, PromptBoxBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        if (topicPrompt.isEmpty()) {
                            Text(
                                text = "Narrow the simulation to a topic in this chapter — e.g. 'just the velocity-time graph'. Optional.",
                                color = PromptPlaceholderColor,
                                fontSize = 12.5.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        BasicTextField(
                            value = topicPrompt,
                            onValueChange = onTopicPromptChange,
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 12.5.sp
                            ),
                            cursorBrush = SolidColor(GenerateButtonColor),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Row 3: Status hint & Generate button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Status Hint
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = StatusHintColor,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = if (selectedChapter.isNullOrBlank()) {
                                    "Select a chapter to enable generation."
                                } else {
                                    "Ready to generate simulation."
                                },
                                color = if (selectedChapter.isNullOrBlank()) StatusHintColor else StatusHintReadyColor,
                                fontSize = 11.5.sp
                            )
                        }

                        // Generate CTA Button
                        val isEnabled = !selectedChapter.isNullOrBlank() && !isGenerating
                        Button(
                            onClick = onGenerateClick,
                            enabled = isEnabled,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GenerateButtonColor,
                                disabledContainerColor = GenerateButtonColor.copy(alpha = 0.35f)
                            ),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            modifier = Modifier.height(38.dp)
                        ) {
                            if (isGenerating) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Generating...",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Outlined.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Generate",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Dropdown trigger field with title label, rounded box, and popover menu.
 */
@Composable
private fun DropdownSelectorField(
    label: String,
    value: String?,
    placeholder: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = label,
            color = DropdownLabelColor,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(5.dp))

        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(DropdownBoxBg)
                    .border(1.dp, DropdownBoxBorder, RoundedCornerShape(8.dp))
                    .clickable { isExpanded = true }
                    .padding(horizontal = 10.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = value ?: placeholder,
                    color = if (value != null) Color.White else DropdownPlaceholderColor,
                    fontSize = 12.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = DropdownPlaceholderColor,
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
                                color = if (option == value) Color.White else DropdownLabelColor,
                                fontSize = 13.sp,
                                fontWeight = if (option == value) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        onClick = {
                            onOptionSelected(option)
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}
