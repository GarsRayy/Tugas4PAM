package org.garis.pam.screens.notes

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import org.garis.pam.GlassTheme
import org.garis.pam.db.NoteEntity
import androidx.compose.ui.draw.clip

@Composable
fun NoteDetailScreen(
    note: NoteEntity,
    onBack: () -> Unit,                // popBackStack()
    onEditClick: (Long) -> Unit,        // navigate ke EditNote dengan noteId
    onToggleFavorite: (Long) -> Unit,
    onDelete: (Long) -> Unit
) {
    val accentColor = GlassTheme.colors.Violet

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(GlassTheme.colors.BgPage, GlassTheme.colors.BgPhone)
                )
            )
    ) {
        // Header dengan back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
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

            // Action buttons
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Favorite
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.dp, GlassTheme.colors.GlassBorder, CircleShape)
                        .background(GlassTheme.colors.GlassBg)
                        .clickable { onToggleFavorite(note.id) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("🤍", fontSize = 16.sp)
                }
                // Edit
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(GlassTheme.colors.Violet, GlassTheme.colors.Pink)
                            )
                        )
                        .clickable { onEditClick(note.id) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("✏", fontSize = 16.sp)
                }
            }
        }

        // Konten note
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            // Accent color dot
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(accentColor)
            )
            Spacer(Modifier.height(12.dp))

            Text(
                note.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = GlassTheme.colors.TextPrimary,
                lineHeight = 32.sp
            )

            Spacer(Modifier.height(8.dp))
            Text(
                note.created_at.toString(),
                fontSize = 12.sp,
                color = GlassTheme.colors.TextMuted
            )

            Spacer(Modifier.height(20.dp))

            // Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(GlassTheme.colors.GlassBorder2)
            )

            Spacer(Modifier.height(20.dp))

            Text(
                note.content,
                fontSize = 15.sp,
                color = GlassTheme.colors.TextSecond,
                lineHeight = 24.sp
            )

            Spacer(Modifier.height(40.dp))

            // Tombol delete
            OutlinedButton(
                onClick = { onDelete(note.id) },
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, GlassTheme.colors.Pink.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = GlassTheme.colors.Pink.copy(alpha = 0.1f)
                )
            ) {
                Text("🗑  Hapus Catatan", color = GlassTheme.colors.Pink)
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}