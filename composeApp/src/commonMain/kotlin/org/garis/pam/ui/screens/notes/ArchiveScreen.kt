package org.garis.pam.ui.screens.notes

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
import androidx.compose.ui.unit.*
import org.garis.pam.GlassTheme
import org.garis.pam.viewmodel.NoteViewModel
import androidx.compose.ui.draw.clip

@Composable
fun ArchiveScreen(
    viewModel: NoteViewModel,
    onNoteClick: (Long) -> Unit,
    onBack: () -> Unit
) {
    val archivedNotes by viewModel.archivedNotes.collectAsState()

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.dp, GlassTheme.colors.GlassBorder, CircleShape)
                        .background(GlassTheme.colors.GlassBg)
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center
                ) {
                    Text("‹", fontSize = 22.sp, color = GlassTheme.colors.TextPrimary)
                }
                
                Spacer(Modifier.width(16.dp))
                
                Text(
                    "📦 Arsip",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = GlassTheme.colors.TextPrimary
                )
            }

            if (archivedNotes.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Tidak ada catatan diarsip",
                        color = GlassTheme.colors.TextMuted,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(archivedNotes, key = { it.id }) { note ->
                        NoteCard(
                            note = note,
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
