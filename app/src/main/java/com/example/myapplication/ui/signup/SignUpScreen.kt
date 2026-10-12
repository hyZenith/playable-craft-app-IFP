package com.example.myapplication.ui.signup

import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Apartment
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*

private val Lavender = Color(0xFF9FA9E0)
private val FieldShape = RoundedCornerShape(12.dp)

// ── Public API ───────────────────────────────────────────────────

@Composable
fun SignUpRoute(
    viewModel: SignUpViewModel,
    onSignInClick: () -> Unit,
    onSignedUp: () -> Unit
) {
    val state = viewModel.uiState
    val context = LocalContext.current

    LaunchedEffect(state.signedUp) {
        if (state.signedUp) {
            onSignedUp()
            viewModel.consumeSignedUp()
        }
    }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) {
            val name = context.contentResolver.query(uri, null, null, null, null)?.use { c ->
                val i = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (i >= 0 && c.moveToFirst()) c.getString(i) else null
            } ?: "certificate"
            viewModel.onCertificatePicked(uri.toString(), name)
        }
    }

    SignUpScreen(
        state = state,
        onSignInClick = onSignInClick,
        onRoleSelected = viewModel::onRoleSelected,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onVerifyEmail = viewModel::onVerifyEmail,
        onPhoneChange = viewModel::onPhoneChange,
        onVerifyPhone = viewModel::onVerifyPhone,
        onPasswordChange = viewModel::onPasswordChange,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onInstitutionTypeChange = viewModel::onInstitutionTypeChange,
        onUdiseChange = viewModel::onUdiseChange,
        onPickCertificate = {
            picker.launch(arrayOf("image/jpeg", "image/png", "application/pdf"))
        },
        onTermsToggle = viewModel::onTermsToggle,
        onSubmit = viewModel::onSubmit
    )
}

// ── Backdrop: top-right disc + bottom-left disc (pinned) ─────────

@Composable
private fun SignUpBackdrop() {
    Canvas(Modifier.fillMaxSize().clearAndSetSemantics {}) {
        val w = size.width
        val h = size.height
        val base = minOf(w, h)

        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(DiscTop, DiscMid.copy(alpha = 0.6f), Color.Transparent),
                startY = 0f,
                endY = h * 0.26f
            ),
            radius = base * 0.45f,
            center = Offset(w * 0.95f, h * 0.07f)
        )
        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, DiscMid.copy(alpha = 0.8f), DiscTop),
                startY = h * 0.84f,
                endY = h
            ),
            radius = base * 0.45f,
            center = Offset(-w * 0.10f, h * 1.02f)
        )
    }
}

// ── Stateless screen ─────────────────────────────────────────────

@Composable
private fun SignUpScreen(
    state: SignUpUiState,
    onSignInClick: () -> Unit,
    onRoleSelected: (SignUpRole) -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onVerifyEmail: () -> Unit,
    onPhoneChange: (String) -> Unit,
    onVerifyPhone: () -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onInstitutionTypeChange: (String) -> Unit,
    onUdiseChange: (String) -> Unit,
    onPickCertificate: () -> Unit,
    onTermsToggle: (Boolean) -> Unit,
    onSubmit: () -> Unit
) {
    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .background(Bg)
            .clipToBounds()
    ) {
        val screenHeight = maxHeight
        val screenWidth = maxWidth
        val isNarrow = screenWidth < 360.dp
        val isWide = screenWidth >= 500.dp
        val horizontalPad = if (isNarrow) 16.dp else 24.dp

        SignUpBackdrop()

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp)
                    .heightIn(min = screenHeight)
                    .verticalScroll(rememberScrollState())
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                // ── Top bar ─────────────────────────────────────────
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPad)
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Row {
                        Text("Playable ", color = TextPrimary, style = WordmarkStyle)
                        Text("Craft", color = AccentLink, style = WordmarkStyle)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Already have an account?", color = TextSecondary, fontSize = AppFontSize.BodySmall)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable(onClick = onSignInClick)
                        ) {
                            Text("Sign in", color = AccentLink, fontSize = AppFontSize.Body, fontWeight = FontWeight.Medium)
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward, null,
                                tint = AccentLink, modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // ── Headline ────────────────────────────────────────
                Spacer(Modifier.height(if (isNarrow) 24.dp else 32.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = TextPrimary)) { append("Create ") }
                        withStyle(SpanStyle(color = Lavender)) { append("your account") }
                    },
                    style = HeadlineStyle,
                    modifier = Modifier.padding(horizontal = horizontalPad)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Generate AI simulations and save your library.",
                    style = SubtitleStyle,
                    modifier = Modifier.padding(horizontal = horizontalPad)
                )

                // ── I am a ──────────────────────────────────────────
                Spacer(Modifier.height(24.dp))
                UpperLabel("I AM A", horizontalPad = horizontalPad)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPad)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SignUpRole.entries.forEach { role ->
                        RoleChip(
                            label = role.label,
                            selected = role == state.role,
                            onClick = { onRoleSelected(role) }
                        )
                    }
                }

                // ── Name ────────────────────────────────────────────
                Spacer(Modifier.height(20.dp))
                UpperLabel(
                    if (state.isSchool) "SCHOOL / INSTITUTION NAME" else "FULL NAME",
                    horizontalPad = horizontalPad
                )
                Spacer(Modifier.height(8.dp))
                AuthField(
                    value = state.name,
                    onValueChange = onNameChange,
                    placeholder = if (state.isSchool) "Greenfield International School" else "Your full name",
                    icon = if (state.isSchool) Icons.Outlined.Apartment else Icons.Outlined.Person,
                    modifier = Modifier.padding(horizontal = horizontalPad)
                )

                // ── Email + verify ──────────────────────────────────
                Spacer(Modifier.height(16.dp))
                UpperLabel("EMAIL", horizontalPad = horizontalPad)
                Spacer(Modifier.height(8.dp))
                AuthField(
                    value = state.email,
                    onValueChange = onEmailChange,
                    placeholder = if (state.isSchool) "you@school.edu" else "you@example.com",
                    icon = Icons.Outlined.Email,
                    keyboardType = KeyboardType.Email,
                    modifier = Modifier.padding(horizontal = horizontalPad)
                )
                Spacer(Modifier.height(8.dp))
                VerifyRow(
                    buttonLabel = "Verify email",
                    hint = "Enter your email first",
                    verified = state.emailVerified,
                    loading = state.emailVerifying,
                    enabled = state.isEmailValid,
                    horizontalPad = horizontalPad,
                    onClick = onVerifyEmail
                )

                // ── Phone + password (adaptive: side-by-side on wide, stacked on phone) ──
                Spacer(Modifier.height(20.dp))
                if (isWide) {
                    Row(
                        Modifier.padding(horizontal = horizontalPad),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(Modifier.weight(1f)) {
                            UpperLabel("PHONE NUMBER", padded = false)
                            Spacer(Modifier.height(8.dp))
                            AuthField(
                                value = state.phone,
                                onValueChange = onPhoneChange,
                                placeholder = "9876543210",
                                icon = Icons.Outlined.Phone,
                                keyboardType = KeyboardType.Phone
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            UpperLabel("PASSWORD", padded = false)
                            Spacer(Modifier.height(8.dp))
                            AuthField(
                                value = state.password,
                                onValueChange = onPasswordChange,
                                placeholder = "8+ characters",
                                icon = Icons.Outlined.Lock,
                                keyboardType = KeyboardType.Password,
                                imeAction = if (state.isSchool) ImeAction.Next else ImeAction.Done,
                                visualTransformation = if (state.passwordVisible)
                                    VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = onTogglePasswordVisibility) {
                                        Icon(
                                            if (state.passwordVisible) Icons.Outlined.Visibility
                                            else Icons.Outlined.VisibilityOff,
                                            contentDescription = if (state.passwordVisible)
                                                "Hide password" else "Show password",
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    VerifyRow(
                        buttonLabel = "Verify phone number",
                        hint = "Enter 10 digits first",
                        verified = state.phoneVerified,
                        loading = state.phoneVerifying,
                        enabled = state.isPhoneValid,
                        horizontalPad = horizontalPad,
                        onClick = onVerifyPhone
                    )
                } else {
                    UpperLabel("PHONE NUMBER", horizontalPad = horizontalPad)
                    Spacer(Modifier.height(8.dp))
                    AuthField(
                        value = state.phone,
                        onValueChange = onPhoneChange,
                        placeholder = "9876543210",
                        icon = Icons.Outlined.Phone,
                        keyboardType = KeyboardType.Phone,
                        modifier = Modifier.padding(horizontal = horizontalPad)
                    )
                    Spacer(Modifier.height(8.dp))
                    VerifyRow(
                        buttonLabel = "Verify phone number",
                        hint = "Enter 10 digits first",
                        verified = state.phoneVerified,
                        loading = state.phoneVerifying,
                        enabled = state.isPhoneValid,
                        horizontalPad = horizontalPad,
                        onClick = onVerifyPhone
                    )
                    Spacer(Modifier.height(16.dp))
                    UpperLabel("PASSWORD", horizontalPad = horizontalPad)
                    Spacer(Modifier.height(8.dp))
                    AuthField(
                        value = state.password,
                        onValueChange = onPasswordChange,
                        placeholder = "8+ characters",
                        icon = Icons.Outlined.Lock,
                        keyboardType = KeyboardType.Password,
                        imeAction = if (state.isSchool) ImeAction.Next else ImeAction.Done,
                        visualTransformation = if (state.passwordVisible)
                            VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = onTogglePasswordVisibility) {
                                Icon(
                                    if (state.passwordVisible) Icons.Outlined.Visibility
                                    else Icons.Outlined.VisibilityOff,
                                    contentDescription = if (state.passwordVisible)
                                        "Hide password" else "Show password",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        modifier = Modifier.padding(horizontal = horizontalPad)
                    )
                }

                // ── School-only fields ──────────────────────────────
                if (state.isSchool) {
                    Spacer(Modifier.height(20.dp))
                    if (isWide) {
                        Row(
                            Modifier.padding(horizontal = horizontalPad),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(Modifier.weight(1f)) {
                                PlainLabel("Institution type", padded = false)
                                Spacer(Modifier.height(8.dp))
                                InstitutionTypeDropdown(
                                    selected = state.institutionType,
                                    onSelected = onInstitutionTypeChange
                                )
                            }
                            Column(Modifier.weight(1f)) {
                                UpperLabel("UDISE CODE", padded = false)
                                Spacer(Modifier.height(8.dp))
                                AuthField(
                                    value = state.udiseCode,
                                    onValueChange = onUdiseChange,
                                    placeholder = "e.g. 18230100101",
                                    icon = null,
                                    keyboardType = KeyboardType.Number
                                )
                            }
                        }
                    } else {
                        PlainLabel("Institution type", horizontalPad = horizontalPad)
                        Spacer(Modifier.height(8.dp))
                        Box(Modifier.padding(horizontal = horizontalPad)) {
                            InstitutionTypeDropdown(
                                selected = state.institutionType,
                                onSelected = onInstitutionTypeChange
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        UpperLabel("UDISE CODE", horizontalPad = horizontalPad)
                        Spacer(Modifier.height(8.dp))
                        AuthField(
                            value = state.udiseCode,
                            onValueChange = onUdiseChange,
                            placeholder = "e.g. 18230100101",
                            icon = null,
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.padding(horizontal = horizontalPad)
                        )
                    }

                    Spacer(Modifier.height(20.dp))
                    PlainLabel("Affiliation / recognition certificate (or NOC)", horizontalPad = horizontalPad)
                    Spacer(Modifier.height(8.dp))
                    UploadField(
                        fileName = state.certificateName,
                        horizontalPad = horizontalPad,
                        onClick = onPickCertificate
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Used only for institution verification by our team.",
                        style = CaptionStyle,
                        modifier = Modifier.padding(horizontal = horizontalPad)
                    )
                }

                // ── Terms ───────────────────────────────────────────
                Spacer(Modifier.height(20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = horizontalPad)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onTermsToggle(!state.acceptedTerms) }
                ) {
                    Checkbox(
                        checked = state.acceptedTerms,
                        onCheckedChange = onTermsToggle,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Accent,
                            uncheckedColor = FieldStroke,
                            checkmarkColor = TextPrimary
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = TextPrimary)) { append("I agree to the ") }
                            withStyle(SpanStyle(color = AccentLink, fontWeight = FontWeight.Medium)) {
                                append("Terms and Privacy Policy.")
                            }
                        },
                        fontSize = AppFontSize.Body
                    )
                }

                // ── Error banner ────────────────────────────────────
                if (state.errorMessage != null) {
                    Spacer(Modifier.height(16.dp))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = horizontalPad)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ErrorFill)
                            .border(1.dp, ErrorBorder, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            state.errorMessage,
                            color = ErrorText,
                            fontSize = AppFontSize.Caption,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // ── Primary CTA ─────────────────────────────────────
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = onSubmit,
                    enabled = !state.isLoading,
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
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            color = TextPrimary, strokeWidth = 2.dp, modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text("Create account", style = ButtonTextStyle, fontSize = AppFontSize.Button)
                        Spacer(Modifier.width(10.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, null, modifier = Modifier.size(20.dp))
                    }
                }

                // ── Footer ──────────────────────────────────────────
                Spacer(Modifier.height(24.dp))
                Spacer(Modifier.weight(1f))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPad)
                        .height(1.dp)
                        .background(FieldStroke)
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPad)
                        .padding(top = 16.dp, bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(if (isNarrow) 12.dp else 20.dp)) {
                        Text("Privacy", style = FooterTextStyle)
                        Text("Terms", style = FooterTextStyle)
                        Text("Help", style = FooterTextStyle)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Status", style = FooterTextStyle)
                        Box(Modifier.size(8.dp).clip(CircleShape).background(StatusGreen))
                        Text("ok", color = TextPrimary, fontSize = AppFontSize.Body)
                    }
                }
            }
        }
    }
}

// Reusable Components (file-private)

@Composable
private fun UpperLabel(text: String, horizontalPad: Dp = 24.dp, padded: Boolean = true) {
    Text(
        text = text,
        style = SectionLabelStyle,
        modifier = if (padded) Modifier.padding(horizontal = horizontalPad) else Modifier
    )
}

@Composable
private fun PlainLabel(text: String, horizontalPad: Dp = 24.dp, padded: Boolean = true) {
    Text(
        text = text,
        style = PlainLabelStyle,
        modifier = if (padded) Modifier.padding(horizontal = horizontalPad) else Modifier
    )
}

@Composable
private fun fieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = FieldFill,
    unfocusedContainerColor = FieldFill,
    disabledContainerColor = FieldFill,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    cursorColor = Accent,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedPlaceholderColor = TextPlaceholder,
    unfocusedPlaceholderColor = TextPlaceholder,
    focusedLeadingIconColor = TextPlaceholder,
    unfocusedLeadingIconColor = TextPlaceholder,
    focusedTrailingIconColor = TextLabel,
    unfocusedTrailingIconColor = TextLabel
)

@Composable
private fun AuthField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector?,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, style = PlaceholderTextStyle, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        singleLine = true,
        colors = fieldColors(),
        shape = FieldShape,
        textStyle = InputTextStyle,
        leadingIcon = icon?.let { { Icon(it, null, modifier = Modifier.size(20.dp)) } },
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            onDone = { focusManager.clearFocus() }
        ),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clip(FieldShape)
            .border(1.dp, FieldStroke, FieldShape)
    )
}

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
                fontSize = AppFontSize.Body,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun VerifyRow(
    buttonLabel: String,
    hint: String,
    verified: Boolean,
    loading: Boolean,
    enabled: Boolean,
    horizontalPad: Dp = 24.dp,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPad),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled && !verified && !loading,
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, FieldStroke),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = TextPrimary,
                disabledContentColor = if (verified) StatusGreen else TextSecondary
            ),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 0.dp),
            modifier = Modifier.height(40.dp)
        ) {
            if (loading) {
                CircularProgressIndicator(
                    color = TextPrimary, strokeWidth = 2.dp, modifier = Modifier.size(16.dp)
                )
            } else {
                Text(if (verified) "Verified ✓" else buttonLabel, fontSize = AppFontSize.Caption)
            }
        }
        Spacer(Modifier.width(12.dp))
        if (!verified && !enabled) {
            Text(
                hint,
                style = CaptionStyle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
        }
    }
}

@Composable
private fun InstitutionTypeDropdown(
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .clip(FieldShape)
                .background(FieldFill)
                .border(1.dp, FieldStroke, FieldShape)
                .clickable { expanded = true }
                .padding(start = 16.dp, end = 8.dp)
        ) {
            Text(
                selected,
                style = InputTextStyle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Filled.KeyboardArrowDown, "Choose institution type", tint = TextPrimary)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = FieldFill
        ) {
            institutionTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type, style = InputTextStyle) },
                    onClick = {
                        onSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun UploadField(fileName: String?, horizontalPad: Dp = 24.dp, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = horizontalPad)
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clip(FieldShape)
            .background(FieldFill)
            .border(1.dp, FieldStroke, FieldShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp)
    ) {
        Icon(Icons.Outlined.Description, null, tint = TextPlaceholder, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Text(
            text = fileName ?: "Upload certificate (JPG, PNG or PDF)",
            color = if (fileName != null) TextPrimary else TextPlaceholder,
            fontSize = AppFontSize.Input,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Icon(Icons.Outlined.FileUpload, "Upload", tint = TextLabel, modifier = Modifier.size(22.dp))
    }
}
