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
import org.garis.pam.data.model.remote.AiInsightResponse
import org.garis.pam.data.model.remote.AiAction
import org.garis.pam.ui.components.MarkdownText
import org.garis.pam.core.network.NetworkResult
import org.garis.pam.viewmodel.NoteViewModel
import androidx.compose.animation.*

import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NoteDetailScreen(
    note: NoteEntity,
    viewModel: NoteViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onBack: () -> Unit,
    onEditClick: (Long) -> Unit,
    onToggleFavorite: (Long) -> Unit,
    onArchiveClick: (Long) -> Unit,
    onHiddenClick: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    onThemeChange: (String) -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current
    val accentColor = when (note.color_name) {
        "VIOLET" -> GlassTheme.colors.Violet
        "TEAL"   -> GlassTheme.colors.Teal
        "PINK"   -> GlassTheme.colors.Pink
        "GOLD"   -> GlassTheme.colors.Gold
        "SKY"    -> GlassTheme.colors.Sky
        else     -> GlassTheme.colors.Violet
    }

    val aiState = viewModel.aiSummaryState
    val insightState = viewModel.aiInsightState

    with(sharedTransitionScope) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .sharedElement(
                        rememberSharedContentState(key = "note-${note.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
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
                                .clickable { 
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onToggleFavorite(note.id) 
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(if (note.is_favorite == 1L) "❤️" else "🤍", fontSize = 16.sp)
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
                                .clickable { 
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onEditClick(note.id) 
                                },
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
                    // Tags & Title
                    if (note.tags.isNotBlank()) {
                        Text(
                            note.tags,
                            fontSize = 12.sp,
                            color = accentColor,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    Text(
                        note.title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = GlassTheme.colors.TextPrimary,
                        lineHeight = 32.sp
                    )

                    Spacer(Modifier.height(20.dp))

                    MarkdownText(note.content)

                    Spacer(Modifier.height(32.dp))

                    // AI Generative UI Section ✨
                    when (insightState) {
                        is NetworkResult.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(GlassTheme.colors.Teal.copy(alpha = 0.05f))
                                    .border(1.dp, GlassTheme.colors.Teal.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = GlassTheme.colors.Teal, strokeWidth = 2.dp)
                                    Spacer(Modifier.width(12.dp))
                                    Text("Sedang mengambil insight...", fontSize = 14.sp, color = GlassTheme.colors.TextSecond)
                                }
                            }
                            Spacer(Modifier.height(24.dp))
                        }
                        is NetworkResult.Error -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(GlassTheme.colors.Pink.copy(alpha = 0.05f))
                                    .border(1.dp, GlassTheme.colors.Pink.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Gagal mengambil insight", color = GlassTheme.colors.Pink, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text(insightState.message ?: "Terjadi kesalahan", fontSize = 12.sp, color = GlassTheme.colors.TextMuted, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                                }
                            }
                            Spacer(Modifier.height(24.dp))
                        }
                        is NetworkResult.Success -> {
                            AiInsightCard(
                                insight = insightState.data,
                                onActionClick = { action ->
                                    viewModel.performAction(action)
                                },
                                onApplyTheme = { theme ->
                                    val themeString = "ai_theme|${theme.primaryHex}|${theme.secondaryHex}|${theme.accentHex}|${theme.backgroundHex}|${theme.name}"
                                    onThemeChange(themeString)
                                }
                            )
                            Spacer(Modifier.height(24.dp))
                        }
                        null -> {}
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // AI Summary Button
                        Button(
                            onClick = { 
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.summarizeNote(note.content)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GlassTheme.colors.Violet.copy(alpha = 0.15f),
                                contentColor = GlassTheme.colors.Violet
                            ),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, GlassTheme.colors.Violet.copy(alpha = 0.3f))
                        ) {
                            Text("✨ Rangkum", fontWeight = FontWeight.Bold)
                        }

                        // AI Insight Button
                        Button(
                            onClick = { 
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.getInsights(note.content)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GlassTheme.colors.Teal.copy(alpha = 0.15f),
                                contentColor = GlassTheme.colors.Teal
                            ),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, GlassTheme.colors.Teal.copy(alpha = 0.3f))
                        ) {
                            Text("💡 Insight", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    OutlinedButton(
                        onClick = { onDelete(note.id) },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GlassTheme.colors.Pink)
                    ) {
                        Text("🗑 Hapus Catatan")
                    }
                    
                    Spacer(Modifier.height(40.dp))
                }
            }

            // AI Bottom Sheet Overlay (Glassmorphism)
            AnimatedVisibility(
                visible = aiState != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .clickable { viewModel.resetAiState() },
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.7f)
                            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                            .background(GlassTheme.colors.BgPage.copy(alpha = 0.95f))
                            .border(1.dp, GlassTheme.colors.GlassBorder, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                            .padding(24.dp)
                            .clickable(enabled = false) { }
                    ) {
                        // Handle bar
                        Box(
                            modifier = Modifier
                                .size(40.dp, 4.dp)
                                .clip(CircleShape)
                                .background(GlassTheme.colors.TextMuted.copy(alpha = 0.3f))
                                .align(Alignment.CenterHorizontally)
                        )
                        
                        Spacer(Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "AI Note Assistant ✨",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = GlassTheme.colors.TextPrimary
                            )
                            IconButton(onClick = { viewModel.resetAiState() }) {
                                Text("✕", color = GlassTheme.colors.TextSecond)
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        Box(modifier = Modifier.weight(1f)) {
                            when (aiState) {
                                is NetworkResult.Loading -> {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        CircularProgressIndicator(color = GlassTheme.colors.Violet)
                                        Spacer(Modifier.height(16.dp))
                                        Text("Sedang merangkum...", color = GlassTheme.colors.TextSecond)
                                    }
                                }
                                is NetworkResult.Success -> {
                                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                        MarkdownText(aiState.data)
                                    }
                                }
                                is NetworkResult.Error -> {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text("Gagal memproses", color = GlassTheme.colors.Pink, fontWeight = FontWeight.Bold)
                                        Text(aiState.message ?: "Terjadi kesalahan", fontSize = 12.sp, color = GlassTheme.colors.TextMuted)
                                        Spacer(Modifier.height(16.dp))
                                        Button(onClick = { viewModel.summarizeNote(note.content) }) {
                                            Text("Coba Lagi")
                                        }
                                    }
                                }
                                null -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AiInsightCard(
    insight: AiInsightResponse,
    onActionClick: (AiAction) -> Unit = {},
    onApplyTheme: (org.garis.pam.data.model.remote.AiTheme) -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(GlassTheme.colors.Teal.copy(alpha = 0.05f))
            .border(1.dp, GlassTheme.colors.Teal.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(GlassTheme.colors.Teal.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("💡", fontSize = 16.sp)
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    insight.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = GlassTheme.colors.TextPrimary
                )
            }

            // AI Theme Suggestion
            insight.suggestedTheme?.let { theme ->
                Button(
                    onClick = { 
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onApplyTheme(theme)
                    },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GlassTheme.colors.Violet.copy(alpha = 0.1f),
                        contentColor = GlassTheme.colors.Violet
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, GlassTheme.colors.Violet.copy(alpha = 0.3f))
                ) {
                    Text("🎨 Pakai Tema", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Key Insights
        insight.insights.forEach { point ->
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text("•", color = GlassTheme.colors.Teal, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                Text(
                    point,
                    fontSize = 13.sp,
                    color = GlassTheme.colors.TextSecond,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Action Chips
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            insight.actions.forEach { action ->
                val chipColor = when(action.type) {
                    "TODO" -> GlassTheme.colors.Violet
                    "CALENDAR" -> GlassTheme.colors.Pink
                    "SEARCH" -> GlassTheme.colors.Sky
                    else -> GlassTheme.colors.Teal
                }

                Surface(
                    onClick = { 
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onActionClick(action)
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = chipColor.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, chipColor.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val icon = when(action.type) {
                            "TODO" -> "✅"
                            "CALENDAR" -> "📅"
                            "SEARCH" -> "🔍"
                            else -> "⚡"
                        }
                        Text(icon, fontSize = 12.sp)
                        Spacer(Modifier.width(6.dp))
                        Text(
                            action.label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = chipColor
                        )
                    }
                }
            }
        }
    }
}
