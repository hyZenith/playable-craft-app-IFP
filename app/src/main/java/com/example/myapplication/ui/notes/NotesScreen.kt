package com.example.myapplication.ui.notes

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Unarchive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.NoteItem
import com.example.myapplication.data.model.NoteStatus
import com.example.myapplication.ui.theme.AccentLink
import com.example.myapplication.ui.theme.FieldFill
import com.example.myapplication.ui.theme.FieldStroke
import com.example.myapplication.ui.theme.TextLabel
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary

private val Lavender = Color(0xFF8F9BD6)
private val SoftField = Color(0xFF0B1530)
private val NotesPillBg = Color(0xFF101A34)
private val VibrantButtonBlue = Color(0xFF3875FF)
private val ContextTagBg = Color(0xFF152243)

/**
 * Stateful entry route for the Notes screen.
 *
 * @param viewModel Controller managing notes state, querying, and dialogs.
 */
@Composable
fun NotesRoute(
    viewModel: NotesViewModel
) {
    val state = viewModel.uiState

    NotesScreen(
        state = state,
        onStatusTabSelected = viewModel::onStatusTabSelected,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        onPageFilterSelected = viewModel::onPageFilterSelected,
        onOpenCreateDialog = viewModel::openCreateDialog,
        onCloseCreateDialog = viewModel::closeCreateDialog,
        onCreateNote = viewModel::createNote,
        onToggleArchive = viewModel::toggleArchive,
        onDeleteNote = viewModel::deleteNote,
        onRetry = viewModel::retry
    )
}

/**
 * Stateless screen displaying the Notes header, status segmented toggle,
 * search controls, empty state illustration, and note card list.
 */
@Composable
fun NotesScreen(
    state: NotesUiState,
    onStatusTabSelected: (NoteStatus) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onPageFilterSelected: (String?) -> Unit,
    onOpenCreateDialog: () -> Unit,
    onCloseCreateDialog: () -> Unit,
    onCreateNote: (String, String, String) -> Unit,
    onToggleArchive: (String) -> Unit,
    onDeleteNote: (String) -> Unit,
    onRetry: () -> Unit,
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
            Spacer(Modifier.height(14.dp))

            NotesResponsiveHeader(
                selectedStatus = state.statusTab,
                onStatusTabSelected = onStatusTabSelected
            )

            Spacer(Modifier.height(16.dp))

            NotesFilterControlsRow(
                query = state.searchQuery,
                onQueryChange = onSearchQueryChange,
                pageFilter = state.pageFilter,
                onPageFilterSelected = onPageFilterSelected
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

                    state.notes.isEmpty() -> {
                        EmptyState(
                            isSearching = state.isSearchOrFilterActive,
                            onOpenCreate = onOpenCreateDialog
                        )
                    }

                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 80.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = state.notes,
                                key = { it.id }
                            ) { note ->
                                NoteCard(
                                    note = note,
                                    onToggleArchive = { onToggleArchive(note.id) },
                                    onDelete = { onDeleteNote(note.id) }
                                )
                            }
                        }
                    }
                }
            }
        }

        NotesFloatingButton(
            onClick = onOpenCreateDialog,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp)
        )

        if (state.showCreateDialog) {
            CreateNoteDialog(
                onDismiss = onCloseCreateDialog,
                onConfirm = onCreateNote
            )
        }
    }
}

/**
 * Mobile-responsive header presenting title and subtitle on the left,
 * with the segmented status toggle on the right (wrapping gracefully on narrow displays).
 */
@Composable
private fun NotesResponsiveHeader(
    selectedStatus: NoteStatus,
    onStatusTabSelected: (NoteStatus) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f, fill = false)) {
            Text(
                text = "Notes",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = "Ideas, explanation tips and reminders. Notes taken on a page also appear there in the dock.",
                color = Lavender,
                fontSize = 14.sp,
                lineHeight = 19.sp
            )
        }

        Spacer(Modifier.width(16.dp))

        StatusSegmentedSwitch(
            selectedStatus = selectedStatus,
            onStatusSelected = onStatusTabSelected
        )
    }
}

/**
 * Segmented switcher between "Active" and "Archived" notes.
 */
@Composable
private fun StatusSegmentedSwitch(
    selectedStatus: NoteStatus,
    onStatusSelected: (NoteStatus) -> Unit
) {
    val pillShape = RoundedCornerShape(10.dp)
    Row(
        modifier = Modifier
            .clip(pillShape)
            .background(SoftField)
            .border(1.dp, FieldStroke, pillShape)
            .padding(3.dp)
    ) {
        val isActive = selectedStatus == NoteStatus.ACTIVE
        val isArchived = selectedStatus == NoteStatus.ARCHIVED

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isActive) Color.White else Color.Transparent)
                .clickable { onStatusSelected(NoteStatus.ACTIVE) }
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Inbox,
                contentDescription = null,
                tint = if (isActive) Color(0xFF0A1122) else Lavender,
                modifier = Modifier.size(15.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "Active",
                color = if (isActive) Color(0xFF0A1122) else Lavender,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isArchived) Color.White else Color.Transparent)
                .clickable { onStatusSelected(NoteStatus.ARCHIVED) }
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Archive,
                contentDescription = null,
                tint = if (isArchived) Color(0xFF0A1122) else Lavender,
                modifier = Modifier.size(15.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "Archived",
                color = if (isArchived) Color(0xFF0A1122) else Lavender,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Search field and page context dropdown selector arranged in a responsive row.
 */
@Composable
private fun NotesFilterControlsRow(
    query: String,
    onQueryChange: (String) -> Unit,
    pageFilter: String?,
    onPageFilterSelected: (String?) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        val shape = RoundedCornerShape(12.dp)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .clip(shape)
                .background(SoftField)
                .border(1.dp, FieldStroke, shape)
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
                        text = "Search notes",
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
                    onClick = { onQueryChange("") },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear",
                        tint = Lavender,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }

        PageContextDropdown(
            selectedPage = pageFilter,
            onPageSelected = onPageFilterSelected
        )
    }
}

/**
 * Dropdown filter for scoping notes by origin feature page.
 */
@Composable
private fun PageContextDropdown(
    selectedPage: String?,
    onPageSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(12.dp)

    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(44.dp)
                .clip(shape)
                .background(SoftField)
                .border(1.dp, FieldStroke, shape)
                .clickable { expanded = true }
                .padding(horizontal = 14.dp)
        ) {
            Text(
                text = selectedPage ?: "All pages",
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
                text = { Text("All pages", color = TextPrimary, fontSize = 14.sp) },
                onClick = {
                    onPageSelected(null)
                    expanded = false
                }
            )
            notePageOptions.forEach { page ->
                DropdownMenuItem(
                    text = { Text(page, color = TextPrimary, fontSize = 14.sp) },
                    onClick = {
                        onPageSelected(page)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Individual note card displaying title, preview body, origin tag, and actions.
 */
@Composable
private fun NoteCard(
    note: NoteItem,
    onToggleArchive: () -> Unit,
    onDelete: () -> Unit
) {
    val cardShape = RoundedCornerShape(14.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SoftField)
            .border(1.dp, FieldStroke, cardShape)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = note.title,
                color = TextPrimary,
                fontSize = 15.5.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ContextTagBg)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = note.pageContext,
                    color = Color(0xFFBACAF8),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = note.content,
            color = Lavender,
            fontSize = 13.5.sp,
            lineHeight = 18.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = note.createdAtFormatted,
                color = TextLabel,
                fontSize = 12.sp
            )

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(
                    onClick = onToggleArchive,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (note.status == NoteStatus.ACTIVE) Icons.Outlined.Archive else Icons.Outlined.Unarchive,
                        contentDescription = "Archive toggle",
                        tint = Lavender,
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete note",
                        tint = Lavender,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * Dialog enabling creation of a new quick note.
 */
@Composable
private fun CreateNoteDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, content: String, page: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedPage by remember { mutableStateOf("General") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SoftField,
        title = {
            Text("Jot down a Note", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Title or topic", color = TextSecondary) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = FieldFill,
                        unfocusedContainerColor = FieldFill,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("Thoughts, explanation tips, or reminder...", color = TextSecondary) },
                    minLines = 3,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = FieldFill,
                        unfocusedContainerColor = FieldFill,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(title, content, selectedPage) },
                colors = ButtonDefaults.buttonColors(containerColor = VibrantButtonBlue)
            ) {
                Text("Save note")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel", color = TextPrimary)
            }
        }
    )
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

/**
 * Empty state matching the reference design when zero notes are stored.
 */
@Composable
private fun EmptyState(
    isSearching: Boolean,
    onOpenCreate: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.Description,
            contentDescription = null,
            tint = Lavender,
            modifier = Modifier.size(42.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = if (isSearching) "No notes match your search." else "No notes yet. Open the Notes button on any page to jot one down.",
            color = Lavender,
            fontSize = 14.5.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        if (!isSearching) {
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = onOpenCreate,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VibrantButtonBlue),
                modifier = Modifier.height(40.dp)
            ) {
                Text("Take a note", color = Color.White, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
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
            text = "Unable to load notes",
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
