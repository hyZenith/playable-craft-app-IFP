package com.example.myapplication.ui.login

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.ui.theme.*

// ── Public API ───────────────────────────────────────────────────

data class LoginCallbacks(
    val onCreateAccount: () -> Unit,
    val onForgotPassword: () -> Unit
)

@Composable
fun LoginRoute(
    viewModel: LoginViewModel,
    callbacks: LoginCallbacks,
    onLoginSuccess: (user: LoginResponse, keepSignedIn: Boolean) -> Unit
) {
    val state = viewModel.uiState

    LaunchedEffect(state.loggedInUser) {
        state.loggedInUser?.let { user ->
            onLoginSuccess(user, state.keepSignedIn)
            viewModel.consumeLoginSuccess()
        }
    }

    LoginScreen(
        selectedRole              = state.selectedRole,
        identifier                = state.identifier,
        password                  = state.password,
        keepSignedIn              = state.keepSignedIn,
        passwordVisible           = state.passwordVisible,
        isLoading                 = state.isLoading,
        errorMessage              = state.errorMessage,
        onRoleSelected            = viewModel::onRoleSelected,
        onIdentifierChange        = viewModel::onIdentifierChange,
        onPasswordChange          = viewModel::onPasswordChange,
        onKeepSignedInToggle      = viewModel::onKeepSignedInToggle,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onSignIn                  = viewModel::login,
        onCreateAccount           = callbacks.onCreateAccount,
        onForgotPassword          = callbacks.onForgotPassword
    )
}

/**
 * Backdrop Disc
 *
 * A very large circle whose centre is off-screen to the right.
 * Only its left arc is visible, reading as a "semi-circle".
 * It does NOT scroll with the form - stays pinned to the screen.
 * Gradient fades from DiscTop -> DiscMid -> transparent.
 */
@Composable
private fun BackdropDisc(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .clearAndSetSemantics {}          // decorative, no a11y
    ) {
        val baseDim = minOf(size.width, size.height)
        val r = baseDim * 0.85f
        val c = Offset(
            x = size.width * 1.15f,           // centre is off right edge
            y = size.height * 0.38f            // aligned with chip row
        )
        drawCircle(
            brush = Brush.verticalGradient(
                colorStops = arrayOf(
                    0.0f to DiscTop,                     // brightest
                    0.5f to DiscMid.copy(alpha = 0.80f), // mid fade
                    1.0f to Color.Transparent            // dissolves into bg
                ),
                startY = size.height * 0.05f,
                endY   = size.height * 0.50f
            ),
            radius = r,
            center = c
        )
    }
}

/**
 * Stateless Login Screen
 */
@Composable
private fun LoginScreen(
    selectedRole: SignInRole,
    identifier: String,
    password: String,
    keepSignedIn: Boolean,
    passwordVisible: Boolean,
    isLoading: Boolean,
    errorMessage: String?,
    onRoleSelected: (SignInRole) -> Unit,
    onIdentifierChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onKeepSignedInToggle: (Boolean) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onSignIn: () -> Unit,
    onCreateAccount: () -> Unit,
    onForgotPassword: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val fieldShape = RoundedCornerShape(12.dp)

    val fieldColors = TextFieldDefaults.colors(
        focusedContainerColor   = FieldFill,
        unfocusedContainerColor = FieldFill,
        disabledContainerColor  = FieldFill,
        focusedIndicatorColor   = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor  = Color.Transparent,
        cursorColor             = Accent,
        focusedTextColor        = TextPrimary,
        unfocusedTextColor      = TextPrimary,
        focusedPlaceholderColor = TextPlaceholder,
        unfocusedPlaceholderColor = TextPlaceholder,
        focusedLeadingIconColor  = TextPlaceholder,
        unfocusedLeadingIconColor = TextPlaceholder,
        focusedTrailingIconColor  = TextLabel,
        unfocusedTrailingIconColor = TextLabel
    )

    val horizontalPad = 24.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Bg)
            .clipToBounds()               // disc never overflows screen
    ) {
        // ── Layer 1: Backdrop disc (pinned, doesn't scroll) ─────
        BackdropDisc()

        // ── Layer 2: Centered responsive column ──────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 480.dp)
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
                // ── #1  Top bar ─────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPad)
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Wordmark: "Playable" white Bold + "Craft" secondary Bold
                    Row {
                        Text(
                            text = "Playable ",
                            color = TextPrimary,
                            style = WordmarkStyle
                        )
                        Text(
                            text = "Craft",
                            color = TextSecondary,
                            style = WordmarkStyle
                        )
                    }
                    // Two-line link: "New here?" / "Create account →"
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "New here?",
                            color = TextSecondary,
                            fontSize = AppFontSize.BodySmall
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable(onClick = onCreateAccount)
                        ) {
                            Text(
                                text = "Create account",
                                color = AccentLink,
                                fontSize = AppFontSize.Body,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = AccentLink,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // ── #2  Headline ────────────────────────────────────
                Spacer(Modifier.height(36.dp))
                Text(
                    text = "Welcome back,",
                    style = HeadlineStyle,
                    modifier = Modifier.padding(horizontal = horizontalPad)
                )

                // ── #3  Subtitle ────────────────────────────────────
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Pick up where your last simulation left off.",
                    style = SubtitleStyle,
                    modifier = Modifier
                        .padding(horizontal = horizontalPad)
                        .fillMaxWidth()
                )

                // ── #4  SIGN IN AS (role chips) ─────────────────────
                Spacer(Modifier.height(24.dp))
                SectionLabel("SIGN IN AS", Modifier.padding(horizontal = horizontalPad))
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPad)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SignInRole.entries.forEach { role ->
                        RoleChip(
                            label = role.label,
                            selected = role == selectedRole,
                            onClick = { onRoleSelected(role) }
                        )
                    }
                }

                // ── #5  IDENTIFIER (Application ID for Teacher & Tutor, email for others) ──
                val usesAppId = selectedRole.usesApplicationId
                Spacer(Modifier.height(20.dp))
                SectionLabel(if (usesAppId) "APPLICATION ID" else "EMAIL", Modifier.padding(horizontal = horizontalPad))
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = identifier,
                    onValueChange = onIdentifierChange,
                    placeholder = { Text(if (usesAppId) "SCH-XXXX-XXXX" else "you@example.com", fontSize = 15.sp) },
                    singleLine = true,
                    colors = fieldColors,
                    shape = fieldShape,
                    textStyle = TextStyle(fontSize = 15.sp, color = TextPrimary),
                    leadingIcon = {
                        Icon(
                            imageVector = if (usesAppId) Icons.Filled.Tag else Icons.Filled.Email,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = if (usesAppId) KeyboardType.Ascii else KeyboardType.Email,
                        capitalization = if (usesAppId) KeyboardCapitalization.Characters else KeyboardCapitalization.None,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPad)
                        .heightIn(min = 56.dp)
                        .clip(fieldShape)
                        .border(1.dp, FieldStroke, fieldShape)
                )

                // ── #6  PASSWORD ────────────────────────────────────
                Spacer(Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPad),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionLabel("PASSWORD")
                    Text(
                        text = "Forgot password?",
                        color = AccentLink,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable(onClick = onForgotPassword)
                    )
                }
                Spacer(Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    placeholder = {
                        Text("Enter your password", fontSize = 15.sp)
                    },
                    singleLine = true,
                    colors = fieldColors,
                    shape = fieldShape,
                    textStyle = TextStyle(fontSize = 15.sp, color = TextPrimary),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = onTogglePasswordVisibility) {
                            Icon(
                                imageVector = if (passwordVisible)
                                    Icons.Filled.Visibility
                                else
                                    Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisible)
                                    "Hide password" else "Show password",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            onSignIn()
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPad)
                        .heightIn(min = 56.dp)
                        .clip(fieldShape)
                        .border(1.dp, FieldStroke, fieldShape)
                )

                // ── #7  Remember me ─────────────────────────────────
                Spacer(Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = horizontalPad)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onKeepSignedInToggle(!keepSignedIn) }
                ) {
                    Checkbox(
                        checked = keepSignedIn,
                        onCheckedChange = onKeepSignedInToggle,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Accent,
                            uncheckedColor = FieldStroke,
                            checkmarkColor = TextPrimary
                        ),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Keep me signed in for 30 days",
                        color = TextPrimary,
                        fontSize = 14.sp
                    )
                }

                // ── Error banner (if any) ───────────────────────────
                if (errorMessage != null) {
                    Spacer(Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = horizontalPad)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ErrorFill)
                            .border(1.dp, ErrorBorder, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = errorMessage,
                            color = ErrorText,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // ── #8  Primary CTA ─────────────────────────────────
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = onSignIn,
                    enabled = !isLoading,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Accent,
                        contentColor = TextPrimary,
                        disabledContainerColor = Accent.copy(alpha = 0.40f),
                        disabledContentColor = TextPrimary.copy(alpha = 0.50f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPad)
                        .height(56.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = TextPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = "Sign in",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))

                // ── #11  Footer ─────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPad)
                        .padding(bottom = 24.dp, top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                        Text("Privacy", color = TextLabel, fontSize = 14.sp)
                        Text("Terms",   color = TextLabel, fontSize = 14.sp)
                        Text("Help",    color = TextLabel, fontSize = 14.sp)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Status", color = TextLabel, fontSize = 14.sp)
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(StatusGreen)
                        )
                        Text("ok", color = TextPrimary, fontSize = 14.sp)
                    }
                }
            }
        }
    }

// Reusable Components

// Section label (uppercase)

@Composable
private fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = SectionLabelStyle,
        modifier = modifier
    )
}

// ── Role chip ────────────────────────────────────────────────────
// Selected: solid white bg, BLACK text, no border.
// Unselected: transparent bg (disc shows through), white text, 1 px stroke.

@Composable
private fun RoleChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val chipShape = RoundedCornerShape(10.dp)
    val bgColor = if (selected) ChipSelectedBg else Color.Transparent
    val textColor = if (selected) ChipSelectedText else TextPrimary

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(38.dp)
            .clip(chipShape)
            .background(bgColor)
            .then(
                if (!selected) Modifier.border(1.dp, FieldStroke, chipShape)
                else Modifier
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp)
    ) {
        CompositionLocalProvider(LocalContentColor provides textColor) {
            Text(
                text = label,
                color = textColor,
                fontSize = 14.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}
