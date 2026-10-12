package com.example.myapplication.ui.theme

import androidx.compose.ui.graphics.Color

// ── Background ───────────────────────────────────────────────────
val Bg              = Color(0xFF0A1122)

// ── Fields / Controls ────────────────────────────────────────────
val FieldFill       = Color(0xFF0E1730)   // ≈ white @ 3–4 %
val FieldStroke     = Color(0xFF2A3552)   // 1 px borders everywhere

// ── Text hierarchy ───────────────────────────────────────────────
val TextPrimary     = Color(0xFFFFFFFF)
val TextSecondary   = Color(0xFF98A2BC)   // subtitles, "Craft" in wordmark
val TextLabel       = Color(0xFF8C96B0)   // uppercase labels, footer, "OR"
val TextPlaceholder = Color(0xFF5E6985)   // placeholders, leading icons

// ── Accent ───────────────────────────────────────────────────────
val Accent          = Color(0xFF4466FF)   // primary button, checkbox
val AccentLink      = Color(0xFF5E82FF)   // "Create account →", "Forgot password?"

// ── Chip selected state ──────────────────────────────────────────
val ChipSelectedBg   = Color(0xFFFFFFFF)
val ChipSelectedText = Color(0xFF0A1122)   // same as Bg

// ── Status ───────────────────────────────────────────────────────
val StatusGreen     = Color(0xFF1FD17B)

// ── Error (suggested states from spec) ───────────────────────────
val ErrorBorder     = Color(0xFFFF5C6C)
val ErrorText       = Color(0xFFFF7B7B)
val ErrorFill       = Color(0x33FF5A5A)

// ── Backdrop disc ────────────────────────────────────────────────
val DiscTop         = Color(0xFF1F3C94)   // brightest (top-right)
val DiscMid         = Color(0xFF162A6B)   // mid-fade