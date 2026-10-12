package com.example.myapplication.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Global font size tokens (standardized across the entire app)

object AppFontSize {
    val SectionLabel = 11.sp   // uppercase section labels, tags, field headers
    val Caption      = 13.sp   // helper text, hints, error banners, small secondary links
    val BodySmall    = 13.sp   // secondary links ("Forgot password?", "Already have an account?")
    val Body         = 14.sp   // standard body text, chips, checkboxes, terms, footer items
    val Input        = 15.sp   // text field inputs, placeholders, dropdown items
    val Subtitle     = 15.sp   // screen subtitles, supporting descriptions
    val Button       = 16.sp   // primary CTA button text
    val Wordmark     = 18.sp   // brand logo / top bar wordmark
    val Headline     = 26.sp   // main page headline ("Welcome back,", "Create your account")
}

// Global text styles

val WordmarkStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = AppFontSize.Wordmark
)

val HeadlineStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = AppFontSize.Headline,
    lineHeight = 28.sp,
    letterSpacing = (-0.5).sp,
    color = TextPrimary
)

val SubtitleStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = AppFontSize.Subtitle,
    lineHeight = 18.sp,
    color = TextSecondary
)

val SectionLabelStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.SemiBold,
    fontSize = AppFontSize.SectionLabel,
    letterSpacing = 1.5.sp,
    color = TextLabel
)

val PlainLabelStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = AppFontSize.Body,
    color = TextPrimary
)

val InputTextStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = AppFontSize.Input,
    color = TextPrimary
)

val PlaceholderTextStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = AppFontSize.Input,
    color = TextPlaceholder
)

val ButtonTextStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = AppFontSize.Button
)

val CaptionStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = AppFontSize.Caption,
    color = TextLabel
)

val FooterTextStyle = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = AppFontSize.Body,
    color = TextLabel
)

// Material 3 Typography

val Typography = Typography(
    headlineLarge  = HeadlineStyle,
    headlineMedium = HeadlineStyle.copy(fontSize = 24.sp),
    titleLarge     = WordmarkStyle,
    titleMedium    = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    bodyLarge      = InputTextStyle,
    bodyMedium     = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = AppFontSize.Body,
        color = TextPrimary
    ),
    bodySmall      = CaptionStyle,
    labelLarge     = ButtonTextStyle,
    labelMedium    = PlainLabelStyle,
    labelSmall     = SectionLabelStyle
)