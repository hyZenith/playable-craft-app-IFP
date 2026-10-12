package com.example.myapplication.ui.ai

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.model.SimulationSummary
import com.example.myapplication.data.model.SimulationVisualType
import com.example.myapplication.data.repo.SimulationRepository
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val PageBackground = Color(0xFF070B16)
private val CardBackground = Color(0xFF0F1830)
private val CardBorder = Color(0xFF22355E)
private val AccentPurple = Color(0xFF635BFF)
private val TextLavender = Color(0xFF8F9BD6)
private val CanvasBg = Color(0xFF080E20)

@Composable
fun SimulationViewerRoute(
    simId: String,
    repository: SimulationRepository,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var simulation by remember { mutableStateOf<SimulationSummary?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(simId) {
        isLoading = true
        val result = repository.getById(simId)
        simulation = result.getOrNull()
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

    val item = simulation ?: SimulationSummary(
        id = simId,
        title = "Interactive Simulation",
        board = "SEBA",
        chapter = "Simulation Lab",
        subject = "Science",
        formattedDate = "Oct 12, 2026",
        visualType = SimulationVisualType.PHYSICS_ORBIT
    )

    SimulationViewerScreen(
        simulation = item,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
fun SimulationViewerScreen(
    simulation: SimulationSummary,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(true) }
    var param1 by remember { mutableFloatStateOf(45f) } // Angle or Temp
    var param2 by remember { mutableFloatStateOf(60f) } // Velocity or Concentration

    val infiniteTransition = rememberInfiniteTransition(label = "sim_anim")
    val animProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "anim"
    )

    val currentProgress = if (isPlaying) animProgress else 0.5f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PageBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Column {
                    Text(
                        text = simulation.title,
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${simulation.subject} • ${simulation.chapter}",
                        color = TextLavender,
                        fontSize = 12.sp
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = CardBackground,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Text(
                    text = simulation.board,
                    color = AccentPurple,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Interactive Simulation Canvas Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(CanvasBg)
                    .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
            ) {
                when (simulation.visualType) {
                    SimulationVisualType.MOLECULE -> {
                        MoleculeCanvas(progress = currentProgress, temp = param1)
                    }
                    SimulationVisualType.WAVE_OPTICS -> {
                        WaveOpticsCanvas(progress = currentProgress, angle = param1)
                    }
                    else -> {
                        PhysicsMotionCanvas(progress = currentProgress, angle = param1, velocity = param2)
                    }
                }

                // Overlay Controls: Play/Pause and Reset
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = CardBackground.copy(alpha = 0.85f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.clickable { isPlaying = !isPlaying }
                    ) {
                        Box(modifier = Modifier.padding(8.dp)) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = CardBackground.copy(alpha = 0.85f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                        modifier = Modifier.clickable {
                            param1 = 45f
                            param2 = 60f
                        }
                    ) {
                        Box(modifier = Modifier.padding(8.dp)) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Parameter Sliders Panel
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardBackground)
                    .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Simulation Parameters",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.AutoAwesome,
                            contentDescription = null,
                            tint = AccentPurple,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Live Interactive",
                            color = AccentPurple,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Slider 1
                val label1 = when (simulation.visualType) {
                    SimulationVisualType.MOLECULE -> "Reaction Temperature: ${param1.toInt()}°C"
                    SimulationVisualType.WAVE_OPTICS -> "Incident Angle: ${param1.toInt()}°"
                    else -> "Launch Angle: ${param1.toInt()}°"
                }
                Text(text = label1, color = TextLavender, fontSize = 12.sp)
                Slider(
                    value = param1,
                    onValueChange = { param1 = it },
                    valueRange = 10f..85f,
                    colors = SliderDefaults.colors(
                        thumbColor = AccentPurple,
                        activeTrackColor = AccentPurple,
                        inactiveTrackColor = Color(0xFF1E2D4E)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Slider 2
                val label2 = when (simulation.visualType) {
                    SimulationVisualType.MOLECULE -> "Reagent Concentration: ${param2.toInt()}%"
                    SimulationVisualType.WAVE_OPTICS -> "Medium Refractive Index: ${(1f + param2 / 100f).let { "%.2f".format(it) }}"
                    else -> "Initial Velocity: ${param2.toInt()} m/s"
                }
                Text(text = label2, color = TextLavender, fontSize = 12.sp)
                Slider(
                    value = param2,
                    onValueChange = { param2 = it },
                    valueRange = 20f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = AccentPurple,
                        activeTrackColor = AccentPurple,
                        inactiveTrackColor = Color(0xFF1E2D4E)
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Explanation & Key Takeaways Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(CardBackground)
                    .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = null,
                        tint = TextLavender,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Underlying Scientific Principles",
                        color = Color.White,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "• Real-time calculations adhere strictly to the ingested curriculum standards for ${simulation.board}.\n" +
                           "• Modify angle, speed, and concentration variables to examine how physical observables respond instantaneously.\n" +
                           "• Grounded equations provide pedagogical clarity for classroom demonstration and self-directed student inquiry.",
                    color = TextLavender,
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PhysicsMotionCanvas(progress: Float, angle: Float, velocity: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Ground line
        val groundY = h * 0.82f
        drawLine(
            color = Color(0xFF233560),
            start = Offset(20f, groundY),
            end = Offset(w - 20f, groundY),
            strokeWidth = 2f
        )

        val originX = 60f
        val originY = groundY

        // Draw parabolic trajectory path
        val path = Path()
        path.moveTo(originX, originY)
        val rad = (angle * PI / 180f).toFloat()
        val vScale = velocity * 2.5f
        val g = 9.8f * 15f
        val totalTime = (2 * vScale * sin(rad)) / g

        var curX = originX
        var curY = originY
        val steps = 60
        for (i in 0..steps) {
            val t = (i.toFloat() / steps) * totalTime
            val x = originX + (vScale * cos(rad) * t)
            val y = originY - ((vScale * sin(rad) * t) - (0.5f * g * t * t))
            if (x < w - 20f && y <= groundY) {
                path.lineTo(x, y)
                if ((i.toFloat() / steps) <= progress) {
                    curX = x
                    curY = y
                }
            }
        }

        drawPath(
            path = path,
            color = Color(0xFF3875FF).copy(alpha = 0.5f),
            style = Stroke(width = 3f, cap = StrokeCap.Round)
        )

        // Draw current projectile particle
        drawCircle(
            color = Color(0xFF635BFF),
            radius = 12f,
            center = Offset(curX, curY)
        )
        drawCircle(
            color = Color.White,
            radius = 6f,
            center = Offset(curX, curY)
        )
    }
}

@Composable
private fun MoleculeCanvas(progress: Float, temp: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val jitter = (temp / 10f) * sin(progress * 2 * PI.toFloat())

        // Central nucleus
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF8B5CF6), Color(0xFF4C1D95)),
                center = Offset(cx, cy),
                radius = 36f
            ),
            radius = 30f,
            center = Offset(cx + jitter, cy - jitter)
        )

        // Orbital rings
        val orbitR1 = 70f
        val orbitR2 = 110f
        drawCircle(
            color = Color(0xFF22355E),
            radius = orbitR1,
            center = Offset(cx, cy),
            style = Stroke(width = 2f)
        )
        drawCircle(
            color = Color(0xFF22355E),
            radius = orbitR2,
            center = Offset(cx, cy),
            style = Stroke(width = 2f)
        )

        // Electrons
        val e1Angle = progress * 2 * PI.toFloat()
        val e1x = cx + orbitR1 * cos(e1Angle)
        val e1y = cy + orbitR1 * sin(e1Angle)
        drawCircle(color = Color(0xFF38BDF8), radius = 8f, center = Offset(e1x, e1y))

        val e2Angle = -progress * 2 * PI.toFloat() + 1.2f
        val e2x = cx + orbitR2 * cos(e2Angle)
        val e2y = cy + orbitR2 * sin(e2Angle)
        drawCircle(color = Color(0xFF38BDF8), radius = 8f, center = Offset(e2x, e2y))
    }
}

@Composable
private fun WaveOpticsCanvas(progress: Float, angle: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        // Prism triangle
        val prismPath = Path().apply {
            moveTo(cx, cy - 80f)
            lineTo(cx - 90f, cy + 80f)
            lineTo(cx + 90f, cy + 80f)
            close()
        }
        drawPath(path = prismPath, color = Color(0xFF1E2F54).copy(alpha = 0.5f))
        drawPath(path = prismPath, color = Color(0xFF3B82F6), style = Stroke(width = 2f))

        // Incident ray
        val inAngleRad = (angle * PI / 180f).toFloat()
        val inStartX = 40f
        val inStartY = cy + 40f - (angle * 0.8f)
        val hitX = cx - 40f
        val hitY = cy + 10f

        drawLine(
            color = Color.White,
            start = Offset(inStartX, inStartY),
            end = Offset(hitX, hitY),
            strokeWidth = 3f,
            cap = StrokeCap.Round
        )

        // Refracted rays through & exiting
        val outX = cx + 50f
        val outY = cy + 20f
        drawLine(
            color = Color(0xFFA5B4FC),
            start = Offset(hitX, hitY),
            end = Offset(outX, outY),
            strokeWidth = 3f
        )

        // Dispersed color rays exiting prism
        val colors = listOf(Color(0xFFEF4444), Color(0xFFF59E0B), Color(0xFF10B981), Color(0xFF3B82F6), Color(0xFF8B5CF6))
        colors.forEachIndexed { idx, col ->
            val spreadY = (outY - 20f) + (idx * 16f)
            drawLine(
                color = col,
                start = Offset(outX, outY),
                end = Offset(w - 30f, spreadY),
                strokeWidth = 2.5f,
                cap = StrokeCap.Round
            )
        }
    }
}
