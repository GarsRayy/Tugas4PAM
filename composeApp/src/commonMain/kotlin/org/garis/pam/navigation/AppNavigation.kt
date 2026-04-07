package org.garis.pam.navigation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import org.garis.pam.GlassTheme
import org.garis.pam.screens.FavoritesScreen
import org.garis.pam.screens.notes.*
import org.garis.pam.ui.ProfileScreen
import org.garis.pam.ui.EditProfileScreen
import org.garis.pam.viewmodel.NoteViewModel
import org.garis.pam.viewmodel.ProfileViewModel

@Composable
fun AppNavigation(
    profileViewModel: ProfileViewModel,
    noteViewModel: NoteViewModel,
    isDarkMode: Boolean,
    onToggleDark: () -> Unit
) {
    val navController = rememberNavController()
    val noteUiState by noteViewModel.uiState.collectAsState()
    val profileUiState by profileViewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            // Hanya tampilkan bottom nav di tab utama,
            // sembunyikan saat di detail/add/edit screen
            val currentRoute = navController
                .currentBackStackEntryAsState().value?.destination?.route
            val showBottomNav = currentRoute in listOf(
                Screen.Notes.route,
                Screen.Favorites.route,
                Screen.Profile.route
            )
            if (showBottomNav) {
                GlassBottomNav(navController = navController)
            }
        },
        containerColor = GlassTheme.colors.BgPage
    ) { paddingValues ->

        NavHost(
            navController   = navController,
            startDestination = Screen.Notes.route,
            modifier = Modifier.padding(paddingValues)
        ) {

            // ── TAB: Notes ──
            composable(Screen.Notes.route) {
                NoteListScreen(
                    notes            = noteUiState.notes,
                    onNoteClick      = { noteId ->
                        navController.navigate(Screen.NoteDetail.createRoute(noteId))
                    },
                    onAddClick       = { navController.navigate(Screen.AddNote.route) },
                    onToggleFavorite = noteViewModel::toggleFavorite
                )
            }

            // ── TAB: Favorites ──
            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    favorites        = noteUiState.notes.filter { it.isFavorite },
                    onNoteClick      = { noteId ->
                        navController.navigate(Screen.NoteDetail.createRoute(noteId))
                    },
                    onToggleFavorite = noteViewModel::toggleFavorite
                )
            }

            // ── TAB: Profile ──
            composable(Screen.Profile.route) {
                ProfileScreen(
                    profile      = profileUiState.profile,
                    isDarkMode   = isDarkMode,
                    onEditClick  = {
                        // ← INI yang harus ada, navigate ke route edit_profile
                        navController.navigate("edit_profile")
                    },
                    onToggleDark = onToggleDark
                )
            }

            // ── Note Detail (dengan argument noteId) ──
            composable(
                route = Screen.NoteDetail.route,
                arguments = listOf(
                    navArgument("noteId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                val note = noteViewModel.getNoteById(noteId)
                note?.let {
                    NoteDetailScreen(
                        note             = it,
                        onBack           = { navController.popBackStack() },
                        onEditClick      = { id ->
                            navController.navigate(Screen.EditNote.createRoute(id))
                        },
                        onToggleFavorite = noteViewModel::toggleFavorite,
                        onDelete         = { id ->
                            noteViewModel.deleteNote(id)
                            navController.popBackStack()
                        }
                    )
                }
            }

            // ── Add Note ──
            composable(Screen.AddNote.route) {
                AddEditNoteScreen(
                    title           = noteUiState.editTitle,
                    content         = noteUiState.editContent,
                    isEditMode      = false,
                    onTitleChange   = noteViewModel::onTitleChange,
                    onContentChange = noteViewModel::onContentChange,
                    onSave          = {
                        noteViewModel.addNote(
                            noteUiState.editTitle,
                            noteUiState.editContent
                        )
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            // ── Edit Note (dengan argument noteId) ──
            composable(
                route = Screen.EditNote.route,
                arguments = listOf(
                    navArgument("noteId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                val note = noteViewModel.getNoteById(noteId)
                note?.let {
                    noteViewModel.selectNote(it)
                    AddEditNoteScreen(
                        title           = noteUiState.editTitle,
                        content         = noteUiState.editContent,
                        isEditMode      = true,
                        onTitleChange   = noteViewModel::onTitleChange,
                        onContentChange = noteViewModel::onContentChange,
                        onSave          = {
                            noteViewModel.saveNote(noteId)
                            navController.popBackStack()
                        },
                        onBack = { navController.popBackStack() }
                    )
                }
            }

            // ── Edit Profile ──
            composable("edit_profile") {
                EditProfileScreen(
                    editName     = profileUiState.editName,
                    editBio      = profileUiState.editBio,
                    onNameChange = profileViewModel::onNameChange,
                    onBioChange  = profileViewModel::onBioChange,
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

// ── Glass Bottom Navigation Bar ──
@Composable
fun GlassBottomNav(navController: NavController) {
    val currentRoute = navController
        .currentBackStackEntryAsState().value?.destination?.route

    val items = listOf(
        Triple(Screen.Notes.route,     "📝", "Notes"),
        Triple(Screen.Favorites.route, "❤",  "Favorit"),
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
                            // Sesuai materi: popUpTo + launchSingleTop
                            popUpTo(navController.graph.startDestinationId) {
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