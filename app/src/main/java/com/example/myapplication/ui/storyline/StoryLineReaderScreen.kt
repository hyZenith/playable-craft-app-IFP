package com.example.myapplication.ui.storyline

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.StoryLineSummary
import com.example.myapplication.data.repo.StoryLineRepository

private val PageBackground = Color(0xFF070B16)
private val CardBackground = Color(0xFF0D162C)
private val CardBorder = Color(0xFF1C2C4E)
private val TextLavender = Color(0xFF8F9BD6)
private val AccentPurple = Color(0xFF635BFF)
private val NotesPillBg = Color(0xFF101A34)
private val NotesPillBorder = Color(0xFF223154)

@Composable
fun StoryLineReaderRoute(
    storyId: String,
    repository: StoryLineRepository,
    onBackClick: () -> Unit,
    onNotesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var story by remember { mutableStateOf<StoryLineSummary?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(storyId) {
        isLoading = true
        val result = repository.getById(storyId)
        story = result.getOrNull()
        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(PageBackground),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = AccentPurple)
        }
        return
    }

    val item = story ?: StoryLineSummary(
        id = storyId,
        title = "Ohm's Law",
        subject = "Physics",
        board = "SEBA",
        grade = "Class 11",
        actCount = 4
    )

    StoryLineReaderScreen(
        story = item,
        onBackClick = onBackClick,
        onNotesClick = onNotesClick,
        modifier = modifier
    )
}

@Composable
fun StoryLineReaderScreen(
    story: StoryLineSummary,
    onBackClick: () -> Unit,
    onNotesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentActIndex by remember { mutableIntStateOf(0) }
    val totalActs = story.actCount.coerceAtLeast(3)

    val sampleActs = remember(story.title) {
        listOf(
            ActContent(
                title = "Act 1: The Sparks in Georg's Workshop",
                setting = "Munich, 1826 — A chilly laboratory filled with copper wires and magnetic needles.",
                narrative = "Georg Simon Ohm observed a curious anomaly: when different metal wires connected his electrochemical circuits, the compass needles deflected at wildly varying angles. Why did certain wires hinder the invisible fluid while others let it surge freely?",
                dialogue = "\"There is an inherent friction here,\" Georg whispered. \"Every substance offers its own resistance to the potential drive.\"",
                keyConcept = "Voltage acts as the electrical pressure difference driving current through conductors."
            ),
            ActContent(
                title = "Act 2: The Direct Ratio",
                setting = "The High School Laboratory in Cologne.",
                narrative = "Carefully varying the temperature difference across thermocouples, Georg discovered that doubling the electric tension directly doubled the stream of electrons flowing through the circuit. The relationship was strictly proportional.",
                dialogue = "\"Current is directly proportional to potential difference across two endpoints.\"",
                keyConcept = "V ∝ I — the foundation of linear resistive electrical systems."
            ),
            ActContent(
                title = "Act 3: Introducing Resistance",
                setting = "Publishing the Mathematical Theory.",
                narrative = "By standardizing copper conductor dimensions, the constant of proportionality was established as Resistance (R). Ohm recognized that geometry matters: thicker wires lower resistance, whereas longer wires amplify it.",
                dialogue = "\"V = I × R holds steadfast regardless of circuit complexity, provided temperature remains constant.\"",
                keyConcept = "Ohmic vs Non-Ohmic conductors and the significance of temperature invariance."
            ),
            ActContent(
                title = "Act 4: The Legacy in Modern Circuits",
                setting = "A Contemporary High-Tech Computing Lab.",
                narrative = "From simple light bulbs to microscopic transistor pathways in microprocessors, Georg Ohm's fundamental discovery governs how electrical power is routed, dissipated, and measured worldwide.",
                dialogue = "\"Mastering the story of Ohm's Law equips students to design the sustainable energy grids of tomorrow.\"",
                keyConcept = "Power dissipation P = V × I = I²R and energy conservation in electrical networks."
            )
        )
    }

    val currentAct = sampleActs.getOrElse(currentActIndex) { sampleActs.first() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PageBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = onBackClick, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = story.title,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = story.breadcrumb,
                            color = TextLavender,
                            fontSize = 11.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CardBackground,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.MenuBook,
                            contentDescription = null,
                            tint = AccentPurple,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Act ${currentActIndex + 1} of $totalActs",
                            color = AccentPurple,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Act Progress Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (i in 0 until totalActs) {
                    val isSelected = i == currentActIndex
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(if (isSelected) AccentPurple else Color(0xFF1E2D4E))
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main Narrative Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardBackground)
                    .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
                    .padding(22.dp)
            ) {
                Text(
                    text = currentAct.title,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = currentAct.setting,
                    color = TextLavender.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = currentAct.narrative,
                    color = Color(0xFFE2E8F0),
                    fontSize = 13.5.sp,
                    lineHeight = 21.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dialogue Quote Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF091024))
                        .border(1.dp, Color(0xFF1F2F52), RoundedCornerShape(8.dp))
                        .padding(14.dp)
                ) {
                    Text(
                        text = currentAct.dialogue,
                        color = TextLavender,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 19.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Scientific Key Concept Pill Box
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF111E3E))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AutoAwesome,
                        contentDescription = null,
                        tint = AccentPurple,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = currentAct.keyConcept,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Act Navigation Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentActIndex > 0) {
                        OutlinedButton(
                            onClick = { currentActIndex-- },
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                tint = TextLavender,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Previous Act", color = TextLavender, fontSize = 12.5.sp)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    if (currentActIndex < totalActs - 1) {
                        Button(
                            onClick = { currentActIndex++ },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
                        ) {
                            Text(text = "Next Act", color = Color.White, fontSize = 12.5.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    } else {
                        Button(
                            onClick = onBackClick,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                        ) {
                            Text(text = "Complete StoryLine", color = Color.White, fontSize = 12.5.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(90.dp))
        }

        // Pinned Notes Action
        com.example.myapplication.ui.common.NotesFloatingButton(
            onClick = onNotesClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(24.dp)
        )
    }
}

private data class ActContent(
    val title: String,
    val setting: String,
    val narrative: String,
    val dialogue: String,
    val keyConcept: String
)
