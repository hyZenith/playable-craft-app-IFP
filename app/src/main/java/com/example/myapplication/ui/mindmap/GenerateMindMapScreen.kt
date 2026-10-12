package com.example.myapplication.ui.mindmap

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
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.myapplication.data.repo.MindMapRepository
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private val PageBackground = Color(0xFF070B16)
private val CardBackground = Color(0xFF0D162C)
private val CardBorder = Color(0xFF1C2C4E)

private val InputBoxBg = Color(0xFF080F22)
private val InputBoxBorder = Color(0xFF1E2D4E)
private val LabelColor = Color(0xFF657696)
private val PlaceholderColor = Color(0xFF526382)
private val SubtitleColor = Color(0xFF5E6E8C)

private val SliderInactiveColor = Color(0xFF233252)
private val GenerateBtnBg = Color(0xFFDCE4F0)
private val GenerateBtnText = Color(0xFF152238)

private val NotesPillBg = Color(0xFF101A34)
private val NotesPillBorder = Color(0xFF223154)
private val NotesLavender = Color(0xFF8F9BD6)

/**
 * Stateful route for generating a new Mind Map.
 */
@Composable
fun GenerateMindMapRoute(
    repository: MindMapRepository,
    onBackClick: () -> Unit,
    onMindMapGenerated: (mapId: String) -> Unit,
    onNotesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var isGenerating by remember { mutableStateOf(false) }

    var selectedCurriculum by remember { mutableStateOf<String?>(null) }
    var depth by remember { mutableFloatStateOf(4f) }
    var focusThemes by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }

    val curriculumOptions = remember {
        listOf(
            "Class 10 · Mathematics (SEBA)",
            "Class 10 · Science (SEBA)",
            "Class 9 · Science (CBSE)",
            "Class 10 · Social Science (NCERT)",
            "Class 11 · Physics (SEBA)",
            "Class 12 · Chemistry (CBSE)"
        )
    }

    GenerateMindMapScreen(
        selectedCurriculum = selectedCurriculum,
        curriculumOptions = curriculumOptions,
        onCurriculumSelect = { selectedCurriculum = it },
        depth = depth,
        onDepthChange = { depth = it },
        focusThemes = focusThemes,
        onFocusThemesChange = { focusThemes = it },
        title = title,
        onTitleChange = { title = it },
        isGenerating = isGenerating,
        onGenerateClick = {
            val curriculum = selectedCurriculum ?: "Class 10 · Science (SEBA)"
            isGenerating = true
            scope.launch {
                val result = repository.create(
                    curriculum = curriculum,
                    depth = depth.roundToInt(),
                    focusThemes = focusThemes.ifBlank { null },
                    title = title.ifBlank { null }
                )
                isGenerating = false
                result.onSuccess { created ->
                    onMindMapGenerated(created.id)
                }
            }
        },
        onBackClick = onBackClick,
        onNotesClick = onNotesClick,
        modifier = modifier
    )
}

/**
 * Pixel-accurate screen layout matching the "Generate Mind Map" specification.
 */
@Composable
fun GenerateMindMapScreen(
    selectedCurriculum: String?,
    curriculumOptions: List<String>,
    onCurriculumSelect: (String) -> Unit,
    depth: Float,
    onDepthChange: (Float) -> Unit,
    focusThemes: String,
    onFocusThemesChange: (String) -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
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
            // Header: ← Generate Mind Map
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
                    text = "Generate Mind Map",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Column(
                modifier = Modifier
                    .widthIn(max = 880.dp)
                    .fillMaxWidth()
            ) {
                // Card 1: CURRICULUM
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                        .padding(18.dp)
                ) {
                    Text(
                        text = "CURRICULUM",
                        color = LabelColor,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    CurriculumDropdownField(
                        value = selectedCurriculum,
                        placeholder = "Select grade · subject...",
                        options = curriculumOptions,
                        onOptionSelect = onCurriculumSelect
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Card 2: DEPTH, FOCUS THEMES, TITLE
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                        .padding(18.dp)
                ) {
                    // Depth (Levels)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DEPTH (LEVELS)",
                            color = LabelColor,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${depth.roundToInt()}",
                            color = Color.White,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Slider(
                        value = depth,
                        onValueChange = onDepthChange,
                        valueRange = 2f..6f,
                        steps = 3,
                        colors = SliderDefaults.colors(
                            activeTrackColor = Color.White,
                            inactiveTrackColor = SliderInactiveColor,
                            thumbColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "More levels means more detail, but a bigger map. 4 works well for most chapters.",
                        color = SubtitleColor,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Focus Themes (Optional)
                    Text(
                        text = "FOCUS THEMES (OPTIONAL)",
                        color = LabelColor,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(InputBoxBg)
                            .border(1.dp, InputBoxBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 14.dp, vertical = 11.dp)
                    ) {
                        if (focusThemes.isEmpty()) {
                            Text(
                                text = "Comma-separated, e.g. photosynthesis, chlorophyll",
                                color = PlaceholderColor,
                                fontSize = 12.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        BasicTextField(
                            value = focusThemes,
                            onValueChange = onFocusThemesChange,
                            singleLine = true,
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 12.5.sp
                            ),
                            cursorBrush = SolidColor(NotesLavender),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Title (Optional)
                    Text(
                        text = "TITLE (OPTIONAL)",
                        color = LabelColor,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(InputBoxBg)
                            .border(1.dp, InputBoxBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 14.dp, vertical = 11.dp)
                    ) {
                        if (title.isEmpty()) {
                            Text(
                                text = "Auto-generated if left blank",
                                color = PlaceholderColor,
                                fontSize = 12.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        BasicTextField(
                            value = title,
                            onValueChange = onTitleChange,
                            singleLine = true,
                            textStyle = TextStyle(
                                color = Color.White,
                                fontSize = 12.5.sp
                            ),
                            cursorBrush = SolidColor(NotesLavender),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Generate mind map CTA Button
                Button(
                    onClick = onGenerateClick,
                    enabled = !isGenerating,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GenerateBtnBg,
                        contentColor = GenerateBtnText
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 9.dp),
                    modifier = Modifier.height(38.dp)
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(
                            color = GenerateBtnText,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Generating...",
                            color = GenerateBtnText,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.AutoAwesome,
                            contentDescription = null,
                            tint = GenerateBtnText,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Generate mind map",
                            color = GenerateBtnText,
                            fontSize = 12.5.sp,
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
 * Dropdown trigger field for curriculum selection.
 */
@Composable
private fun CurriculumDropdownField(
    value: String?,
    placeholder: String,
    options: List<String>,
    onOptionSelect: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box {
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
                text = value ?: placeholder,
                color = if (value != null) Color.White else PlaceholderColor,
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
