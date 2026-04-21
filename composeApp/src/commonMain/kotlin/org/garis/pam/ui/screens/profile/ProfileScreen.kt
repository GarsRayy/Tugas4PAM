package org.garis.pam.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import org.garis.pam.GlassTheme
import org.garis.pam.data.model.UserProfile
import org.garis.pam.viewmodel.SettingsViewModel
import org.garis.pam.ui.screens.settings.SettingsScreen

@Composable
fun ProfileScreen(
    profile: UserProfile,
    isDarkMode: Boolean,
    onEditClick: () -> Unit,       
    settingsViewModel: SettingsViewModel
) {
    var showSettings by remember { mutableStateOf(false) }

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
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onEditClick,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GlassTheme.colors.Violet)
                    ) {
                        Text("Edit Profil", color = androidx.compose.ui.graphics.Color.White)
                    }
                }

                StatsAndActions(
                    subtitle     = profile.subtitle,
                    isDarkMode   = isDarkMode,
                    onSettingsClick = { showSettings = true },
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

        if (showSettings) {
            AlertDialog(
                onDismissRequest = { showSettings = false },
                confirmButton = {
                    TextButton(onClick = { showSettings = false }) {
                        Text("Tutup", color = GlassTheme.colors.Violet)
                    }
                },
                title = { Text("Pengaturan", color = GlassTheme.colors.TextPrimary) },
                text = {
                    Box(modifier = Modifier.height(400.dp)) {
                        SettingsScreen(viewModel = settingsViewModel)
                    }
                },
                containerColor = GlassTheme.colors.BgPhone,
                shape = RoundedCornerShape(24.dp)
            )
        }
    }
}
