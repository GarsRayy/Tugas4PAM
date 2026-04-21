package org.garis.pam.ui.screens.notes

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import org.garis.pam.GlassTheme
import org.garis.pam.db.NoteEntity
import androidx.compose.ui.draw.clip
import org.garis.pam.ui.components.MarkdownText

@Composable
fun NoteDetailScreen(
    note: NoteEntity,
    onBack: () -> Unit,
    onEditClick: (Long) -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onArchiveClick: (Long) -> Unit,
    onDelete: (Long) -> Unit
) {
    val accentColor = when (note.color_name) {
        "VIOLET" -> GlassTheme.colors.Violet
        "TEAL"   -> GlassTheme.colors.Teal
        "PINK"   -> GlassTheme.colors.Pink
        "GOLD"   -> GlassTheme.colors.Gold
        "SKY"    -> GlassTheme.colors.Sky
        else     -> GlassTheme.colors.Violet
    }

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
                    Text(if (note.is_favorite == 1L) "❤️" else "🤍", fontSize = 16.sp)
                }

                // Archive
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.dp, GlassTheme.colors.GlassBorder, CircleShape)
                        .background(GlassTheme.colors.GlassBg)
                        .clickable { onArchiveClick(note.id) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (note.is_archived == 1L) "📥" else "📦", fontSize = 16.sp)
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
            // Accent color dot & Tags
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
                
                if (note.tags.isNotBlank()) {
                    Spacer(Modifier.width(12.dp))
                    Text(
                        note.tags,
                        fontSize = 12.sp,
                        color = accentColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
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
                "Dibuat pada: ${note.created_at}", 
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

            MarkdownText(note.content)

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
