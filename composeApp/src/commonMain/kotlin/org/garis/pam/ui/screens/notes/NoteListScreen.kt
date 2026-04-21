package org.garis.pam.ui.screens.notes

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import org.garis.pam.GlassTheme
import org.garis.pam.db.NoteEntity
import androidx.compose.ui.draw.clip

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import org.garis.pam.viewmodel.NoteViewModel
import org.garis.pam.viewmodel.SettingsViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

@Composable
fun NoteListScreen(
    viewModel: NoteViewModel,
    settingsViewModel: SettingsViewModel,
    onNoteClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onTogglePin: (Long) -> Unit,
    onArchiveClick: () -> Unit
) {
    val notes by viewModel.notes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currentSortOrder by settingsViewModel.currentSortOrder.collectAsState()

    LaunchedEffect(currentSortOrder) {
        viewModel.updateSortOrder(currentSortOrder)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(GlassTheme.colors.BgPage, GlassTheme.colors.BgPhone)
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(GlassTheme.colors.BgPhone, Color.Transparent)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Text(
                        "📝 Catatan",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = GlassTheme.colors.TextPrimary
                    )
                    Text(
                        "${notes.size} catatan tersimpan",
                        fontSize = 13.sp,
                        color = GlassTheme.colors.TextMuted,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(GlassTheme.colors.GlassBg)
                        .border(1.dp, GlassTheme.colors.GlassBorder, CircleShape)
                        .clickable(onClick = onArchiveClick),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📦", fontSize = 18.sp)
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Cari catatan...", color = GlassTheme.colors.TextMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) },
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GlassTheme.colors.Violet,
                    unfocusedBorderColor = GlassTheme.colors.GlassBorder2,
                    focusedContainerColor = GlassTheme.colors.GlassBg,
                    unfocusedContainerColor = GlassTheme.colors.GlassBg,
                    cursorColor = Color.White
                )
            )

            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp, end = 16.dp,
                    top = 4.dp, bottom = 100.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteCard(
                        note = note,
                        onClick = { onNoteClick(note.id) },
                        onToggleFavorite = { onToggleFavorite(note.id) },
                        onTogglePin = { onTogglePin(note.id) }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = onAddClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp),
            shape = RoundedCornerShape(18.dp),
            containerColor = GlassTheme.colors.Violet,
            contentColor = Color.White
        ) {
            Text("＋", fontSize = 24.sp)
        }
    }
}

