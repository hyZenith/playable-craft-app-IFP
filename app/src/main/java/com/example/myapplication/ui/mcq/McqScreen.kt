package com.example.myapplication.ui.mcq

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.McqSummary
import com.example.myapplication.ui.theme.AccentLink
import com.example.myapplication.ui.theme.ErrorText
import com.example.myapplication.ui.theme.FieldFill
import com.example.myapplication.ui.theme.FieldStroke
import com.example.myapplication.ui.theme.StatusGreen
import com.example.myapplication.ui.theme.TextLabel
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary

private val Lavender = Color(0xFF8F9BD6)
private val SoftField = Color(0xFF0B1530)
private val CardIconContainerBg = Color(0xFF13203E)
private val VibrantButtonBlue = Color(0xFF3875FF)
private val ArrowCircleBg = Color(0xFF152243)
private val RagBadgeBg = Color(0xFF0C241B)
private val RagBadgeBorder = Color(0xFF1A5A3D)
private val RagBadgeText = Color(0xFF2EE59D)

/**
 * Stateful entry point route for the MCQ Sets feature.
 *
 * @param viewModel Controller managing state and user actions.
 * @param onGenerateMcqClick Triggered when user initiates new MCQ generation.
 * @param onConnectDocsClick Triggered to attach external PDF or doc context.
 * @param onMcqSetClick Callback with the set ID when a card is selected.
 */
@Composable
fun McqRoute(
    viewModel: McqViewModel,
    onGenerateMcqClick: () -> Unit,
    onConnectDocsClick: () -> Unit,
    onMcqSetClick: (id: String) -> Unit
) {
    val state = viewModel.uiState

    McqScreen(
        state = state,
        onBoardSelected = viewModel::onBoardSelected,
        onSubjectSelected = viewModel::onSubjectSelected,
        onClearFilters = viewModel::clearFilters,
        onRetry = viewModel::retry,
        onGenerateMcqClick = onGenerateMcqClick,
        onConnectDocsClick = onConnectDocsClick,
        onMcqSetClick = onMcqSetClick,
        onDeleteSet = viewModel::deleteSet
    )
}

/**
 * Stateless screen rendering the header, action buttons, board/subject filters,
 * and list of MCQ set cards matching the Playable Craft design system.
 */
@Composable
fun McqScreen(
    state: McqUiState,
    onBoardSelected: (String?) -> Unit,
    onSubjectSelected: (String?) -> Unit,
    onClearFilters: () -> Unit,
    onRetry: () -> Unit,
    onGenerateMcqClick: () -> Unit,
    onConnectDocsClick: () -> Unit,
    onMcqSetClick: (id: String) -> Unit,
    onDeleteSet: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        Text(
            text = "MCQ Sets",
            color = TextPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Your saved MCQ Sets.",
            color = Lavender,
            fontSize = 15.sp
        )

        Spacer(Modifier.height(18.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ConnectDocsButton(
                onClick = onConnectDocsClick,
                modifier = Modifier.weight(1f)
            )

            GenerateMcqButton(
                onClick = onGenerateMcqClick,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(18.dp))

        FilterBar(
            selectedBoard = state.boardFilter,
            selectedSubject = state.subjectFilter,
            onBoardSelected = onBoardSelected,
            onSubjectSelected = onSubjectSelected
        )

        Spacer(Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        color = AccentLink,
                        strokeWidth = 3.dp,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(36.dp)
                    )
                }

                state.errorMessage != null -> {
                    ErrorState(
                        message = state.errorMessage,
                        onRetry = onRetry
                    )
                }

                state.mcqSets.isEmpty() -> {
                    EmptyState(
                        hasFilters = state.hasFilters,
                        onGenerateClick = onGenerateMcqClick,
                        onClearFilters = onClearFilters
                    )
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(
                            items = state.mcqSets,
                            key = { idx, mcqSet -> "${mcqSet.id}_$idx" }
                        ) { _, mcqSet ->
                            McqSetCard(
                                mcqSet = mcqSet,
                                onClick = { onMcqSetClick(mcqSet.id) },
                                onDelete = { onDeleteSet(mcqSet.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Secondary outlined action button to connect document context or PDFs.
 */
@Composable
private fun ConnectDocsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .height(44.dp)
            .clip(shape)
            .background(SoftField)
            .border(1.dp, FieldStroke, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Link,
            contentDescription = null,
            tint = TextPrimary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Connect Docs/PDF",
            color = TextPrimary,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Primary accent button to launch the MCQ generation flow.
 */
@Composable
private fun GenerateMcqButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = VibrantButtonBlue,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        contentPadding = PaddingValues(horizontal = 12.dp),
        modifier = modifier.height(44.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "Generate MCQ",
            color = Color.White,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Horizontal filter section with "Filter" label and board/subject dropdown selectors.
 */
@Composable
private fun FilterBar(
    selectedBoard: String?,
    selectedSubject: String?,
    onBoardSelected: (String?) -> Unit,
    onSubjectSelected: (String?) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Filter",
            color = Lavender,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        McqFilterDropdown(
            allLabel = "All boards",
            options = mcqBoardOptions,
            selected = selectedBoard,
            onSelected = onBoardSelected,
            modifier = Modifier.weight(1f)
        )

        McqFilterDropdown(
            allLabel = "All subjects",
            options = mcqSubjectOptions,
            selected = selectedSubject,
            onSelected = onSubjectSelected,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Dropdown selector styled with subtle container fill and stroke borders.
 */
@Composable
private fun McqFilterDropdown(
    allLabel: String,
    options: List<String>,
    selected: String?,
    onSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(12.dp)

    Box(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 42.dp)
                .clip(shape)
                .background(SoftField)
                .border(1.dp, FieldStroke, shape)
                .clickable { expanded = true }
                .padding(start = 14.dp, end = 10.dp)
        ) {
            Text(
                text = selected ?: allLabel,
                color = TextPrimary,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = FieldFill
        ) {
            DropdownMenuItem(
                text = { Text(allLabel, color = TextPrimary, fontSize = 14.sp) },
                onClick = {
                    onSelected(null)
                    expanded = false
                }
            )
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = TextPrimary, fontSize = 14.sp) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Individual MCQ set card presenting subject icon, title, RAG indicator,
 * board breadcrumbs, question metrics, and navigation affordance.
 */
@Composable
private fun McqSetCard(
    mcqSet: McqSummary,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SoftField)
            .border(1.dp, FieldStroke, shape)
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(CardIconContainerBg)
        ) {
            Icon(
                imageVector = subjectIcon(mcqSet.subject),
                contentDescription = null,
                tint = Color(0xFF6B8EFF),
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(
                        text = mcqSet.title,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (mcqSet.ragGrounded) {
                        Spacer(Modifier.width(8.dp))
                        RagBadge()
                    }
                }

                Box {
                    IconButton(
                        onClick = { menuExpanded = true },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "Options",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        containerColor = FieldFill
                    ) {
                        DropdownMenuItem(
                            text = { Text("Delete", color = ErrorText, fontSize = 14.sp) },
                            onClick = {
                                menuExpanded = false
                                onDelete()
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            val metadataText = "${mcqSet.board.uppercase()}  ·  ${mcqSet.subject.uppercase()}  ·  ${mcqSet.grade.uppercase()}"
            Text(
                text = metadataText,
                color = Lavender,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.4.sp
            )

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.HelpOutline,
                    contentDescription = null,
                    tint = Lavender,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "${mcqSet.questionCount} Q",
                    color = Lavender,
                    fontSize = 12.sp
                )

                Text(
                    text = "·",
                    color = Lavender,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )

                Icon(
                    imageVector = Icons.Outlined.Article,
                    contentDescription = null,
                    tint = Lavender,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "${mcqSet.optionsPerQuestion} opt",
                    color = Lavender,
                    fontSize = 12.sp
                )

                Text(
                    text = "·",
                    color = Lavender,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )

                Text(
                    text = mcqSet.difficulty.replaceFirstChar { it.uppercase() },
                    color = Lavender,
                    fontSize = 12.sp
                )

                if (mcqSet.timer.enabled) {
                    Text(
                        text = "·",
                        color = Lavender,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                    Text(
                        text = "${mcqSet.timer.secondsPerQuestion ?: 60}s/Q",
                        color = Lavender,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(Modifier.width(8.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(ArrowCircleBg)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "View MCQ set",
                tint = TextPrimary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/**
 * Green indicator badge representing Retrieval-Augmented Generation context.
 */
@Composable
private fun RagBadge() {
    val shape = RoundedCornerShape(6.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape)
            .background(RagBadgeBg)
            .border(1.dp, RagBadgeBorder, shape)
            .padding(horizontal = 5.dp, vertical = 2.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Description,
            contentDescription = null,
            tint = RagBadgeText,
            modifier = Modifier.size(11.dp)
        )
        Spacer(Modifier.width(3.dp))
        Text(
            text = "RAG",
            color = RagBadgeText,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Maps subject names to their respective vector iconography.
 */
private fun subjectIcon(subject: String): ImageVector {
    return when (subject.lowercase()) {
        "science" -> Icons.Outlined.Science
        "mathematics", "maths", "math" -> Icons.Outlined.Calculate
        "english" -> Icons.AutoMirrored.Outlined.MenuBook
        "social science" -> Icons.Outlined.Public
        "environmental science", "evs" -> Icons.Outlined.Eco
        else -> Icons.Outlined.Checklist
    }
}

/**
 * Empty state displayed when no sets exist or when filters yield zero results.
 */
@Composable
private fun EmptyState(
    hasFilters: Boolean,
    onGenerateClick: () -> Unit,
    onClearFilters: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(CardIconContainerBg)
        ) {
            Icon(
                imageVector = Icons.Outlined.Checklist,
                contentDescription = null,
                tint = Color(0xFFCAD5F5),
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = if (hasFilters) "No MCQ sets match." else "No MCQ sets yet.",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = if (hasFilters) "Try resetting the board or subject filters." else "Generate your first MCQ set to get started.",
            color = TextSecondary,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        if (hasFilters) {
            OutlinedButton(
                onClick = onClearFilters,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FieldStroke),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                modifier = Modifier.height(44.dp)
            ) {
                Text("Clear filters", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        } else {
            Button(
                onClick = onGenerateClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VibrantButtonBlue),
                modifier = Modifier.height(44.dp)
            ) {
                Text("Generate MCQ", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

/**
 * Error state with retry callback for handling network or retrieval issues.
 */
@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Unable to load MCQ sets",
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = message,
            color = TextSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(18.dp))
        OutlinedButton(
            onClick = onRetry,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, FieldStroke),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
        ) {
            Text("Try again")
        }
    }
}
