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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.TextPrimary

private val Lavender = Color(0xFF8F9BD6)
private val SoftField = Color(0xFF0B1530)
private val CardBorder = Color(0xFF1E2B4B)
private val DropdownBg = Color(0xFF070E1F)
private val DropdownBorder = Color(0xFF1C2848)
private val VibrantButtonBlue = Color(0xFF3875FF)
private val ModeSelectorBg = Color(0xFF070E20)
private val ModeSelectorBorder = Color(0xFF182442)
private val ContextPillBg = Color(0xFF13203E)
private val ContextPillBorder = Color(0xFF22355E)

/**
 * Stateful route component binding CreateLessonPlanViewModel to UI.
 *
 * @param viewModel Controller managing lesson plan generation inputs.
 * @param onBackClick Triggered when back button is tapped.
 * @param onPlanGenerated Triggered when new plan is created to navigate to detail viewer.
 * @param onNotesClick Triggered when floating notes button is clicked.
 */
@Composable
fun CreateLessonPlanRoute(
    viewModel: CreateLessonPlanViewModel,
    onBackClick: () -> Unit,
    onPlanGenerated: (planId: String) -> Unit,
    onNotesClick: () -> Unit
) {
    val state = viewModel.uiState

    CreateLessonPlanScreen(
        state = state,
        availableGrades = viewModel.availableGrades,
        availableSubjects = viewModel.availableSubjects,
        availableChapters = viewModel.availableChapters,
        availableDurations = viewModel.availableDurations,
        availableBoards = viewModel.availableBoards,
        onModeSelected = viewModel::onModeSelected,
        onGradeSelected = viewModel::onGradeSelected,
        onSubjectSelected = viewModel::onSubjectSelected,
        onChapterSelected = viewModel::onChapterSelected,
        onDurationSelected = viewModel::onDurationSelected,
        onBoardSelected = viewModel::onBoardSelected,
        onTopicPromptChange = viewModel::onTopicPromptChange,
        onGenerate = { viewModel.generatePlan(onPlanGenerated) },
        onBackClick = onBackClick,
        onNotesClick = onNotesClick
    )
}

/**
 * Stateless screen displaying the New Lesson Plan header, central prompt watermark,
 * and docked bottom generation control card matching reference designs.
 */
@Composable
fun CreateLessonPlanScreen(
    state: CreateLessonPlanUiState,
    availableGrades: List<String>,
    availableSubjects: List<String>,
    availableChapters: List<String>,
    availableDurations: List<Int>,
    availableBoards: List<String>,
    onModeSelected: (LessonPlanCreationMode) -> Unit,
    onGradeSelected: (String?) -> Unit,
    onSubjectSelected: (String?) -> Unit,
    onChapterSelected: (String?) -> Unit,
    onDurationSelected: (Int) -> Unit,
    onBoardSelected: (String) -> Unit,
    onTopicPromptChange: (String) -> Unit,
    onGenerate: () -> Unit,
    onBackClick: () -> Unit,
    onNotesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Header Row with back arrow, title, and subtitle
            CreateLessonPlanHeader(
                onBackClick = onBackClick
            )

            // Central Canvas Area: Empty prompt or generation status
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (state.isGenerating) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        CircularProgressIndicator(
                            color = VibrantButtonBlue,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(38.dp)
                        )
                        Spacer(Modifier.height(14.dp))
                        Text(
                            text = "Drafting lesson objectives, 45-min timeblocks, and misconceptions...",
                            color = Lavender,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AutoAwesome,
                            contentDescription = null,
                            tint = Lavender,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "Choose a chapter and enter a topic to generate a lesson plan.",
                            color = Lavender,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Bottom Docked Generator Control Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                GeneratorControlCard(
                    state = state,
                    availableGrades = availableGrades,
                    availableSubjects = availableSubjects,
                    availableChapters = availableChapters,
                    availableDurations = availableDurations,
                    availableBoards = availableBoards,
                    onModeSelected = onModeSelected,
                    onGradeSelected = onGradeSelected,
                    onSubjectSelected = onSubjectSelected,
                    onChapterSelected = onChapterSelected,
                    onDurationSelected = onDurationSelected,
                    onBoardSelected = onBoardSelected,
                    onTopicPromptChange = onTopicPromptChange,
                    onGenerate = onGenerate
                )
            }
        }

        // Floating Notes Button in bottom right
        NotesFloatingButton(
            onClick = onNotesClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp)
        )
    }
}

/**
 * Top header containing back navigation arrow, title, and descriptive subtitle.
 */
@Composable
private fun CreateLessonPlanHeader(
    onBackClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF0F1A34))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Lavender,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = "New Lesson Plan",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.3).sp
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = "Pick a chapter from your textbooks or enter a topic manually.",
                color = Lavender,
                fontSize = 13.sp
            )
        }
    }
}

/**
 * Bottom docked generator control card hosting mode toggle, dropdowns, chips, and prompt input.
 */
@Composable
private fun GeneratorControlCard(
    state: CreateLessonPlanUiState,
    availableGrades: List<String>,
    availableSubjects: List<String>,
    availableChapters: List<String>,
    availableDurations: List<Int>,
    availableBoards: List<String>,
    onModeSelected: (LessonPlanCreationMode) -> Unit,
    onGradeSelected: (String?) -> Unit,
    onSubjectSelected: (String?) -> Unit,
    onChapterSelected: (String?) -> Unit,
    onDurationSelected: (Int) -> Unit,
    onBoardSelected: (String) -> Unit,
    onTopicPromptChange: (String) -> Unit,
    onGenerate: () -> Unit
) {
    val cardShape = RoundedCornerShape(16.dp)

    Column(
        modifier = Modifier
            .widthIn(max = 760.dp)
            .fillMaxWidth()
            .clip(cardShape)
            .background(SoftField)
            .border(1.dp, CardBorder, cardShape)
            .padding(16.dp)
    ) {
        // Mode Switcher: From Textbook vs Manual
        ModeSwitcherRow(
            currentMode = state.mode,
            onModeSelected = onModeSelected
        )

        Spacer(Modifier.height(14.dp))

        if (state.mode == LessonPlanCreationMode.FROM_TEXTBOOK) {
            // Dropdowns: Grade, Subject, Chapter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Grade Dropdown
                DropdownPickerBox(
                    label = "GRADE",
                    selectedValue = state.selectedGrade,
                    placeholder = "Select grade",
                    options = availableGrades,
                    onSelected = onGradeSelected,
                    modifier = Modifier.weight(1f)
                )

                // Subject Dropdown
                DropdownPickerBox(
                    label = "SUBJECT",
                    selectedValue = state.selectedSubject,
                    placeholder = "Select subject",
                    options = availableSubjects,
                    onSelected = onSubjectSelected,
                    modifier = Modifier.weight(1f)
                )

                // Chapter Dropdown
                DropdownPickerBox(
                    label = "CHAPTER",
                    selectedValue = state.selectedChapter,
                    placeholder = "Select chapter",
                    options = availableChapters,
                    onSelected = onChapterSelected,
                    modifier = Modifier.weight(1.3f)
                )
            }

            Spacer(Modifier.height(12.dp))

            // Duration Dropdown
            DurationPickerBox(
                selectedDuration = state.selectedDurationMinutes,
                options = availableDurations,
                onSelected = onDurationSelected,
                modifier = Modifier.width(160.dp)
            )
        } else {
            // Manual Mode: Custom inputs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DropdownPickerBox(
                    label = "SUBJECT",
                    selectedValue = state.selectedSubject,
                    placeholder = "Select subject",
                    options = availableSubjects,
                    onSelected = onSubjectSelected,
                    modifier = Modifier.weight(1f)
                )

                DurationPickerBox(
                    selectedDuration = state.selectedDurationMinutes,
                    options = availableDurations,
                    onSelected = onDurationSelected,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        // Context Pill Bar with grip, board chip, duration chip, and arrow
        ContextPillBar(
            board = state.selectedBoard,
            durationMinutes = state.selectedDurationMinutes,
            availableBoards = availableBoards,
            onBoardSelected = onBoardSelected
        )

        Spacer(Modifier.height(14.dp))

        // Topic Input Field + Arrow Up Submit Button
        PromptInputRow(
            prompt = state.topicPrompt,
            isGenerating = state.isGenerating,
            onPromptChange = onTopicPromptChange,
            onSubmit = onGenerate
        )
    }
}

/**
 * Segmented switcher between From Textbook and Manual modes.
 */
@Composable
private fun ModeSwitcherRow(
    currentMode: LessonPlanCreationMode,
    onModeSelected: (LessonPlanCreationMode) -> Unit
) {
    val shape = RoundedCornerShape(10.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(ModeSelectorBg)
            .border(1.dp, ModeSelectorBorder, shape)
            .padding(3.dp)
    ) {
        // From Textbook tab
        val isTextbook = currentMode == LessonPlanCreationMode.FROM_TEXTBOOK
        val tabShape = RoundedCornerShape(8.dp)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .height(38.dp)
                .clip(tabShape)
                .background(if (isTextbook) VibrantButtonBlue else Color.Transparent)
                .clickable { onModeSelected(LessonPlanCreationMode.FROM_TEXTBOOK) }
                .padding(horizontal = 12.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.MenuBook,
                contentDescription = null,
                tint = if (isTextbook) Color.White else Lavender,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "From Textbook",
                color = if (isTextbook) Color.White else Lavender,
                fontSize = 13.sp,
                fontWeight = if (isTextbook) FontWeight.SemiBold else FontWeight.Medium
            )
        }

        // Manual tab
        val isManual = currentMode == LessonPlanCreationMode.MANUAL
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .height(38.dp)
                .clip(tabShape)
                .background(if (isManual) VibrantButtonBlue else Color.Transparent)
                .clickable { onModeSelected(LessonPlanCreationMode.MANUAL) }
                .padding(horizontal = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                tint = if (isManual) Color.White else Lavender,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Manual",
                color = if (isManual) Color.White else Lavender,
                fontSize = 13.sp,
                fontWeight = if (isManual) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}

/**
 * Dropdown box with uppercase label, placeholder, and selectable menu options.
 */
@Composable
private fun DropdownPickerBox(
    label: String,
    selectedValue: String?,
    placeholder: String,
    options: List<String>,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val boxShape = RoundedCornerShape(8.dp)

    Column(modifier = modifier) {
        Text(
            text = label,
            color = Lavender,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )

        Spacer(Modifier.height(4.dp))

        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .clip(boxShape)
                    .background(DropdownBg)
                    .border(1.dp, DropdownBorder, boxShape)
                    .clickable { expanded = true }
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = selectedValue ?: placeholder,
                    color = if (selectedValue != null) TextPrimary else Color(0xFF6B7EAB),
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF6B7EAB),
                    modifier = Modifier.size(18.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(SoftField)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, color = TextPrimary, fontSize = 13.sp) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/**
 * Dropdown selector specifically for lesson duration in minutes.
 */
@Composable
private fun DurationPickerBox(
    selectedDuration: Int,
    options: List<Int>,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val boxShape = RoundedCornerShape(8.dp)

    Column(modifier = modifier) {
        Text(
            text = "DURATION",
            color = Lavender,
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )

        Spacer(Modifier.height(4.dp))

        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .clip(boxShape)
                    .background(DropdownBg)
                    .border(1.dp, DropdownBorder, boxShape)
                    .clickable { expanded = true }
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = "$selectedDuration min",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    maxLines = 1
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF6B7EAB),
                    modifier = Modifier.size(18.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(SoftField)
            ) {
                options.forEach { duration ->
                    DropdownMenuItem(
                        text = { Text("$duration min", color = TextPrimary, fontSize = 13.sp) },
                        onClick = {
                            onSelected(duration)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/**
 * Contextual pill bar showcasing the syllabus board, duration, and expandable parameters.
 */
@Composable
private fun ContextPillBar(
    board: String,
    durationMinutes: Int,
    availableBoards: List<String>,
    onBoardSelected: (String) -> Unit
) {
    var boardExpanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.DragIndicator,
            contentDescription = null,
            tint = Color(0xFF5D6E9A),
            modifier = Modifier.size(16.dp)
        )

        Spacer(Modifier.width(8.dp))

        // Board Pill
        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ContextPillBg)
                    .border(1.dp, ContextPillBorder, RoundedCornerShape(6.dp))
                    .clickable { boardExpanded = true }
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = board,
                    color = Color(0xFFCAD5F5),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            DropdownMenu(
                expanded = boardExpanded,
                onDismissRequest = { boardExpanded = false },
                modifier = Modifier.background(SoftField)
            ) {
                availableBoards.forEach { b ->
                    DropdownMenuItem(
                        text = { Text(b, color = TextPrimary, fontSize = 12.sp) },
                        onClick = {
                            onBoardSelected(b)
                            boardExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.width(6.dp))

        // Duration Pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(ContextPillBg)
                .border(1.dp, ContextPillBorder, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Text(
                text = "$durationMinutes min",
                color = Color(0xFFCAD5F5),
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.weight(1f))

        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = Color(0xFF6B7EAB),
            modifier = Modifier.size(18.dp)
        )
    }
}

/**
 * Free-form focus topic input bar with circular submit arrow button.
 */
@Composable
private fun PromptInputRow(
    prompt: String,
    isGenerating: Boolean,
    onPromptChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val fieldShape = RoundedCornerShape(12.dp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(fieldShape)
            .background(DropdownBg)
            .border(1.dp, DropdownBorder, fieldShape)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        TextField(
            value = prompt,
            onValueChange = onPromptChange,
            placeholder = {
                Text(
                    text = "Topic to plan — e.g. Newton's Second Law",
                    color = Color(0xFF5D6B8F),
                    fontSize = 13.5.sp
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            singleLine = true,
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (isGenerating) Color(0xFF1D3265) else VibrantButtonBlue)
                .clickable(enabled = !isGenerating, onClick = onSubmit),
            contentAlignment = Alignment.Center
        ) {
            if (isGenerating) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(16.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = "Generate Lesson Plan",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

/**
 * Bottom-pinned rounded pill button for opening revision notes.
 */
@Composable
private fun NotesFloatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    com.example.myapplication.ui.common.NotesFloatingButton(
        onClick = onClick,
        modifier = modifier
    )
}
