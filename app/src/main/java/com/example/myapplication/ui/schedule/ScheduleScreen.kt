package com.example.myapplication.ui.schedule

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Inbox
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.DeliveryContentType
import com.example.myapplication.data.model.DeliveryItem
import com.example.myapplication.ui.theme.AccentLink
import com.example.myapplication.ui.theme.FieldFill
import com.example.myapplication.ui.theme.FieldStroke
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary

private val Lavender = Color(0xFF8F9BD6)
private val SoftField = Color(0xFF0B1530)
private val TrayIconBg = Color(0xFF13203E)
private val TrayIconBorder = Color(0xFF24355E)
private val BatchTagBg = Color(0xFF152243)
private val VibrantButtonBlue = Color(0xFF3875FF)

/**
 * Stateful entry point route for the Schedule / Deliveries feature.
 *
 * @param viewModel Controller managing deliveries and scheduling state.
 */
@Composable
fun ScheduleRoute(
    viewModel: ScheduleViewModel
) {
    val state = viewModel.uiState

    ScheduleScreen(
        state = state,
        onBatchSelected = viewModel::onBatchSelected,
        onOpenCreateDialog = viewModel::openCreateDialog,
        onCloseCreateDialog = viewModel::closeCreateDialog,
        onCreateDelivery = viewModel::createDelivery,
        onDeleteDelivery = viewModel::deleteDelivery,
        onRetry = viewModel::retry
    )
}

/**
 * Stateless screen rendering the Deliveries header, batch filter controls,
 * empty state matching the reference design, and delivery list.
 */
@Composable
fun ScheduleScreen(
    state: ScheduleUiState,
    onBatchSelected: (String?) -> Unit,
    onOpenCreateDialog: () -> Unit,
    onCloseCreateDialog: () -> Unit,
    onCreateDelivery: (String, DeliveryContentType, String, String) -> Unit,
    onDeleteDelivery: (id: String) -> Unit,
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

            ScheduleHeaderRow(
                onOpenCreateDialog = onOpenCreateDialog
            )

            Spacer(Modifier.height(16.dp))

            ScheduleFilterBar(
                selectedBatch = state.batchFilter,
                dateRangeText = state.dateRangeText,
                onBatchSelected = onBatchSelected
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

                    state.deliveries.isEmpty() -> {
                        EmptyState()
                    }

                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(bottom = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = state.deliveries,
                                key = { it.id }
                            ) { delivery ->
                                DeliveryCard(
                                    delivery = delivery,
                                    onDelete = { onDeleteDelivery(delivery.id) }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (state.showCreateDialog) {
            CreateDeliveryDialog(
                onDismiss = onCloseCreateDialog,
                onConfirm = onCreateDelivery
            )
        }
    }
}

/**
 * Top header containing Deliveries title, subtitle, and white "+ New delivery" button.
 */
@Composable
private fun ScheduleHeaderRow(
    onOpenCreateDialog: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.weight(1f, fill = false)) {
            Text(
                text = "Deliveries",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = "Simulations, lesson plans, and MCQ sets sent to your batches.",
                color = Lavender,
                fontSize = 14.sp,
                lineHeight = 19.sp
            )
        }

        Spacer(Modifier.width(16.dp))

        Button(
            onClick = onOpenCreateDialog,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF0A1122)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
            contentPadding = PaddingValues(horizontal = 14.dp),
            modifier = Modifier.height(40.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = Color(0xFF0A1122),
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "New delivery",
                color = Color(0xFF0A1122),
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}

/**
 * Filter bar featuring calendar icon, Batch dropdown selector, and active date range text.
 */
@Composable
private fun ScheduleFilterBar(
    selectedBatch: String?,
    dateRangeText: String,
    onBatchSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(8.dp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Outlined.CalendarMonth,
            contentDescription = null,
            tint = Lavender,
            modifier = Modifier.size(18.dp)
        )

        Spacer(Modifier.width(10.dp))

        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(36.dp)
                    .clip(shape)
                    .background(SoftField)
                    .border(1.dp, FieldStroke, shape)
                    .clickable { expanded = true }
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = selectedBatch ?: "Batch",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Lavender,
                    modifier = Modifier.size(16.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = FieldFill
            ) {
                batchOptions.forEach { batch ->
                    DropdownMenuItem(
                        text = { Text(batch, color = TextPrimary, fontSize = 13.5.sp) },
                        onClick = {
                            onBatchSelected(batch)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.width(14.dp))

        Text(
            text = dateRangeText,
            color = Lavender,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Empty state matching the reference design with inbox tray illustration and guidance text.
 */
@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(TrayIconBg)
                .border(1.dp, TrayIconBorder, RoundedCornerShape(12.dp))
        ) {
            Icon(
                imageVector = Icons.Outlined.Inbox,
                contentDescription = null,
                tint = Color(0xFFCAD5F5),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(Modifier.height(18.dp))

        Text(
            text = "No deliveries yet. Use the New delivery button to send\ncontent to a batch.",
            color = Lavender,
            fontSize = 14.5.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

/**
 * Individual card presenting a scheduled content package, target batch, and delete action.
 */
@Composable
private fun DeliveryCard(
    delivery: DeliveryItem,
    onDelete: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SoftField)
            .border(1.dp, FieldStroke, shape)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = delivery.title,
                    color = TextPrimary,
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(BatchTagBg)
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = delivery.contentType.label,
                        color = Color(0xFFBACAF8),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = "${delivery.targetBatch}  ·  Scheduled for ${delivery.scheduledDateFormatted}",
                color = Lavender,
                fontSize = 12.5.sp
            )
        }

        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Cancel delivery",
                tint = Lavender,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/**
 * Dialog to configure and schedule a new educational content delivery.
 */
@Composable
private fun CreateDeliveryDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, contentType: DeliveryContentType, batch: String, date: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(DeliveryContentType.SIMULATION) }
    var selectedBatch by remember { mutableStateOf("Class 10 - Batch A") }
    var scheduledDate by remember { mutableStateOf("Oct 15, 2026") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SoftField,
        title = {
            Text("Schedule New Delivery", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Content title (e.g. Ohm's Law)", color = TextSecondary) },
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
                    value = scheduledDate,
                    onValueChange = { scheduledDate = it },
                    placeholder = { Text("Scheduled Date", color = TextSecondary) },
                    singleLine = true,
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
                onClick = { onConfirm(title, selectedType, selectedBatch, scheduledDate) },
                colors = ButtonDefaults.buttonColors(containerColor = VibrantButtonBlue)
            ) {
                Text("Schedule delivery")
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
            text = "Unable to load deliveries",
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
