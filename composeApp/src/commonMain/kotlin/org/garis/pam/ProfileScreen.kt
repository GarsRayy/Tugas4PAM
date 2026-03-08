package org.garis.pam

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(profile: UserProfile = myProfile) {
    // Struktur utama: Box untuk layer bg + Column untuk konten
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GlassTheme.BgPage)
    ) {
        // Kolom scrollable dengan semua section
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(GlassTheme.BgPage, GlassTheme.BgPhone)
                    )
                )
        ) {
            // Area scrollable (semua kecuali bottom nav)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // ── 1. HERO SECTION ──
                // Padanan: <div class="hero">
                HeroSection(
                    name  = profile.name,
                    badge = profile.badge
                )

                // ── 2. STATS + ACTION BUTTONS ──
                // Padanan: .stats-row + .profile-subtitle + .action-row
                StatsAndActions(subtitle = profile.subtitle)

                Spacer(Modifier.height(4.dp))

                // ── 3. INFORMASI KONTAK ──
                // Padanan: 2-col cards + full-width location card
                ContactSection(
                    email    = profile.email,
                    phone    = profile.phone,
                    location = profile.location
                )

                Spacer(Modifier.height(12.dp))

                // ── 4. BIO CARD ──
                // Padanan: <div class="gcard"> bio + AnimatedVisibility BONUS
                BioCard(bio = profile.bio)

                Spacer(Modifier.height(12.dp))

                // ── 5. SKILLS GRID 3-COL ──
                // Padanan: <div class="skills-grid">
                SkillsGrid()

                Spacer(Modifier.height(20.dp))
            }

            // ── 6. BOTTOM NAVIGATION ──
            // Padanan: <div class="bottom-nav"> — sticky di bawah
            BottomNav()
        }
    }
}