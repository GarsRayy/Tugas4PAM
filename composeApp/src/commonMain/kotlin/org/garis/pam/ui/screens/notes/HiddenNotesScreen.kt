package org.garis.pam.ui.screens.notes

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.garis.pam.GlassTheme
import org.garis.pam.viewmodel.NoteViewModel
import org.garis.pam.viewmodel.SettingsViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HiddenNotesScreen(
    viewModel: NoteViewModel,
    settingsViewModel: SettingsViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNoteClick: (Long) -> Unit,
    onBackClick: () -> Unit
) {
    val hiddenNotes by viewModel.hiddenNotes.collectAsState()
    val savedPassword by settingsViewModel.hiddenPassword.collectAsState()
    
    var isAuthenticated by remember { mutableStateOf(savedPassword.isEmpty()) }
    var passwordInput by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(GlassTheme.colors.BgPage, GlassTheme.colors.BgPhone)
                )
            )
    ) {
        if (!isAuthenticated) {
            // Password Entry UI
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Folder Tersembunyi",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = GlassTheme.colors.TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Masukkan password untuk melihat catatan tersembunyi",
                    fontSize = 14.sp,
                    color = GlassTheme.colors.TextMuted,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { 
                        passwordInput = it
                        showError = false
                    },
                    label = { Text("Password") },
                    singleLine = true,
                    isError = showError,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GlassTheme.colors.Violet,
                        unfocusedBorderColor = GlassTheme.colors.GlassBorder2,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                
                if (showError) {
                    Text(
                        "Password salah!",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Start).padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (passwordInput == savedPassword) {
                            isAuthenticated = true
                        } else {
                            showError = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GlassTheme.colors.Violet)
                ) {
                    Text("Buka Folder", color = Color.White)
                }
                
                TextButton(onClick = onBackClick) {
                    Text("Kembali", color = GlassTheme.colors.TextSecond)
                }
            }
        } else {
            // Hidden Notes List
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(GlassTheme.colors.GlassBg)
                                .clickable(onClick = onBackClick),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("←", color = Color.White, fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                "🔒 Tersembunyi",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = GlassTheme.colors.TextPrimary
                            )
                            Text(
                                "${hiddenNotes.size} catatan rahasia",
                                fontSize = 13.sp,
                                color = GlassTheme.colors.TextMuted
                            )
                        }
                    }
                }

                if (hiddenNotes.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Tidak ada catatan tersembunyi", color = GlassTheme.colors.TextMuted)
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(hiddenNotes) { note ->
                            with(sharedTransitionScope) {
                                NoteCard(
                                    note = note,
                                    modifier = Modifier.sharedElement(
                                        rememberSharedContentState(key = "note-${note.id}"),
                                        animatedVisibilityScope = animatedVisibilityScope
                                    ),
                                    onClick = { onNoteClick(note.id) },
                                    onToggleFavorite = { viewModel.toggleFavorite(note.id) },
                                    onTogglePin = { viewModel.togglePin(note.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
