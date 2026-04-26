package org.garis.pam.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.Color
import org.garis.pam.GlassTheme
import org.garis.pam.ui.screens.favorites.FavoritesScreen
import org.garis.pam.ui.screens.notes.*
import org.garis.pam.ui.screens.profile.ProfileScreen
import org.garis.pam.ui.screens.profile.EditProfileScreen
import org.garis.pam.ui.screens.news.NewsListScreen
import org.garis.pam.ui.screens.news.NewsDetailScreen
import org.garis.pam.ui.screens.settings.SettingsScreen
import org.garis.pam.viewmodel.*
import org.garis.pam.data.local.DatabaseDriverFactory
import org.garis.pam.data.local.SettingsManager
import org.garis.pam.data.repository.NoteRepository
import org.garis.pam.data.repository.NewsRepository
import org.garis.pam.data.remote.HttpClientFactory
import org.garis.pam.data.model.Article
import org.garis.pam.db.*
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavigation(
    profileViewModel: ProfileViewModel,
    isDarkMode: Boolean,
    onToggleDark: () -> Unit,
    databaseDriverFactory: DatabaseDriverFactory
) {
    val navController = rememberNavController()
    
    val noteViewModel: NoteViewModel = koinViewModel()
    val settingsViewModel: SettingsViewModel = koinViewModel()
    val newsViewModel: NewsViewModel = koinViewModel()

    val favoriteNotes by noteViewModel.favoriteNotes.collectAsState()
    val selectedNote by noteViewModel.selectedNote.collectAsState()
    val profileUiState by profileViewModel.uiState.collectAsState()

    SharedTransitionLayout {
        Scaffold(
            bottomBar = {
                val currentRoute = navController
                    .currentBackStackEntryAsState().value?.destination?.route
                val showBottomNav = currentRoute in listOf(
                    Screen.Notes.route,
                    Screen.Favorites.route,
                    Screen.Profile.route,
                    Screen.News.route
                )
                if (showBottomNav) {
                    GlassBottomNav(navController = navController)
                }
            },
            containerColor = GlassTheme.colors.BgPage
        ) { paddingValues ->

            NavHost(
                navController = navController,
                startDestination = Screen.Notes.route,
                modifier = Modifier.padding(paddingValues)
            ) {

                // ── TAB: Notes ──
                composable(Screen.Notes.route) {
                    NoteListScreen(
                        viewModel        = noteViewModel,
                        settingsViewModel = settingsViewModel,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                        onNoteClick      = { noteId: Long ->
                            noteViewModel.selectNote(noteId)
                            navController.navigate(Screen.NoteDetail.createRoute(noteId))
                        },
                        onAddClick       = { 
                            noteViewModel.clearSelectedNote()
                            navController.navigate(Screen.AddNote.route) 
                        },
                        onToggleFavorite = { noteId: Long ->
                            noteViewModel.toggleFavorite(noteId)
                        },
                        onTogglePin = { noteId: Long ->
                            noteViewModel.togglePin(noteId)
                        },
                        onArchiveClick = {
                            navController.navigate(Screen.Archive.route)
                        },
                        onHiddenClick = {
                            navController.navigate(Screen.HiddenNotes.route)
                        }
                    )
                }

                // ── TAB: Favorites ──
                composable(Screen.Favorites.route) {
                    FavoritesScreen(
                        favorites        = favoriteNotes,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                        onNoteClick      = { noteId: Long ->
                            noteViewModel.selectNote(noteId)
                            navController.navigate(Screen.NoteDetail.createRoute(noteId))
                        },
                        onToggleFavorite = { noteId: Long ->
                            noteViewModel.toggleFavorite(noteId)
                        },
                        onTogglePin = { noteId: Long ->
                            noteViewModel.togglePin(noteId)
                        }
                    )
                }

                // ── TAB: News ──
                composable(Screen.News.route) {
                    NewsListScreen(
                        viewModel = newsViewModel,
                        onNavigateToDetail = { article: Article ->
                            newsViewModel.selectArticle(article)
                            navController.navigate(Screen.NewsDetail.route)
                        }
                    )
                }

                // ── TAB: Profile ──
                composable(Screen.Profile.route) {
                    ProfileScreen(
                        profile      = profileUiState.profile,
                        isDarkMode   = isDarkMode,
                        onEditClick  = {
                            profileViewModel.startEditing()
                            navController.navigate("edit_profile")
                        },
                        onProfileImageChange = profileViewModel::onProfileImageChange,
                        onSettingsClick = {
                            navController.navigate(Screen.Settings.route)
                        },
                        noteViewModel = noteViewModel,
                        newsViewModel = newsViewModel
                    )
                }

                // ── Settings ──
                composable(Screen.Settings.route) {
                    SettingsScreen(
                        viewModel = settingsViewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // ── News Detail ──
                composable(Screen.NewsDetail.route) {
                    val article by newsViewModel.selectedArticle.collectAsState()
                    article?.let {
                        NewsDetailScreen(
                            article = it,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }

                // ── Note Detail (dengan argument noteId) ──
                composable(
                    route = Screen.NoteDetail.route,
                    arguments = listOf(
                        navArgument("noteId") { type = NavType.LongType }
                    )
                ) {
                    selectedNote?.let {
                        NoteDetailScreen(
                            note             = it,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this@composable,
                            onBack           = { navController.popBackStack() },
                            onEditClick      = { id ->
                                navController.navigate(Screen.EditNote.createRoute(id))
                            },
                            onToggleFavorite = { id -> noteViewModel.toggleFavorite(id) },
                            onArchiveClick   = { id -> noteViewModel.toggleArchive(id) },
                            onHiddenClick    = { id -> noteViewModel.toggleHidden(id) },
                            onDelete         = { id ->
                                noteViewModel.deleteNote(id)
                                navController.popBackStack()
                            }
                        )
                    }
                }

                // ── Archive ──
                composable(Screen.Archive.route) {
                    ArchiveScreen(
                        viewModel = noteViewModel,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                        onNoteClick = { noteId ->
                            noteViewModel.selectNote(noteId)
                            navController.navigate(Screen.NoteDetail.createRoute(noteId))
                        },
                        onBack = { navController.popBackStack() }
                    )
                }

                // ── Hidden Notes ──
                composable(Screen.HiddenNotes.route) {
                    HiddenNotesScreen(
                        viewModel = noteViewModel,
                        settingsViewModel = settingsViewModel,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                        onNoteClick = { noteId ->
                            noteViewModel.selectNote(noteId)
                            navController.navigate(Screen.NoteDetail.createRoute(noteId))
                        },
                        onBackClick = { navController.popBackStack() }
                    )
                }

                // ── Add Note ──
                composable(Screen.AddNote.route) {
                    AddEditNoteScreen(
                        viewModel = noteViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // ── Edit Note (dengan argument noteId) ──
                composable(
                    route = Screen.EditNote.route,
                    arguments = listOf(
                        navArgument("noteId") { type = NavType.LongType }
                    )
                ) {
                    AddEditNoteScreen(
                        viewModel = noteViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                // ── Edit Profile ──
                composable("edit_profile") {
                    EditProfileScreen(
                        editName     = profileUiState.editName,
                        editBio      = profileUiState.editBio,
                        editEmail    = profileUiState.editEmail,
                        editPhone    = profileUiState.editPhone,
                        editLocation = profileUiState.editLocation,
                        editGithub   = profileUiState.editGithub,
                        editLinkedin = profileUiState.editLinkedin,
                        editInstagram = profileUiState.editInstagram,
                        onNameChange = profileViewModel::onNameChange,
                        onBioChange  = profileViewModel::onBioChange,
                        onEmailChange = profileViewModel::onEmailChange,
                        onPhoneChange = profileViewModel::onPhoneChange,
                        onLocationChange = profileViewModel::onLocationChange,
                        onGithubChange = profileViewModel::onGithubChange,
                        onLinkedinChange = profileViewModel::onLinkedinChange,
                        onInstagramChange = profileViewModel::onInstagramChange,
                        onSave       = {
                            profileViewModel.saveProfile()
                            navController.popBackStack()
                        },
                        onCancel = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@Composable
fun GlassBottomNav(navController: NavController) {
    val currentRoute = navController
        .currentBackStackEntryAsState().value?.destination?.route

    val items = listOf(
        Triple(Screen.Notes.route,     "📝", "Notes"),
        Triple(Screen.Favorites.route, "❤",  "Favorit"),
        Triple(Screen.News.route,      "📰", "News"),
        Triple(Screen.Profile.route,   "👤", "Profil")
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GlassTheme.colors.BgPhone.copy(alpha = 0.9f))
            .border(
                1.dp,
                GlassTheme.colors.GlassBorder2,
                RoundedCornerShape(0.dp)
            )
            .padding(horizontal = 8.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        items.forEach { (route, icon, label) ->
            val isActive = currentRoute == route
            val scale by androidx.compose.animation.core.animateFloatAsState(
                targetValue = if (isActive) 1.08f else 1f,
                animationSpec = androidx.compose.animation.core.spring(
                    dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
                ),
                label = "scale_$route"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        navController.navigate(route) {
                            popUpTo(Screen.Notes.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    }
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .graphicsLayer { scaleX = scale; scaleY = scale }
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isActive)
                                Brush.linearGradient(
                                    listOf(
                                        GlassTheme.colors.Violet.copy(alpha = 0.45f),
                                        GlassTheme.colors.Lavender.copy(alpha = 0.25f)
                                    )
                                )
                            else
                                Brush.linearGradient(
                                    listOf(
                                        GlassTheme.colors.GlassBg,
                                        GlassTheme.colors.GlassBg
                                    )
                                )
                        )
                        .border(
                            1.dp,
                            if (isActive) GlassTheme.colors.Violet.copy(0.5f)
                            else GlassTheme.colors.GlassBorder2,
                            RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(icon, fontSize = 16.sp)
                }
                Spacer(Modifier.height(4.dp))
                val labelColor by androidx.compose.animation.animateColorAsState(
                    if (isActive) GlassTheme.colors.Lavender
                    else GlassTheme.colors.TextMuted,
                    label = "labelColor_$route"
                )
                Text(label, fontSize = 10.sp, color = labelColor)
            }
        }
    }
}
