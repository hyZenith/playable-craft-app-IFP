package com.example.myapplication.ui.teachercontext

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.PromptRule
import com.example.myapplication.data.model.TeacherContextTab
import com.example.myapplication.data.model.VideoBadgeType
import com.example.myapplication.data.model.VideoWalkthrough
import com.example.myapplication.ui.theme.AccentLink
import com.example.myapplication.ui.theme.FieldStroke
import com.example.myapplication.ui.theme.TextPrimary
import com.example.myapplication.ui.theme.TextSecondary

private val Lavender = Color(0xFF8F9BD6)
private val SoftField = Color(0xFF0B1530)
private val NoticeBannerBg = Color(0xFF0B152C)
private val NoticeBannerStroke = Color(0xFF1C2B4E)
private val ThumbnailBg = Color(0xFF060B18)
private val LockIconColor = Color(0xFF5D6C91)
private val TimestampBg = Color(0xCC081022)
private val VibrantButtonBlue = Color(0xFF3875FF)
private val TabBarBg = Color(0xFF0A1326)
private val TabBarBorder = Color(0xFF1E2B4B)
private val StatusPillBg = Color(0xFF101B34)
private val StatusPillStroke = Color(0xFF223050)

/**
 * Stateful entry point route for the Teacher Context guide page.
 *
 * @param viewModel Controller managing instructional walkthrough data and prompt rules.
 * @param onNotesClick Triggered when the user taps the floating notes button.
 */
@Composable
fun TeacherContextRoute(
    viewModel: TeacherContextViewModel,
    onNotesClick: () -> Unit
) {
    val state = viewModel.uiState

    TeacherContextScreen(
        state = state,
        onTabSelected = viewModel::onTabSelected,
        onToggleRuleExpanded = viewModel::toggleRuleExpanded,
        onRetry = viewModel::retry,
        onNotesClick = onNotesClick
    )
}

/**
 * Stateless screen displaying the Teacher Context header, tab switcher,
 * info notice, video walkthrough catalog, and prompt guidelines with expandable rule cards.
 */
@Composable
fun TeacherContextScreen(
    state: TeacherContextUiState,
    onTabSelected: (TeacherContextTab) -> Unit,
    onToggleRuleExpanded: (ruleId: String) -> Unit,
    onRetry: () -> Unit,
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
            Spacer(Modifier.height(14.dp))

            Text(
                text = "Teacher Context",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "How to get good output from the generators, and what each subject can realistically be simulated as.",
                color = Lavender,
                fontSize = 14.5.sp,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(18.dp))

            TeacherContextTabsRow(
                selectedTab = state.selectedTab,
                onTabSelected = onTabSelected
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

                    state.selectedTab == TeacherContextTab.VIDEOS -> {
                        VideosContent(videos = state.videos)
                    }

                    state.selectedTab == TeacherContextTab.PROMPT_ENGINEERING -> {
                        PromptEngineeringContent(
                            rules = state.promptRules,
                            expandedRuleIds = state.expandedRuleIds,
                            onToggleRuleExpanded = onToggleRuleExpanded
                        )
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
 * Segmented switcher between "Videos" and "Prompt Engineering" tabs matching the design screenshots.
 */
@Composable
private fun TeacherContextTabsRow(
    selectedTab: TeacherContextTab,
    onTabSelected: (TeacherContextTab) -> Unit
) {
    val containerShape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(containerShape)
            .background(TabBarBg)
            .border(1.dp, TabBarBorder, containerShape)
            .padding(4.dp)
    ) {
        val isVideosSelected = selectedTab == TeacherContextTab.VIDEOS
        val isPromptsSelected = selectedTab == TeacherContextTab.PROMPT_ENGINEERING

        // Videos tab
        val videoTabShape = RoundedCornerShape(8.dp)
        val videoBg = if (isVideosSelected) Color(0xFF0F1B38) else Color.Transparent
        val videoBorder = if (isVideosSelected) Color(0xFF2A4A87) else Color.Transparent
        val videoContentColor = if (isVideosSelected) TextPrimary else Lavender

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clip(videoTabShape)
                .background(videoBg)
                .border(1.dp, videoBorder, videoTabShape)
                .clickable { onTabSelected(TeacherContextTab.VIDEOS) }
                .padding(horizontal = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Videocam,
                contentDescription = null,
                tint = videoContentColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Videos",
                color = videoContentColor,
                fontSize = 13.5.sp,
                fontWeight = if (isVideosSelected) FontWeight.SemiBold else FontWeight.Medium
            )
        }

        // Prompt Engineering tab
        val promptTabShape = RoundedCornerShape(8.dp)
        val promptBg = if (isPromptsSelected) VibrantButtonBlue else Color.Transparent
        val promptBorder = if (isPromptsSelected) VibrantButtonBlue else Color.Transparent
        val promptContentColor = if (isPromptsSelected) Color.White else Lavender

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .clip(promptTabShape)
                .background(promptBg)
                .border(1.dp, promptBorder, promptTabShape)
                .clickable { onTabSelected(TeacherContextTab.PROMPT_ENGINEERING) }
                .padding(horizontal = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Lightbulb,
                contentDescription = null,
                tint = promptContentColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Prompt Engineering",
                color = promptContentColor,
                fontSize = 13.5.sp,
                fontWeight = if (isPromptsSelected) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}

/**
 * List of walkthrough recordings preceded by the status information notice banner.
 */
@Composable
private fun VideosContent(
    videos: List<VideoWalkthrough>
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            WalkthroughNoticeBanner()
        }

        items(
            items = videos,
            key = { it.id }
        ) { video ->
            VideoCard(video = video)
        }
    }
}

/**
 * Information notice describing upcoming recording availability.
 */
@Composable
private fun WalkthroughNoticeBanner() {
    val shape = RoundedCornerShape(12.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(NoticeBannerBg)
            .border(1.dp, NoticeBannerStroke, shape)
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = AccentLink,
            modifier = Modifier
                .size(20.dp)
                .padding(top = 1.dp)
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = "Walkthroughs are being recorded. Titles below are the planned running order — the cards will become playable as each one lands.",
            color = Color(0xFF98A6D4),
            fontSize = 13.sp,
            lineHeight = 18.5.sp
        )
    }
}

/**
 * Individual video walkthrough card showing thumbnail, duration, badges, and status.
 */
@Composable
private fun VideoCard(
    video: VideoWalkthrough
) {
    val cardShape = RoundedCornerShape(16.dp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SoftField)
            .border(1.dp, FieldStroke, cardShape)
            .padding(14.dp)
    ) {
        Box(
            modifier = Modifier
                .width(124.dp)
                .height(86.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(ThumbnailBg)
                .border(1.dp, Color(0xFF1B2642), RoundedCornerShape(12.dp))
        ) {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                tint = LockIconColor,
                modifier = Modifier
                    .size(22.dp)
                    .align(Alignment.Center)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(TimestampBg)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = video.duration,
                    color = Color(0xFFCAD5F5),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(Modifier.width(14.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                BadgePill(badge = video.badge)

                Spacer(Modifier.width(8.dp))

                Text(
                    text = video.featureCategory,
                    color = Lavender,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(4.dp))

            Text(
                text = video.title,
                color = TextPrimary,
                fontSize = 14.5.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = video.description,
                color = Lavender,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(StatusPillBg)
                    .border(1.dp, StatusPillStroke, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.PlayArrow,
                    contentDescription = null,
                    tint = Lavender,
                    modifier = Modifier.size(14.dp)
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    text = if (video.isPlayable) "Watch now" else "Coming soon",
                    color = Lavender,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Small indicator pill for categorizing walkthrough priority.
 */
@Composable
private fun BadgePill(
    badge: VideoBadgeType
) {
    val isStartHere = badge == VideoBadgeType.START_HERE
    val containerBg = if (isStartHere) Color(0xFF0F1E42) else Color(0xFF181B38)
    val strokeColor = if (isStartHere) Color(0xFF2D55B8) else Color(0xFF3B4175)
    val textColor = if (isStartHere) Color(0xFF668EFF) else Color(0xFF8C96D8)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(containerBg)
            .border(1.dp, strokeColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = badge.label,
            color = textColor,
            fontSize = 9.5.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

/**
 * Content view rendering prompt engineering guidance and interactive comparison rules.
 */
@Composable
private fun PromptEngineeringContent(
    rules: List<PromptRule>,
    expandedRuleIds: Set<String>,
    onToggleRuleExpanded: (ruleId: String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            PromptHeaderSection()
        }

        items(
            items = rules,
            key = { it.id }
        ) { rule ->
            PromptRuleCard(
                rule = rule,
                isExpanded = expandedRuleIds.contains(rule.id),
                onToggle = { onToggleRuleExpanded(rule.id) }
            )
        }
    }
}

/**
 * Top instructional heading explaining the purpose of the 6 prompt rules.
 */
@Composable
private fun PromptHeaderSection() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.AutoAwesome,
                contentDescription = null,
                tint = Color(0xFF6F92F6),
                modifier = Modifier.size(20.dp)
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = "How to prompt",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Every generator takes a short free-text line — the Focus field on PlayableCraft, the Topic on a lesson plan or StoryLine. These six rules are what separate a usable result from a retry.",
            color = Color(0xFF98A6D4),
            fontSize = 13.5.sp,
            lineHeight = 19.sp
        )
    }
}

/**
 * Expandable card demonstrating a prompt rule with weak vs better comparisons and rationale.
 */
@Composable
private fun PromptRuleCard(
    rule: PromptRule,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val cardShape = RoundedCornerShape(14.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(SoftField)
            .border(1.dp, FieldStroke, cardShape)
            .padding(14.dp)
    ) {
        // Clickable header row
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
        ) {
            // Sequence Number Badge (e.g., "01")
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF121D38))
                    .border(1.dp, Color(0xFF233256), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = rule.index,
                    color = Color(0xFFCCD7F6),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = rule.title,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    text = rule.subtitle,
                    color = Lavender,
                    fontSize = 12.5.sp,
                    lineHeight = 17.sp
                )
            }

            Spacer(Modifier.width(8.dp))

            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = Color(0xFF6B7EAB),
                modifier = Modifier.size(22.dp)
            )
        }

        // Expandable comparison content
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp)
            ) {
                // WEAK Prompt Box
                val weakBoxShape = RoundedCornerShape(10.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(weakBoxShape)
                        .background(Color(0xFF070E1E))
                        .border(1.dp, Color(0xFF1C2745), weakBoxShape)
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color(0xFF8290B5),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "WEAK",
                            color = Color(0xFF8290B5),
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = rule.weakText,
                        color = Color(0xFFD6DFFA),
                        fontSize = 13.5.sp,
                        lineHeight = 18.sp
                    )
                }

                Spacer(Modifier.height(10.dp))

                // BETTER Prompt Box
                val betterBoxShape = RoundedCornerShape(10.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(betterBoxShape)
                        .background(Color(0xFF09142C))
                        .border(1.dp, Color(0xFF2A4885), betterBoxShape)
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF4C82FF),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "BETTER",
                            color = Color(0xFF4C82FF),
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = rule.betterText,
                        color = Color(0xFFE2E9FD),
                        fontSize = 13.5.sp,
                        lineHeight = 18.sp
                    )
                }

                Spacer(Modifier.height(10.dp))

                // Why Explanation
                Text(
                    text = rule.whyExplanation,
                    color = Color(0xFF8290B5),
                    fontSize = 12.5.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

/**
 * Bottom-pinned rounded pill button for opening revision notes matching reference designs.
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
            text = "Unable to load Teacher Context",
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
