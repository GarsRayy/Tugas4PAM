package org.garis.pam.ui.screens.notes

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import org.garis.pam.ui.components.GlassTextField
import org.garis.pam.ui.components.MarkdownText
import org.garis.pam.viewmodel.NoteViewModel

@Composable
fun AddEditNoteScreen(
    viewModel: NoteViewModel,
    onBack: () -> Unit
) {
    val selectedNote by viewModel.selectedNote.collectAsState()
    
    var titleState by remember(selectedNote) { mutableStateOf(selectedNote?.title ?: "") }
    var contentState by remember(selectedNote) { mutableStateOf(selectedNote?.content ?: "") }
    var tagsState by remember(selectedNote) { mutableStateOf(selectedNote?.tags ?: "") }
    var colorNameState by remember(selectedNote) { mutableStateOf(selectedNote?.color_name ?: "VIOLET") }
    
    var isPreviewMode by remember { mutableStateOf(false) }

    val colors = listOf("VIOLET", "TEAL", "PINK", "GOLD", "SKY")
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(GlassTheme.colors.BgPage, GlassTheme.colors.BgPhone)
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(20.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
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

            Text(
                if (selectedNote != null) "✏ Edit Catatan" else "📝 Catatan Baru",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = GlassTheme.colors.TextPrimary
            )

            Spacer(Modifier.size(40.dp))
        }

        Spacer(Modifier.height(8.dp))

        // TextField Judul
        GlassTextField(
            label = "Judul",
            value = titleState,
            onValueChange = { titleState = it }
        )

        // Tags Input
        GlassTextField(
            label = "Tags (contoh: #Kerja #Ide)",
            value = tagsState,
            onValueChange = { tagsState = it }
        )

        // Mode Switcher (Edit vs Preview)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                if (isPreviewMode) "👁 Preview Mode" else "✏ Edit Mode",
                fontSize = 12.sp,
                color = GlassTheme.colors.Violet,
                fontWeight = FontWeight.Bold
            )
            Switch(
                checked = isPreviewMode,
                onCheckedChange = { isPreviewMode = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = GlassTheme.colors.Violet,
                    checkedTrackColor = GlassTheme.colors.Violet.copy(alpha = 0.3f)
                ),
                modifier = Modifier.scale(0.8f)
            )
        }

        if (isPreviewMode) {
            // Markdown Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(GlassTheme.colors.GlassBg)
                    .border(1.dp, GlassTheme.colors.GlassBorder2, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                MarkdownText(contentState)
            }
        } else {
            // TextField Isi
            GlassTextField(
                label = "Isi catatan (mendukung **tebal** dan - list)",
                value = contentState,
                onValueChange = { contentState = it },
                maxLines = 12
            )
        }

        Spacer(Modifier.height(8.dp))
        
        // Color Picker
        Text("Pilih Warna", color = GlassTheme.colors.TextPrimary, fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            colors.forEach { colorName ->
                val color = when(colorName) {
                    "VIOLET" -> GlassTheme.colors.Violet
                    "TEAL" -> GlassTheme.colors.Teal
                    "PINK" -> GlassTheme.colors.Pink
                    "GOLD" -> GlassTheme.colors.Gold
                    "SKY" -> GlassTheme.colors.Sky
                    else -> Color.Gray
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            if (colorNameState == colorName) 3.dp else 0.dp,
                            Color.White,
                            CircleShape
                        )
                        .clickable { colorNameState = colorName }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Tombol Simpan
        Button(
            onClick = { 
                if (titleState.isNotBlank()) {
                    viewModel.saveNote(titleState, contentState, tagsState, colorNameState)
                    onBack() 
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            listOf(GlassTheme.colors.Violet, GlassTheme.colors.Teal)
                        ),
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Simpan Catatan",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
        
        Spacer(Modifier.height(40.dp))
    }
}
