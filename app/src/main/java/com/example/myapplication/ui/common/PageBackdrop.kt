package com.example.myapplication.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp

/** Decorative circles behind the app pages: a faint ring on the right, a filled disc bottom-left. */
@Composable
fun PageBackdrop(modifier: Modifier = Modifier) {
    Canvas(modifier.fillMaxSize().clearAndSetSemantics {}) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = 1.dp.toPx())

        // Right ring
        val c1 = Offset(w * 1.38f, h * 0.46f)
        val r1 = w * 0.55f
        drawCircle(Color(0xFF1B2E6B).copy(alpha = 0.10f), radius = r1, center = c1)
        drawCircle(Color(0xFF2A44A0).copy(alpha = 0.35f), radius = r1, center = c1, style = stroke)

        // Bottom-left disc
        val c2 = Offset(-w * 0.12f, h * 0.98f)
        val r2 = w * 0.55f
        drawCircle(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0E1D52).copy(alpha = 0.8f),
                    Color(0xFF1A3AA0).copy(alpha = 0.7f)
                ),
                startY = c2.y - r2,
                endY = c2.y + r2
            ),
            radius = r2,
            center = c2
        )
        drawCircle(Color(0xFF2E4FC4).copy(alpha = 0.55f), radius = r2, center = c2, style = stroke)
    }
}
