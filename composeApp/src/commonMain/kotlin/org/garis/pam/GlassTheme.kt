package org.garis.pam

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

// ── Color palette tetap sama ──
object GlassPalette {
    val Violet      = Color(0xFF7C6EFA)
    val Lavender    = Color(0xFFA78BFA)
    val Pink        = Color(0xFFF472B6)
    val Teal        = Color(0xFF2DD4BF)
    val Sky         = Color(0xFF38BDF8)
    val Gold        = Color(0xFFFCD34D)
    val Green       = Color(0xFF4ADE80)
    val Orange      = Color(0xFFFB923C)
}

// ── Color set yang berubah berdasarkan dark/light ──
data class GlassColors(
    val BgPage      : Color,
    val BgPhone     : Color,
    val BgHeroTop   : Color,
    val GlassBg     : Color,
    val GlassBorder : Color,
    val GlassBorder2: Color,
    val TextPrimary : Color,
    val TextSecond  : Color,
    val TextMuted   : Color,
    // Aksen selalu sama
    val Violet      : Color = GlassPalette.Violet,
    val Lavender    : Color = GlassPalette.Lavender,
    val Pink        : Color = GlassPalette.Pink,
    val Teal        : Color = GlassPalette.Teal,
    val Sky         : Color = GlassPalette.Sky,
    val Gold        : Color = GlassPalette.Gold,
    val Green       : Color = GlassPalette.Green,
    val Orange      : Color = GlassPalette.Orange,
)

// ── Dark colors ──
val DarkGlassColors = GlassColors(
    BgPage       = Color(0xFF060410),
    BgPhone      = Color(0xFF0B0912),
    BgHeroTop    = Color(0xFF1A0F35),
    GlassBg      = Color(0x10FFFFFF),
    GlassBorder  = Color(0x21FFFFFF),
    GlassBorder2 = Color(0x12FFFFFF),
    TextPrimary  = Color(0xEBFFFFFF),
    TextSecond   = Color(0x8CFFFFFF),
    TextMuted    = Color(0x52FFFFFF),
)

// ── Light colors ──
val LightGlassColors = GlassColors(
    BgPage       = Color(0xFFF0EEFF),
    BgPhone      = Color(0xFFE8E0FF),
    BgHeroTop    = Color(0xFF7C6EFA),
    GlassBg      = Color(0x18000000),
    GlassBorder  = Color(0x28000000),
    GlassBorder2 = Color(0x14000000),
    TextPrimary  = Color(0xFF1A1040),   // hitam keunguan — terbaca di bg terang
    TextSecond   = Color(0xFF4A3F7A),   // ungu medium
    TextMuted    = Color(0xFF8878BB),   // ungu muda
)

// ── CompositionLocal supaya GlassTheme bisa diakses dari mana saja ──
val LocalGlassColors = compositionLocalOf { DarkGlassColors }

// ── Shortcut akses: GlassTheme.colors.Violet dll ──
object GlassTheme {
    val colors: GlassColors
        @Composable
        @ReadOnlyComposable
        get() = LocalGlassColors.current
}