package org.garis.pam.screens.notes

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
import org.garis.pam.data.Note
import org.garis.pam.data.NoteColor
import androidx.compose.ui.draw.clip

@Composable
fun NoteListScreen(
    notes: List<Note>,
    onNoteClick: (Int) -> Unit,       // navigate ke detail dengan noteId
    onAddClick: () -> Unit,           // navigate ke add note
    onToggleFavorite: (Int) -> Unit
) {
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
            }

            // List notes
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
                        onToggleFavorite = { onToggleFavorite(note.id) }
                    )
                }
            }
        }

        // FAB tambah note
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

@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val accentColor = when (note.color) {
        NoteColor.VIOLET -> GlassTheme.colors.Violet
        NoteColor.TEAL   -> GlassTheme.colors.Teal
        NoteColor.PINK   -> GlassTheme.colors.Pink
        NoteColor.GOLD   -> GlassTheme.colors.Gold
        NoteColor.SKY    -> GlassTheme.colors.Sky
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, GlassTheme.colors.GlassBorder, RoundedCornerShape(16.dp))
            .background(GlassTheme.colors.GlassBg)
            .clickable(onClick = onClick)
    ) {
        // Accent bar kiri
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(60.dp)
                .align(Alignment.CenterStart)
                .background(accentColor, RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 12.dp, top = 14.dp, bottom = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    note.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GlassTheme.colors.TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    note.content,
                    fontSize = 12.sp,
                    color = GlassTheme.colors.TextSecond,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    note.createdAt,
                    fontSize = 11.sp,
                    color = GlassTheme.colors.TextMuted
                )
            }

            // Favorite button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(GlassTheme.colors.GlassBg)
                    .clickable(onClick = onToggleFavorite),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (note.isFavorite) "❤" else "🤍",
                    fontSize = 16.sp
                )
            }
        }
    }
}