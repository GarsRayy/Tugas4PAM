package org.garis.pam.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import org.garis.pam.GlassTheme
import org.garis.pam.data.UserProfile

@Composable
fun ProfileScreen(
    profile: UserProfile,
    isDarkMode: Boolean,
    onEditClick: () -> Unit,       // callback ke ViewModel.startEditing()
    onToggleDark: () -> Unit       // callback ke ViewModel.toggleDarkMode()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GlassTheme.colors.BgPage)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(GlassTheme.colors.BgPage, GlassTheme.colors.BgPhone)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                HeroSection(
                    name  = profile.name,
                    badge = profile.badge
                )
                StatsAndActions(
                    subtitle     = profile.subtitle,
                    isDarkMode   = isDarkMode,
                    onToggleDark = onToggleDark,
                    onEditClick  = onEditClick
                )
                Spacer(Modifier.height(4.dp))
                ContactSection(
                    email    = profile.email,
                    phone    = profile.phone,
                    location = profile.location
                )
                Spacer(Modifier.height(12.dp))
                BioCard(bio = profile.bio)
                Spacer(Modifier.height(12.dp))
                SkillsGrid()
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}