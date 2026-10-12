package com.example.myapplication.ui.profile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import com.example.myapplication.data.model.UserProfile
import com.example.myapplication.ui.theme.AccentLink
import com.example.myapplication.ui.theme.FieldStroke
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary

private val Lavender = Color(0xFF8F9BD6)
private val SoftField = Color(0xFF0B1530)
private val VibrantButtonBlue = Color(0xFF3875FF)
private val AvatarBg = Color(0xFF162343)
private val AvatarBorder = Color(0xFF2A3A62)
private val TagPillBg = Color(0xFF121F3E)
private val TagPillBorder = Color(0xFF22345E)
private val ActionIconBoxBg = Color(0xFF121E3B)
private val ActionIconBoxBorder = Color(0xFF202E52)
private val FeedbackFieldBg = Color(0xFF070E1F)
private val FeedbackFieldBorder = Color(0xFF1D2947)
private val LogoutRed = Color(0xFFEF4444)
private val LogoutBg = Color(0x14EF4444)

/**
 * Stateful route component binding the ProfileViewModel to the UI.
 *
 * @param viewModel Controller managing user credentials and actions.
 * @param onNavigateToRoute Callback for quick action screen navigation.
 * @param onLogout Callback for terminating user session and returning to login.
 */
@Composable
fun ProfileRoute(
    viewModel: ProfileViewModel,
    onNavigateToRoute: (String) -> Unit,
    onLogout: () -> Unit
) {
    val state = viewModel.uiState

    ProfileScreen(
        state = state,
        onStartEdit = viewModel::startEditing,
        onDismissEdit = viewModel::dismissEditing,
        onEditNameChange = viewModel::onEditNameChange,
        onEditEmailChange = viewModel::onEditEmailChange,
        onSaveProfile = viewModel::saveProfile,
        onFeedbackChange = viewModel::onFeedbackChange,
        onSubmitFeedback = viewModel::submitFeedback,
        onDismissFeedbackBanner = viewModel::dismissFeedbackBanner,
        onPromptSignOut = viewModel::promptSignOut,
        onDismissSignOut = viewModel::dismissSignOut,
        onConfirmSignOut = {
            viewModel.dismissSignOut()
            onLogout()
        },
        onNavigateToRoute = onNavigateToRoute,
        onRetry = viewModel::retry
    )
}

/**
 * Stateless screen displaying the user's profile card, account information,
 * quick actions, feedback submission form, and sign out section.
 */
@Composable
fun ProfileScreen(
    state: ProfileUiState,
    onStartEdit: () -> Unit,
    onDismissEdit: () -> Unit,
    onEditNameChange: (String) -> Unit,
    onEditEmailChange: (String) -> Unit,
    onSaveProfile: () -> Unit,
    onFeedbackChange: (String) -> Unit,
    onSubmitFeedback: () -> Unit,
    onDismissFeedbackBanner: () -> Unit,
    onPromptSignOut: () -> Unit,
    onDismissSignOut: () -> Unit,
    onConfirmSignOut: () -> Unit,
    onNavigateToRoute: (String) -> Unit,
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

            Text(
                text = "Profile",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "View and manage your profile",
                color = Lavender,
                fontSize = 14.5.sp,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(18.dp))

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

                state.errorMessage != null && state.profile == null -> {
                    ProfileErrorState(
                        message = state.errorMessage,
                        onRetry = onRetry
                    )
                }

                state.profile != null -> {
                    ProfileContent(
                        profile = state.profile,
                        feedbackText = state.feedbackText,
                        isSubmittingFeedback = state.isSubmittingFeedback,
                        feedbackSuccessMessage = state.feedbackSuccessMessage,
                        onStartEdit = onStartEdit,
                        onFeedbackChange = onFeedbackChange,
                        onSubmitFeedback = onSubmitFeedback,
                        onDismissFeedbackBanner = onDismissFeedbackBanner,
                        onPromptSignOut = onPromptSignOut,
                        onNavigateToRoute = onNavigateToRoute
                    )
                }
            }
        }

        // Edit Profile Dialog
        if (state.isEditing) {
            EditProfileDialog(
                name = state.editName,
                email = state.editEmail,
                isSaving = state.isSavingProfile,
                onNameChange = onEditNameChange,
                onEmailChange = onEditEmailChange,
                onSave = onSaveProfile,
                onDismiss = onDismissEdit
            )
        }

        // Sign Out Confirmation Dialog
        if (state.showSignOutDialog) {
            SignOutConfirmationDialog(
                onConfirm = onConfirmSignOut,
                onDismiss = onDismissSignOut
            )
        }
    }
}

/**
 * Scrollable list containing all profile sections.
 */
@Composable
private fun ProfileContent(
    profile: UserProfile,
    feedbackText: String,
    isSubmittingFeedback: Boolean,
    feedbackSuccessMessage: String?,
    onStartEdit: () -> Unit,
    onFeedbackChange: (String) -> Unit,
    onSubmitFeedback: () -> Unit,
    onDismissFeedbackBanner: () -> Unit,
    onPromptSignOut: () -> Unit,
    onNavigateToRoute: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            IdentityCard(
                profile = profile,
                onEditClick = onStartEdit
            )
        }

        item {
            AccountInformationCard(
                profile = profile,
                onEditClick = onStartEdit
            )
        }

        item {
            QuickActionsCard(
                onNavigateToRoute = onNavigateToRoute
            )
        }

        item {
            FeedbackCard(
                feedbackText = feedbackText,
                isSubmitting = isSubmittingFeedback,
                successMessage = feedbackSuccessMessage,
                onFeedbackChange = onFeedbackChange,
                onSubmit = onSubmitFeedback,
                onDismissSuccess = onDismissFeedbackBanner
            )
        }

        item {
            SignOutCard(
                onSignOutClick = onPromptSignOut
            )
        }
    }
}

/**
 * Primary identity summary card with large initials avatar, name, email, tags, and edit button.
 */
@Composable
private fun IdentityCard(
    profile: UserProfile,
    onEditClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(16.dp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SoftField)
            .border(1.dp, FieldStroke, cardShape)
            .padding(16.dp)
    ) {
        // Large Circular Avatar with Camera overlay
        Box(
            modifier = Modifier.size(80.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(AvatarBg)
                    .border(1.5.dp, AvatarBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = profile.initials,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            // Camera overlay icon
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0D1730))
                    .border(1.dp, Color(0xFF2C3C65), CircleShape)
                    .align(Alignment.BottomEnd)
                    .clickable(onClick = onEditClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.PhotoCamera,
                    contentDescription = "Change photo",
                    tint = Lavender,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(Modifier.width(16.dp))

        // Name, Email, Tags, and Edit button
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = profile.fullName,
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.width(8.dp))

                EditPillButton(onClick = onEditClick)
            }

            Spacer(Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = null,
                    tint = Lavender,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = profile.email,
                    color = Lavender,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(10.dp))

            // Badges row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileTagPill(
                    icon = Icons.Outlined.School,
                    text = profile.role
                )

                Spacer(Modifier.width(8.dp))

                ProfileTagPill(
                    icon = Icons.Outlined.CalendarToday,
                    text = "Joined ${profile.memberSince}"
                )
            }
        }
    }
}

/**
 * Small pill button displaying a pencil icon and "Edit" label.
 */
@Composable
private fun EditPillButton(
    onClick: () -> Unit
) {
    val pillShape = RoundedCornerShape(8.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(pillShape)
            .background(Color(0xFF0F1A35))
            .border(1.dp, Color(0xFF233256), pillShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = "Edit",
            tint = Lavender,
            modifier = Modifier.size(13.dp)
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = "Edit",
            color = Lavender,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Pill badge for tags such as Teacher or Joined Date.
 */
@Composable
private fun ProfileTagPill(
    icon: ImageVector,
    text: String
) {
    val shape = RoundedCornerShape(20.dp)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape)
            .background(TagPillBg)
            .border(1.dp, TagPillBorder, shape)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Lavender,
            modifier = Modifier.size(13.dp)
        )
        Spacer(Modifier.width(5.dp))
        Text(
            text = text,
            color = Lavender,
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Card presenting personal account attributes in key-value format.
 */
@Composable
private fun AccountInformationCard(
    profile: UserProfile,
    onEditClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(16.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SoftField)
            .border(1.dp, FieldStroke, cardShape)
            .padding(16.dp)
    ) {
        // Section Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = Lavender,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = "Account Information",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            EditPillButton(onClick = onEditClick)
        }

        Spacer(Modifier.height(18.dp))

        AccountInfoRow(label = "Full Name", value = profile.fullName)
        Spacer(Modifier.height(14.dp))
        AccountInfoRow(label = "Email Address", value = profile.email)
        Spacer(Modifier.height(14.dp))
        AccountInfoRow(label = "Role", value = profile.role)
        Spacer(Modifier.height(14.dp))
        AccountInfoRow(label = "Member Since", value = profile.memberSince)
    }
}

/**
 * Single key-value entry row in the Account Information card.
 */
@Composable
private fun AccountInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Lavender,
            fontSize = 13.5.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1.5f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Card offering direct navigation shortcuts to key workspace features.
 */
@Composable
private fun QuickActionsCard(
    onNavigateToRoute: (String) -> Unit
) {
    val cardShape = RoundedCornerShape(16.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SoftField)
            .border(1.dp, FieldStroke, cardShape)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.GridView,
                contentDescription = null,
                tint = Lavender,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Quick Actions",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))

        QuickActionRow(
            icon = Icons.AutoMirrored.Outlined.Assignment,
            title = "Lesson Plans",
            subtitle = "View your lesson plans",
            onClick = { onNavigateToRoute("teacher/plan-lesson") }
        )

        Spacer(Modifier.height(12.dp))

        QuickActionRow(
            icon = Icons.Outlined.Checklist,
            title = "MCQ Sets",
            subtitle = "View your MCQ sets",
            onClick = { onNavigateToRoute("teacher/mcq") }
        )

        Spacer(Modifier.height(12.dp))

        QuickActionRow(
            icon = Icons.Outlined.PlayArrow,
            title = "Simulations",
            subtitle = "View your simulations",
            onClick = { onNavigateToRoute("teacher/ai") }
        )

        Spacer(Modifier.height(12.dp))

        QuickActionRow(
            icon = Icons.AutoMirrored.Outlined.MenuBook,
            title = "StoryLines",
            subtitle = "View your storylines",
            onClick = { onNavigateToRoute("teacher/storyline") }
        )
    }
}

/**
 * Single actionable navigation row with icon container, title, subtitle, and trailing arrow.
 */
@Composable
private fun QuickActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    val rowShape = RoundedCornerShape(10.dp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(rowShape)
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(ActionIconBoxBg)
                .border(1.dp, ActionIconBoxBorder, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Lavender,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = Lavender,
                fontSize = 12.sp
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFF6B7EAB),
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * Card facilitating user feedback and feature requests.
 */
@Composable
private fun FeedbackCard(
    feedbackText: String,
    isSubmitting: Boolean,
    successMessage: String?,
    onFeedbackChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismissSuccess: () -> Unit
) {
    val cardShape = RoundedCornerShape(16.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SoftField)
            .border(1.dp, FieldStroke, cardShape)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.ChatBubbleOutline,
                contentDescription = null,
                tint = Lavender,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = "Write Feedback",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(14.dp))

        // Multiline input field
        val fieldShape = RoundedCornerShape(10.dp)
        TextField(
            value = feedbackText,
            onValueChange = onFeedbackChange,
            placeholder = {
                Text(
                    text = "Write feedback/request features...",
                    color = Color(0xFF5D6B8F),
                    fontSize = 13.5.sp
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = FeedbackFieldBg,
                unfocusedContainerColor = FeedbackFieldBg,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = fieldShape,
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .clip(fieldShape)
                .border(1.dp, FeedbackFieldBorder, fieldShape)
        )

        // Success message banner if present
        if (successMessage != null) {
            Spacer(Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0F2B1D))
                    .border(1.dp, Color(0xFF1E5B3C), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .clickable(onClick = onDismissSuccess)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = Color(0xFF4ADE80),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = successMessage,
                    color = Color(0xFFB4F2CB),
                    fontSize = 12.5.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        // Send Feedback Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = onSubmit,
                enabled = feedbackText.isNotBlank() && !isSubmitting,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VibrantButtonBlue,
                    disabledContainerColor = Color(0xFF1D3265)
                ),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Send,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Send Feedback",
                        color = Color.White,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/**
 * Card providing clear sign out options and session termination guidance.
 */
@Composable
private fun SignOutCard(
    onSignOutClick: () -> Unit
) {
    val cardShape = RoundedCornerShape(16.dp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SoftField)
            .border(1.dp, FieldStroke, cardShape)
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.Logout,
            contentDescription = null,
            tint = LogoutRed,
            modifier = Modifier.size(24.dp)
        )

        Spacer(Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Sign out",
                color = TextPrimary,
                fontSize = 15.5.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "End your session on this device. You can safely sign in again anytime.",
                color = Lavender,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }

        Spacer(Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(LogoutBg)
                .border(1.dp, LogoutRed, RoundedCornerShape(8.dp))
                .clickable(onClick = onSignOutClick)
                .padding(horizontal = 14.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sign out",
                color = LogoutRed,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

/**
 * Modal dialog enabling modification of user name and email.
 */
@Composable
private fun EditProfileDialog(
    name: String,
    email: String,
    isSaving: Boolean,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SoftField,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = "Edit Profile",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Update your display name and email address.",
                    color = Lavender,
                    fontSize = 13.sp
                )

                TextField(
                    value = name,
                    onValueChange = onNameChange,
                    label = { Text("Full Name", color = Lavender) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = FeedbackFieldBg,
                        unfocusedContainerColor = FeedbackFieldBg,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email Address", color = Lavender) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = FeedbackFieldBg,
                        unfocusedContainerColor = FeedbackFieldBg,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onSave,
                enabled = name.isNotBlank() && email.isNotBlank() && !isSaving,
                colors = ButtonDefaults.buttonColors(containerColor = VibrantButtonBlue)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
                } else {
                    Text("Save")
                }
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel", color = Lavender)
            }
        }
    )
}

/**
 * Confirmation dialog before ending the user's active session.
 */
@Composable
private fun SignOutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SoftField,
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = "Sign out",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Are you sure you want to end your session? You can sign in again anytime.",
                color = Lavender,
                fontSize = 13.5.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = LogoutRed)
            ) {
                Text("Sign out", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel", color = Lavender)
            }
        }
    )
}

/**
 * Error state with retry callback for failed profile loading.
 */
@Composable
private fun ProfileErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Unable to load Profile",
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
