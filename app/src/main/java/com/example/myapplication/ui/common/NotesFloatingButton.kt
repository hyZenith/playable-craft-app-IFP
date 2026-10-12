package com.example.myapplication.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Grey surface styling matching the notes page design tokens
private val NotesGreyBg = Color(0xFF222B3B)
private val NotesGreyBorder = Color(0xFF38465C)
private val NotesIconGrey = Color(0xFFA6B4C9)

/**
 * Floating button for accessing notes across the application.
 * Rendered in slate-grey with the notes logo and no text.
 */
@Composable
fun NotesFloatingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonShape = CircleShape
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(buttonShape)
            .background(NotesGreyBg)
            .border(1.dp, NotesGreyBorder, buttonShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Description,
            contentDescription = "Notes",
            tint = NotesIconGrey,
            modifier = Modifier.size(20.dp)
        )
    }
}
