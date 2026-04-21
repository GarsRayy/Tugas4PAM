package org.garis.pam.ui.screens.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.garis.pam.GlassTheme
import org.garis.pam.db.NoteEntity

@Composable
fun NoteCard(
    note: NoteEntity,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onTogglePin: () -> Unit
) {
    val accentColor = when (note.color_name) {
        "VIOLET" -> GlassTheme.colors.Violet
        "TEAL"   -> GlassTheme.colors.Teal
        "PINK"   -> GlassTheme.colors.Pink
        "GOLD"   -> GlassTheme.colors.Gold
        "SKY"    -> GlassTheme.colors.Sky
        else     -> GlassTheme.colors.Violet
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                1.dp, 
                if (note.is_pinned == 1L) GlassTheme.colors.Violet else GlassTheme.colors.GlassBorder, 
                RoundedCornerShape(16.dp)
            )
            .background(if (note.is_pinned == 1L) GlassTheme.colors.Violet.copy(alpha = 0.05f) else GlassTheme.colors.GlassBg)
            .clickable(onClick = onClick)
    ) {
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
                if (note.tags.isNotBlank()) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        note.tags.split(" ").filter { it.startsWith("#") }.take(3).forEach { tag ->
                            Surface(
                                color = accentColor.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    tag,
                                    fontSize = 9.sp,
                                    color = accentColor,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                }
                Text(
                    "Dibuat pada: ${note.created_at}",
                    fontSize = 11.sp,
                    color = GlassTheme.colors.TextMuted
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (note.is_pinned == 1L) GlassTheme.colors.Violet.copy(alpha = 0.2f) else GlassTheme.colors.GlassBg)
                        .clickable(onClick = onTogglePin),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (note.is_pinned == 1L) "📌" else "📍",
                        fontSize = 14.sp,
                        modifier = Modifier.graphicsLayer { rotationZ = if (note.is_pinned == 1L) 0f else -45f }
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(GlassTheme.colors.GlassBg)
                        .clickable(onClick = onToggleFavorite),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (note.is_favorite == 1L) "❤" else "🤍",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
