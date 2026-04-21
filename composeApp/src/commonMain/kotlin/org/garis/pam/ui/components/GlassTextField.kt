package org.garis.pam.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.garis.pam.GlassTheme

@Composable
fun GlassTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    maxLines: Int = 1
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = GlassTheme.colors.TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            maxLines = maxLines,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GlassTheme.colors.Violet,
                unfocusedBorderColor = GlassTheme.colors.GlassBorder2,
                focusedContainerColor = GlassTheme.colors.GlassBg,
                unfocusedContainerColor = GlassTheme.colors.GlassBg,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
    }
}
