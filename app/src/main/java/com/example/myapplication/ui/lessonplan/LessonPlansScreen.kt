package com.example.myapplication.ui.lessonplan

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.outlined.MenuBook
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.LessonPlanSummary
import com.example.myapplication.ui.theme.*

private val Lavender = Color(0xFF8F9BD6)
private val SoftField = Color(0xFF0B1530)
private val BadgeBg = Color(0xFF14203D)
private val VibrantButtonBlue = Color(0xFF3875FF)

// ── Route (stateful) ─────────────────────────────────────────────

@Composable
fun LessonPlansRoute(
    viewModel: LessonPlansViewModel,
    onCreateClick: () -> Unit,
    onPlanClick: (planId: String) -> Unit
) {
    val state = viewModel.uiState

    Column(
        Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp)
    ) {
        // ── Header ──────────────────────────────────────────────
        Spacer(Modifier.height(12.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Lesson Plans",
                color = TextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(Modifier.width(10.dp))
            CreateButton(
                onClick = onCreateClick,
                height = 40.dp,
                fontSize = 13.5.sp
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            "AI-structured plans aligned to your syllabus.",
            color = Lavender,
            fontSize = 14.5.sp
        )

        // ── Filter Dropdowns (All boards, All subjects) ───────────
        Spacer(Modifier.height(18.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilterDropdown(
                allLabel = "All boards",
                options = boardOptions,
                selected = state.boardFilter,
                onSelected = viewModel::onBoardSelected,
                modifier = Modifier.weight(1f)
            )
            FilterDropdown(
                allLabel = "All subjects",
                options = subjectOptions,
                selected = state.subjectFilter,
                onSelected = viewModel::onSubjectSelected,
                modifier = Modifier.weight(1f)
            )
        }

        // ── Search Bar ───────────────────────────────────────────
        Spacer(Modifier.height(10.dp))
        SearchBar(
            query = state.searchQuery,
            onQueryChange = viewModel::onSearchQueryChanged,
            modifier = Modifier.fillMaxWidth()
        )

        // ── Body ────────────────────────────────────────────────
        Box(Modifier.weight(1f).fillMaxWidth()) {
            when {
                state.isLoading -> CircularProgressIndicator(
                    color = AccentLink,
                    strokeWidth = 3.dp,
                    modifier = Modifier.align(Alignment.Center).size(36.dp)
                )

                state.errorMessage != null -> MessageState(
                    title = "Something went wrong",
                    subtitle = state.errorMessage,
                    actionLabel = "Try again",
                    onAction = viewModel::retry
                )

                state.plans.isEmpty() -> EmptyState(
                    hasFilters = state.hasFilters,
                    onCreateClick = onCreateClick,
                    onClearFilters = viewModel::clearFilters
                )

                else -> Column(Modifier.fillMaxSize()) {
                    Spacer(Modifier.height(18.dp))
                    // ── Counter & Sort Row ───────────────────────
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${state.plans.size} Lesson Plans",
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        SortMenu(
                            currentSort = state.sortOrder,
                            onSortSelected = viewModel::onSortOrderSelected
                        )
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(top = 14.dp, bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(state.plans, key = { idx, plan -> "${plan._id}_$idx" }) { _, plan ->
                            PlanCard(plan = plan, onClick = { onPlanClick(plan._id) })
                        }
                    }
                }
            }
        }
    }
}

// ── Search bar ───────────────────────────────────────────────────

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .background(SoftField)
            .border(1.dp, FieldStroke, shape)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Search",
            tint = Color(0xFF7E8BAA),
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(10.dp))
        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = "Search lesson plans...",
                    color = Color(0xFF5E6C8C),
                    fontSize = 15.sp
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = TextPrimary,
                    fontSize = 15.sp
                ),
                cursorBrush = SolidColor(AccentLink),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (query.isNotEmpty()) {
            IconButton(
                onClick = { onQueryChange("") },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Clear search",
                    tint = Lavender,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// ── Sort menu ────────────────────────────────────────────────────

@Composable
private fun SortMenu(
    currentSort: PlanSortOrder,
    onSortSelected: (PlanSortOrder) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.SwapVert,
                contentDescription = "Sort",
                tint = Color(0xFFCAD5F5),
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = currentSort.label,
                color = Color(0xFFCAD5F5),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = FieldFill
        ) {
            PlanSortOrder.entries.forEach { order ->
                DropdownMenuItem(
                    text = {
                        Text(
                            order.label,
                            color = if (order == currentSort) AccentLink else TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = if (order == currentSort) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onSortSelected(order)
                        expanded = false
                    }
                )
            }
        }
    }
}

// ── Create button (used in header + empty state) ─────────────────

@Composable
private fun CreateButton(
    onClick: () -> Unit,
    height: Dp,
    fontSize: TextUnit,
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
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
        modifier = modifier.height(height)
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = "Create Lesson Plans",
            fontSize = fontSize,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            color = Color.White
        )
    }
}

// ── Filter dropdown ──────────────────────────────────────────────

@Composable
private fun FilterDropdown(
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
                .heightIn(min = 46.dp)
                .clip(shape)
                .background(SoftField)
                .border(1.dp, FieldStroke, shape)
                .clickable { expanded = true }
                .padding(start = 14.dp, end = 10.dp)
        ) {
            Text(
                selected ?: allLabel,
                color = TextPrimary,
                fontSize = 14.5.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Filled.KeyboardArrowDown,
                contentDescription = "Choose",
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
                text = { Text(allLabel, color = TextPrimary, fontSize = 14.5.sp) },
                onClick = { onSelected(null); expanded = false }
            )
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = TextPrimary, fontSize = 14.5.sp) },
                    onClick = { onSelected(option); expanded = false }
                )
            }
        }
    }
}

// ── Empty state ──────────────────────────────────────────────────

@Composable
private fun EmptyState(
    hasFilters: Boolean,
    onCreateClick: () -> Unit,
    onClearFilters: () -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Book badge with small spark ticks at the top-right
        Box(Modifier.size(110.dp), contentAlignment = Alignment.BottomStart) {
            Box(
                Modifier.size(76.dp).clip(CircleShape).background(BadgeBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.MenuBook,
                    contentDescription = null,
                    tint = Color(0xFFCAD5F5),
                    modifier = Modifier.size(36.dp)
                )
            }
            Canvas(Modifier.size(34.dp).align(Alignment.TopEnd)) {
                fun tick(a: Offset, b: Offset) = drawLine(
                    color = AccentLink,
                    start = a, end = b,
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
                tick(Offset(size.width * 0.30f, size.height * 0.38f), Offset(size.width * 0.22f, size.height * 0.10f))
                tick(Offset(size.width * 0.50f, size.height * 0.35f), Offset(size.width * 0.66f, size.height * 0.08f))
                tick(Offset(size.width * 0.62f, size.height * 0.55f), Offset(size.width * 0.98f, size.height * 0.48f))
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(
            if (hasFilters) "No lesson plans match." else "No lesson plans yet.",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            if (hasFilters) "Try a different search, board or subject." else "Generate your first plan using the form.",
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
                modifier = Modifier.height(48.dp)
            ) {
                Text("Clear filters", fontSize = 15.sp, fontWeight = FontWeight.Medium)
            }
        } else {
            CreateButton(onClick = onCreateClick, height = 48.dp, fontSize = 15.sp)
        }
    }
}

// ── Error state ──────────────────────────────────────────────────

@Composable
private fun MessageState(
    title: String,
    subtitle: String,
    actionLabel: String,
    onAction: () -> Unit
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text(subtitle, color = TextSecondary, fontSize = 15.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(20.dp))
        OutlinedButton(
            onClick = onAction,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, FieldStroke),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
        ) { Text(actionLabel) }
    }
}

// ── Plan card ────────────────────────────────────────────────────

@Composable
private fun PlanCard(plan: LessonPlanSummary, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SoftField)
            .border(1.dp, FieldStroke, shape)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(
            plan.topic,
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "${plan.subject}  ·  ${plan.grade}  ·  ${plan.board}",
            color = Lavender,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Pill(plan.difficultyLevel, difficultyColor(plan.difficultyLevel))
            Pill("${plan.estimatedDurationMinutes} min", TextLabel)
            if (plan.simulation.feasible) Pill("Simulation", AccentLink)
            if (plan.mcq.feasible) Pill("MCQ", AccentLink)
        }
        Spacer(Modifier.height(10.dp))
        Text(
            "Created ${plan.createdAt.take(10)}",
            color = TextLabel,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun Pill(text: String, color: Color) {
    Text(
        text,
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, color.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    )
}

private fun difficultyColor(level: String): Color = when (level) {
    "Easy" -> StatusGreen
    "Hard" -> ErrorText
    else -> Color(0xFFF5C451)
}
