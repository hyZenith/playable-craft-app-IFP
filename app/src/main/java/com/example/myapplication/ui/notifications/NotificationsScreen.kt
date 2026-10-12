package com.example.myapplication.ui.notifications

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DoneAll
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.NotificationItem
import com.example.myapplication.data.model.NotificationType
import com.example.myapplication.ui.theme.AccentLink
import com.example.myapplication.ui.theme.FieldStroke
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary

private val Lavender = Color(0xFF8F9BD6)
private val SoftField = Color(0xFF0B1530)
private val CardBorder = Color(0xFF1E2B4B)
private val UnreadBlueDot = Color(0xFF3875FF)

/**
 * Stateful entry route for the Notifications screen.
 *
 * @param viewModel Controller managing user alerts and notification state.
 */
@Composable
fun NotificationsRoute(
    viewModel: NotificationsViewModel
) {
    val state = viewModel.uiState

    NotificationsScreen(
        state = state,
        onMarkAsRead = viewModel::markAsRead,
        onMarkAllAsRead = viewModel::markAllAsRead,
        onClearNotification = viewModel::clearNotification,
        onRetry = viewModel::retry
    )
}

/**
 * Stateless screen displaying the notifications header, empty state card matching
 * the reference screenshot, or actionable notification cards.
 */
@Composable
fun NotificationsScreen(
    state: NotificationsUiState,
    onMarkAsRead: (id: String) -> Unit,
    onMarkAllAsRead: () -> Unit,
    onClearNotification: (id: String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isWideScreen = LocalConfiguration.current.screenWidthDp >= 600

    Box(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(18.dp))

            // Responsive Header Row/Column
            NotificationsHeader(
                isWideScreen = isWideScreen,
                hasUnread = state.notifications.any { !it.isRead },
                onMarkAllAsRead = onMarkAllAsRead
            )

            Spacer(Modifier.height(20.dp))

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = AccentLink,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                state.errorMessage != null -> {
                    NotificationsErrorState(
                        message = state.errorMessage,
                        onRetry = onRetry
                    )
                }

                state.notifications.isEmpty() -> {
                    // Empty state matching the reference design image
                    NotificationsEmptyCard(isWideScreen = isWideScreen)
                }

                else -> {
                    // List of actionable notification cards
                    NotificationsList(
                        notifications = state.notifications,
                        onMarkAsRead = onMarkAsRead,
                        onClearNotification = onClearNotification
                    )
                }
            }
        }
    }
}

/**
 * Responsive header displaying the title, subtitle, and optional Mark All as Read button.
 */
@Composable
private fun NotificationsHeader(
    isWideScreen: Boolean,
    hasUnread: Boolean,
    onMarkAllAsRead: () -> Unit
) {
    if (isWideScreen) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notifications",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.3).sp
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = "View and manage your profile",
                    color = Lavender,
                    fontSize = 14.sp
                )
            }

            if (hasUnread) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF101B34))
                        .border(1.dp, Color(0xFF223050), RoundedCornerShape(8.dp))
                        .clickable(onClick = onMarkAllAsRead)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.DoneAll,
                        contentDescription = null,
                        tint = Lavender,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "Mark all as read",
                        color = Lavender,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Notifications",
                    color = TextPrimary,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )

                if (hasUnread) {
                    IconButton(onClick = onMarkAllAsRead) {
                        Icon(
                            imageVector = Icons.Outlined.DoneAll,
                            contentDescription = "Mark all as read",
                            tint = Lavender,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(3.dp))

            Text(
                text = "View and manage your profile",
                color = Lavender,
                fontSize = 14.sp
            )
        }
    }
}

/**
 * Centered empty state card matching the reference screenshot pixel-for-pixel.
 */
@Composable
private fun NotificationsEmptyCard(
    isWideScreen: Boolean
) {
    val cardShape = RoundedCornerShape(16.dp)
    val cardModifier = if (isWideScreen) {
        Modifier
            .widthIn(min = 380.dp, max = 460.dp)
            .height(180.dp)
    } else {
        Modifier
            .fillMaxWidth()
            .height(180.dp)
    }

    Box(
        modifier = cardModifier
            .clip(cardShape)
            .background(SoftField)
            .border(1.dp, CardBorder, cardShape),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Inbox,
                contentDescription = null,
                tint = Lavender,
                modifier = Modifier.size(32.dp)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "You're all caught up.",
                color = Lavender,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * List of notification item cards when notifications are active.
 */
@Composable
private fun NotificationsList(
    notifications: List<NotificationItem>,
    onMarkAsRead: (id: String) -> Unit,
    onClearNotification: (id: String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = notifications,
            key = { it.id }
        ) { notification ->
            NotificationCard(
                item = notification,
                onMarkAsRead = { onMarkAsRead(notification.id) },
                onClear = { onClearNotification(notification.id) }
            )
        }
    }
}

/**
 * Single interactive notification card with unread status indicator.
 */
@Composable
private fun NotificationCard(
    item: NotificationItem,
    onMarkAsRead: () -> Unit,
    onClear: () -> Unit
) {
    val cardShape = RoundedCornerShape(14.dp)
    val bg = if (item.isRead) SoftField else Color(0xFF0D1734)
    val border = if (item.isRead) CardBorder else Color(0xFF263A6E)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(bg)
            .border(1.dp, border, cardShape)
            .clickable(onClick = onMarkAsRead)
            .padding(14.dp)
    ) {
        // Category Icon Box
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF142042))
                .border(1.dp, Color(0xFF233568), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = getNotificationTypeIcon(item.type),
                contentDescription = null,
                tint = Lavender,
                modifier = Modifier.size(19.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        // Title and description
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    color = TextPrimary,
                    fontSize = 14.5.sp,
                    fontWeight = if (item.isRead) FontWeight.Medium else FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (!item.isRead) {
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(UnreadBlueDot)
                    )
                }
            }

            Spacer(Modifier.height(3.dp))

            Text(
                text = item.description,
                color = Lavender,
                fontSize = 12.5.sp,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = item.timestamp,
                color = Color(0xFF6B7EAB),
                fontSize = 11.5.sp
            )
        }

        Spacer(Modifier.width(8.dp))

        IconButton(
            onClick = onClear,
            modifier = Modifier.size(28.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Dismiss notification",
                tint = Color(0xFF6B7EAB),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/**
 * Returns the contextual icon for a notification category.
 */
private fun getNotificationTypeIcon(type: NotificationType): ImageVector = when (type) {
    NotificationType.SIMULATION -> Icons.Outlined.AutoAwesome
    NotificationType.LESSON_PLAN -> Icons.AutoMirrored.Outlined.Assignment
    NotificationType.MCQ -> Icons.Outlined.Checklist
    NotificationType.DELIVERY -> Icons.AutoMirrored.Outlined.MenuBook
    NotificationType.SYSTEM -> Icons.Outlined.Notifications
}

/**
 * Error state with retry callback for failed notifications loading.
 */
@Composable
private fun NotificationsErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Unable to load notifications",
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
            colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
        ) {
            Text("Try again")
        }
    }
}
