package org.garis.pam.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import org.garis.pam.GlassTheme
import org.garis.pam.data.model.UserProfile
import org.garis.pam.viewmodel.NoteViewModel
import org.garis.pam.viewmodel.NewsViewModel
import androidx.compose.ui.platform.LocalUriHandler

@Composable
fun ProfileScreen(
    profile: UserProfile,
    isDarkMode: Boolean,
    onEditClick: () -> Unit,
    onProfileImageChange: (String?) -> Unit,
    onSettingsClick: () -> Unit,
    noteViewModel: NoteViewModel,
    newsViewModel: NewsViewModel
) {
    var showAvatarPicker by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    val notes by noteViewModel.notes.collectAsState()
    val favorites by noteViewModel.favoriteNotes.collectAsState()
    val newsState by newsViewModel.uiState.collectAsState()
    
    val newsCount = if (newsState is org.garis.pam.viewmodel.NewsUiState.Success) {
        (newsState as org.garis.pam.viewmodel.NewsUiState.Success).articles.size
    } else 0

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
                    badge = profile.badge,
                    profileImage = profile.profileImage,
                    onImageClick = { showAvatarPicker = true }
                )

                StatsAndActions(
                    subtitle     = profile.subtitle,
                    totalNotes   = notes.size,
                    totalFavorites = favorites.size,
                    totalNewsRead = newsCount,
                    onSettingsClick = onSettingsClick,
                    onEditClick  = onEditClick
                )
                
                SocialSection(
                    githubUrl = profile.githubUrl,
                    linkedinUrl = profile.linkedinUrl,
                    instagramUrl = profile.instagramUrl,
                    onUrlClick = { url ->
                        if (url.isNotEmpty()) {
                            try { uriHandler.openUri(url) } catch (e: Exception) {}
                        }
                    }
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

        if (showAvatarPicker) {
            AvatarSelectionDialog(
                onDismiss = { showAvatarPicker = false },
                onAvatarSelected = { avatar ->
                    onProfileImageChange(avatar)
                }
            )
        }
    }
}
