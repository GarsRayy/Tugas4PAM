package org.garis.pam.ui.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.garis.pam.GlassTheme
import org.garis.pam.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val currentTheme by viewModel.currentTheme.collectAsState()
    val currentSortOrder by viewModel.currentSortOrder.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Tema Aplikasi",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = GlassTheme.colors.TextSecond,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Tema Options
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ThemeOptionItem(
                label = "Aurora Glass (Premium)",
                isSelected = currentTheme == "aurora_glass",
                onClick = { viewModel.changeTheme("aurora_glass") }
            )
            ThemeOptionItem(
                label = "Gelap (Dark Mode)",
                isSelected = currentTheme == "dark",
                onClick = { viewModel.changeTheme("dark") }
            )
            ThemeOptionItem(
                label = "Terang (Light Mode)",
                isSelected = currentTheme == "light",
                onClick = { viewModel.changeTheme("light") }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Urutan Catatan",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = GlassTheme.colors.TextSecond,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SortOptionButton(
                label = "Terbaru",
                isSelected = currentSortOrder == "newest",
                modifier = Modifier.weight(1f),
                onClick = { viewModel.changeSortOrder("newest") }
            )
            SortOptionButton(
                label = "Terlama",
                isSelected = currentSortOrder == "oldest",
                modifier = Modifier.weight(1f),
                onClick = { viewModel.changeSortOrder("oldest") }
            )
        }
    }
}

@Composable
fun ThemeOptionItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) GlassTheme.colors.Violet.copy(alpha = 0.2f) else GlassTheme.colors.GlassBg)
            .border(
                1.dp,
                if (isSelected) GlassTheme.colors.Violet else GlassTheme.colors.GlassBorder2,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, color = GlassTheme.colors.TextPrimary, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
            if (isSelected) {
                Text("✓", color = GlassTheme.colors.Violet, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun SortOptionButton(label: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) GlassTheme.colors.Violet else GlassTheme.colors.GlassBg
        ),
        border = BorderStroke(1.dp, if (isSelected) GlassTheme.colors.Violet else GlassTheme.colors.GlassBorder2)
    ) {
        Text(
            label,
            color = if (isSelected) Color.White else GlassTheme.colors.TextPrimary,
            fontSize = 13.sp
        )
    }
}
