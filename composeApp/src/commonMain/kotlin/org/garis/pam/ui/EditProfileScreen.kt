package org.garis.pam.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.garis.pam.GlassTheme

// ── Stateless TextField (state hoisting sesuai materi hal 30) ──
@Composable
fun GlassTextField(
    label: String,
    value: String,                    // state dari parent/ViewModel
    onValueChange: (String) -> Unit,  // callback ke ViewModel
    modifier: Modifier = Modifier,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = GlassTheme.colors.TextMuted, fontSize = 12.sp) },
        modifier = modifier.fillMaxWidth(),
        maxLines = maxLines,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor    = GlassTheme.colors.TextPrimary,
            unfocusedTextColor  = GlassTheme.colors.TextPrimary,
            focusedBorderColor  = GlassTheme.colors.Violet,
            unfocusedBorderColor= GlassTheme.colors.GlassBorder,
            cursorColor         = GlassTheme.colors.Violet,
            focusedContainerColor   = GlassTheme.colors.GlassBg,
            unfocusedContainerColor = GlassTheme.colors.GlassBg
        )
    )
}

// ── Edit Profile Screen ──
@Composable
fun EditProfileScreen(
    editName: String,
    editBio: String,
    editEmail: String,
    editPhone: String,
    editLocation: String,
    onNameChange: (String) -> Unit,   // state hoisting ke ViewModel
    onBioChange: (String) -> Unit,    // state hoisting ke ViewModel
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(GlassTheme.colors.BgPage, GlassTheme.colors.BgPhone)
                )
            )
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.height(40.dp))

        // Judul
        Text(
            "✏  Edit Profil",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = GlassTheme.colors.TextPrimary
        )
        Text(
            "Ubah informasi profil Anda secara lengkap",
            fontSize = 13.sp,
            color = GlassTheme.colors.TextSecond
        )

        Spacer(Modifier.height(8.dp))

        // TextField Nama — stateless, state dihoisting ke ViewModel
        GlassTextField(
            label = "Nama Lengkap",
            value = editName,
            onValueChange = onNameChange
        )

        // TextField Bio — maxLines 5 supaya bisa multi-line
        GlassTextField(
            label = "Bio",
            value = editBio,
            onValueChange = onBioChange,
            maxLines = 3
        )

        GlassTextField(
            label = "Email",
            value = editEmail,
            onValueChange = onEmailChange
        )

        GlassTextField(
            label = "Telepon",
            value = editPhone,
            onValueChange = onPhoneChange
        )

        GlassTextField(
            label = "Lokasi",
            value = editLocation,
            onValueChange = onLocationChange
        )

        Spacer(Modifier.height(8.dp))

        // Tombol Simpan & Batal
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Batal
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(14.dp),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Text("Batal", color = GlassTheme.colors.TextSecond)
            }

            // Simpan — gradient violet → pink
            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GlassTheme.colors.Violet
                )
            ) {
                Text("Simpan", fontWeight = FontWeight.SemiBold, color = GlassTheme.colors.TextPrimary)
            }
        }
    }
}