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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.MindMapSummary
import com.example.myapplication.ui.theme.AccentLink
import com.example.myapplication.ui.theme.FieldFill
import com.example.myapplication.ui.theme.FieldStroke
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary

private val Lavender = Color(0xFF8F9BD6)
private val SoftField = Color(0xFF0B1530)
private val VibrantButtonBlue = Color(0xFF3875FF)
private val RagBadgeBg = Color(0xFF0C241B)
private val RagBadgeBorder = Color(0xFF1A5A3D)
private val RagBadgeText = Color(0xFF2EE59D)

/**
 * Stateful entry route for the Mind Maps feature.
 *
 * @param viewModel Controller managing Mind Map state and querying.
 * @param onGenerateMindMapClick Triggered to launch generation wizard.
 * @param onMindMapClick Callback when an existing Mind Map is selected.
 */
@Composable
fun MindMapRoute(
    viewModel: MindMapViewModel,
    onGenerateMindMapClick: () -> Unit,
    onMindMapClick: (id: String) -> Unit
) {
    val state = viewModel.uiState

    MindMapScreen(
        state = state,
        onBoardSelected = viewModel::onBoardSelected,
        onSubjectSelected = viewModel::onSubjectSelected,
        onClearFilters = viewModel::clearFilters,
        onRetry = viewModel::retry,
        onGenerateMindMapClick = onGenerateMindMapClick,
        onMindMapClick = onMindMapClick
    )
}

/**
 * Stateless screen rendering the Mind Maps header, filter controls,
 * responsive grid of concept map cards, and empty/error states.
 */
@Composable
fun MindMapScreen(
    state: MindMapUiState,
    onBoardSelected: (String?) -> Unit,
    onSubjectSelected: (String?) -> Unit,
    onClearFilters: () -> Unit,
    onRetry: () -> Unit,
    onGenerateMindMapClick: () -> Unit,
    onMindMapClick: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(14.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f, fill = false)) {
                Text(
                    text = "Mind Maps",
                    color = TextPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    text = "Your saved mind maps.",
                    color = Lavender,
                    fontSize = 14.5.sp
                )
            }

            Spacer(Modifier.width(12.dp))

            GenerateMindMapButton(onClick = onGenerateMindMapClick)
        }

        Spacer(Modifier.height(18.dp))

        MindMapFilterBar(
            selectedBoard = state.boardFilter,
            selectedSubject = state.subjectFilter,
            onBoardSelected = onBoardSelected,
            onSubjectSelected = onSubjectSelected
        )

        Spacer(Modifier.height(16.dp))

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

                state.mindMaps.isEmpty() -> {
                    EmptyState(
                        hasFilters = state.hasFilters,
                        onGenerateClick = onGenerateMindMapClick,
                        onClearFilters = onClearFilters
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 280.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        items(
                            items = state.mindMaps,
                            key = { it.id }
                        ) { item ->
                            MindMapCard(
                                mindMap = item,
                                onClick = { onMindMapClick(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Top-right primary action button to initiate Mind Map generation.
 */
@Composable
private fun GenerateMindMapButton(
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
        contentPadding = PaddingValues(horizontal = 14.dp),
        modifier = modifier.height(40.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(17.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "Generate Mind Map",
            color = Color.White,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

/**
 * Filter row displaying uppercase "FILTER" label with board and subject dropdown selectors.
 */
@Composable
private fun MindMapFilterBar(
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
            text = "FILTER",
            color = Lavender,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )

        MindMapDropdown(
            allLabel = "All boards",
            options = mindMapBoardOptions,
            selected = selectedBoard,
            onSelected = onBoardSelected,
            modifier = Modifier.weight(1f, fill = false)
        )

        MindMapDropdown(
            allLabel = "All subjects",
            options = mindMapSubjectOptions,
            selected = selectedSubject,
            onSelected = onSubjectSelected,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}

/**
 * Dropdown selector styled with subtle container fill and stroke borders.
 */
@Composable
private fun MindMapDropdown(
    allLabel: String,
    options: List<String>,
    selected: String?,
    onSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(10.dp)

    Box(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(38.dp)
                .clip(shape)
                .background(SoftField)
                .border(1.dp, FieldStroke, shape)
                .clickable { expanded = true }
                .padding(horizontal = 12.dp)
        ) {
            Text(
                text = selected ?: allLabel,
                color = TextPrimary,
                fontSize = 13.5.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(18.dp)
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
 * Individual Mind Map card showing title, RAG badge, syllabus metadata, and node/level metrics.
 */
@Composable
private fun MindMapCard(
    mindMap: MindMapSummary,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(14.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SoftField)
            .border(1.dp, FieldStroke, cardShape)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = mindMap.title,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            if (mindMap.isRagEnabled) {
                Spacer(Modifier.width(8.dp))
                RagBadge()
            }
        }

        Spacer(Modifier.height(5.dp))

        Text(
            text = mindMap.breadcrumb,
            color = Lavender,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.4.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(14.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.AccountTree,
                contentDescription = null,
                tint = Lavender,
                modifier = Modifier.size(15.dp)
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = mindMap.statsDescription,
                color = Lavender,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Green badge indicating syllabus document grounding.
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
 * Empty state view when no Mind Maps are found.
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
                .background(Color(0xFF13203E))
        ) {
            Icon(
                imageVector = Icons.Outlined.AccountTree,
                contentDescription = null,
                tint = Color(0xFFCAD5F5),
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = if (hasFilters) "No Mind Maps match." else "No Mind Maps yet.",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = if (hasFilters) "Try resetting the board or subject filters." else "Generate your first concept mind map to visualize relationships.",
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
                Text("Generate Mind Map", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

/**
 * Error state with retry callback for failed queries.
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
            text = "Unable to load Mind Maps",
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
