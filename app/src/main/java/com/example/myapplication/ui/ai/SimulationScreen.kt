package com.example.myapplication.ui.ai

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Description
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.SimulationSummary
import com.example.myapplication.data.model.SimulationVisualType
import com.example.myapplication.ui.theme.AccentLink
import com.example.myapplication.ui.theme.ErrorText
import com.example.myapplication.ui.theme.FieldFill
import com.example.myapplication.ui.theme.FieldStroke
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary

private val Lavender = Color(0xFF8F9BD6)
private val SoftField = Color(0xFF0B1530)
private val SearchFieldStroke = Color(0xFF24304E)
private val VibrantButtonBlue = Color(0xFF3875FF)
private val ThumbnailBg = Color(0xFF060B18)
private val SubjectChipBg = Color(0xFF152243)
private val PlayButtonBg = Color(0xCC0A1224)
private val NotesPillBg = Color(0xFF101A34)

/**
 * Stateful entry route for PlayableCraft AI simulations screen.
 *
 * @param viewModel Controller managing simulation state, searching, and mutations.
 * @param onNewSimulationClick Triggered when creating a new simulation.
 * @param onSimulationClick Callback when selecting a simulation to launch or present.
 * @param onNotesClick Triggered when opening study notes or reference material.
 */
@Composable
fun SimulationRoute(
    viewModel: SimulationViewModel,
    onNewSimulationClick: () -> Unit,
    onSimulationClick: (id: String) -> Unit,
    onNotesClick: () -> Unit
) {
    val state = viewModel.uiState

    SimulationScreen(
        state = state,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        onClearSearch = viewModel::clearSearch,
        onRetry = viewModel::retry,
        onNewSimulationClick = onNewSimulationClick,
        onSimulationClick = onSimulationClick,
        onDeleteSimulation = viewModel::deleteSimulation,
        onNotesClick = onNotesClick
    )
}

/**
 * Stateless screen displaying the simulation header, search bar, creation CTA,
 * interactive card list, and bottom-pinned notes action.
 */
@Composable
fun SimulationScreen(
    state: SimulationUiState,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onRetry: () -> Unit,
    onNewSimulationClick: () -> Unit,
    onSimulationClick: (id: String) -> Unit,
    onDeleteSimulation: (id: String) -> Unit,
    onNotesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Playable Craft AI",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Your saved simulations. Open one to present, or create a new one.",
                color = Lavender,
                fontSize = 15.sp,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(18.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SimulationSearchBar(
                    query = state.searchQuery,
                    onQueryChange = onSearchQueryChange,
                    onClear = onClearSearch,
                    modifier = Modifier.weight(1f)
                )

                NewSimulationButton(
                    onClick = onNewSimulationClick
                )
            }

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

                    state.simulations.isEmpty() -> {
                        EmptyState(
                            isSearching = state.isSearchActive,
                            onNewSimulationClick = onNewSimulationClick,
                            onClearSearch = onClearSearch
                        )
                    }

                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 80.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = state.simulations,
                                key = { it.id }
                            ) { item ->
                                SimulationCard(
                                    simulation = item,
                                    onClick = { onSimulationClick(item.id) },
                                    onDelete = { onDeleteSimulation(item.id) }
                                )
                            }
                        }
                    }
                }
            }
        }

        NotesFloatingButton(
            onClick = onNotesClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp)
        )
    }
}

/**
 * Search input field styled with dark background, rounded stroke, and clear button.
 */
@Composable
private fun SimulationSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(12.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(44.dp)
            .clip(shape)
            .background(SoftField)
            .border(1.dp, SearchFieldStroke, shape)
            .padding(horizontal = 12.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = Color(0xFF7E8BAA),
            modifier = Modifier.size(18.dp)
        )

        Spacer(Modifier.width(8.dp))

        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = "Search simulations...",
                    color = Color(0xFF5E6C8C),
                    fontSize = 14.5.sp
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = TextPrimary,
                    fontSize = 14.5.sp
                ),
                cursorBrush = SolidColor(AccentLink),
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (query.isNotEmpty()) {
            IconButton(
                onClick = onClear,
                modifier = Modifier.size(22.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Clear search",
                    tint = Lavender,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}

/**
 * Primary action button for creating a new simulation.
 */
@Composable
private fun NewSimulationButton(
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
            text = "New simulation",
            color = Color.White,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
    }
}

/**
 * Individual interactive simulation card displaying preview canvas, topic,
 * chapter subtitle, discipline tag, and creation date.
 */
@Composable
private fun SimulationCard(
    simulation: SimulationSummary,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val cardShape = RoundedCornerShape(16.dp)
    var menuExpanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SoftField)
            .border(1.dp, FieldStroke, cardShape)
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        SimulationThumbnail(
            visualType = simulation.visualType,
            modifier = Modifier
                .width(130.dp)
                .height(86.dp)
        )

        Spacer(Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = simulation.title,
                    color = TextPrimary,
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

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

            Spacer(Modifier.height(3.dp))

            Text(
                text = "${simulation.board} · ${simulation.chapter}",
                color = Lavender,
                fontSize = 12.5.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )

            Spacer(Modifier.height(10.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SubjectChipBg)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = simulation.subject,
                        color = Color(0xFFC0CEFA),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = simulation.formattedDate,
                    color = Lavender,
                    fontSize = 12.sp
                )
            }
        }
    }
}

/**
 * Custom canvas thumbnail depicting an interactive 3D-styled science simulation
 * with a play button badge overlay in the corner.
 */
@Composable
private fun SimulationThumbnail(
    visualType: SimulationVisualType,
    modifier: Modifier = Modifier
) {
    val thumbShape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .clip(thumbShape)
            .background(ThumbnailBg)
            .border(1.dp, Color(0xFF1B2642), thumbShape)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            val centerNode = Offset(w * 0.52f, h * 0.50f)
            val nodeRadius = h * 0.16f

            val satellites = listOf(
                Offset(w * 0.28f, h * 0.35f),
                Offset(w * 0.76f, h * 0.32f),
                Offset(w * 0.70f, h * 0.72f),
                Offset(w * 0.34f, h * 0.75f),
                Offset(w * 0.54f, h * 0.20f)
            )

            satellites.forEach { sat ->
                drawLine(
                    color = Color(0xFF536792),
                    start = centerNode,
                    end = sat,
                    strokeWidth = 2.5.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF5D88FF), Color(0xFF1A46C4)),
                    center = centerNode,
                    radius = nodeRadius
                ),
                radius = nodeRadius,
                center = centerNode
            )

            satellites.forEach { sat ->
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFB5CAFF), Color(0xFF5B7FD8)),
                        center = sat,
                        radius = nodeRadius * 0.55f
                    ),
                    radius = nodeRadius * 0.55f,
                    center = sat
                )
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 8.dp, bottom = 8.dp)
                .size(26.dp)
                .clip(CircleShape)
                .background(PlayButtonBg)
                .border(1.dp, Color(0xFF324675), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play simulation",
                tint = Color.White,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

/**
 * Bottom-pinned rounded pill button for opening study and revision notes.
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

/**
 * Empty state displayed when query results or simulations are absent.
 */
@Composable
private fun EmptyState(
    isSearching: Boolean,
    onNewSimulationClick: () -> Unit,
    onClearSearch: () -> Unit
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
                imageVector = Icons.Outlined.AutoAwesome,
                contentDescription = null,
                tint = Color(0xFFCAD5F5),
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = if (isSearching) "No simulations match." else "No simulations yet.",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = if (isSearching) "Try a different search query." else "Create your first interactive 3D simulation.",
            color = TextSecondary,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(24.dp))

        if (isSearching) {
            OutlinedButton(
                onClick = onClearSearch,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, FieldStroke),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                modifier = Modifier.height(44.dp)
            ) {
                Text("Clear search", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        } else {
            Button(
                onClick = onNewSimulationClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VibrantButtonBlue),
                modifier = Modifier.height(44.dp)
            ) {
                Text("New simulation", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

/**
 * Error state with retry callback for failed simulation requests.
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
            text = "Unable to load simulations",
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
