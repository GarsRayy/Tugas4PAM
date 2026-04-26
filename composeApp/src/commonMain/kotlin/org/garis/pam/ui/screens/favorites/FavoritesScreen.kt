package org.garis.pam.ui.screens.favorites

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import org.garis.pam.GlassTheme
import org.garis.pam.db.NoteEntity
import org.garis.pam.ui.screens.notes.NoteCard

import org.garis.pam.ui.components.LottieEmptyAnimation

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FavoritesScreen(
    favorites: List<NoteEntity>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNoteClick: (Long) -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onTogglePin: (Long) -> Unit
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Column {
                Text(
                    "❤ Favorit",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = GlassTheme.colors.TextPrimary
                )
                Text(
                    "${favorites.size} catatan favorit",
                    fontSize = 13.sp,
                    color = GlassTheme.colors.TextMuted,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    LottieEmptyAnimation(modifier = Modifier.size(240.dp))
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Belum ada catatan favorit",
                        fontSize = 16.sp,
                        color = GlassTheme.colors.TextMuted
                    )
                    Text(
                        "Tap ❤ di catatan untuk menambahkan",
                        fontSize = 13.sp,
                        color = GlassTheme.colors.TextMuted,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 16.dp, end = 16.dp,
                    bottom = 100.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(favorites, key = { it.id }) { note ->
                    with(sharedTransitionScope) {
                        NoteCard(
                            note = note,
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState(key = "note-${note.id}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            ),
                            onClick = { onNoteClick(note.id) },
                            onToggleFavorite = { onToggleFavorite(note.id) },
                            onTogglePin = { onTogglePin(note.id) }
                        )
                    }
                }
            }
        }
    }
}
