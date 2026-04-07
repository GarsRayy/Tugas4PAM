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
import org.garis.pam.data.NoteColor
import org.garis.pam.ui.GlassTextField  // reuse dari minggu lalu
import androidx.compose.ui.draw.clip

@Composable
fun AddEditNoteScreen(
    title: String,
    content: String,
    isEditMode: Boolean = false,       // true = edit, false = add baru
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
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
                if (isEditMode) "✏ Edit Catatan" else "📝 Catatan Baru",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = GlassTheme.colors.TextPrimary
            )

            Spacer(Modifier.size(40.dp))
        }

        Spacer(Modifier.height(8.dp))

        // TextField Judul — stateless (state hoisting ke ViewModel)
        GlassTextField(
            label = "Judul",
            value = title,
            onValueChange = onTitleChange
        )

        // TextField Isi — multi-line
        GlassTextField(
            label = "Isi catatan...",
            value = content,
            onValueChange = onContentChange,
            maxLines = 12
        )

        Spacer(Modifier.height(8.dp))

        // Tombol Simpan
        Button(
            onClick = onSave,
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
    }
}