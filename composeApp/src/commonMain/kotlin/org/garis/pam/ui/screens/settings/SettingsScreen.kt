package org.garis.pam.ui.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import org.garis.pam.platform.BatteryInfo
import org.garis.pam.platform.DeviceInfo
import org.garis.pam.viewmodel.SettingsViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit,
    deviceInfo: DeviceInfo = koinInject(),
    batteryInfo: BatteryInfo = koinInject()
) {
    val currentTheme by viewModel.currentTheme.collectAsState()
    val currentSortOrder by viewModel.currentSortOrder.collectAsState()
    val isBiometricEnabled by viewModel.isBiometricEnabled.collectAsState()
    val hiddenPassword by viewModel.hiddenPassword.collectAsState()

    var showPasswordDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan", color = GlassTheme.colors.TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = GlassTheme.colors.TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GlassTheme.colors.BgPage
                )
            )
        },
        containerColor = GlassTheme.colors.BgPage
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Informasi Perangkat",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GlassTheme.colors.TextSecond,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(GlassTheme.colors.GlassBg)
                    .padding(16.dp)
            ) {
                InfoRow(label = "Model", value = deviceInfo.getDeviceName())
                InfoRow(label = "Versi OS", value = deviceInfo.getOsVersion())
                InfoRow(label = "Versi App", value = deviceInfo.getAppVersion())
                
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = GlassTheme.colors.GlassBorder2,
                    thickness = 0.5.dp
                )
                
                val batteryLevel = batteryInfo.getBatteryLevel()
                val isCharging = batteryInfo.isCharging()
                InfoRow(
                    label = "Baterai", 
                    value = "$batteryLevel% ${if (isCharging) "(Mengisi daya)" else ""}"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

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

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Keamanan & Privasi",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = GlassTheme.colors.TextSecond,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(GlassTheme.colors.GlassBg)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Kunci Biometrik", color = GlassTheme.colors.TextPrimary)
                Switch(
                    checked = isBiometricEnabled,
                    onCheckedChange = { viewModel.setBiometricEnabled(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = GlassTheme.colors.Violet
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(GlassTheme.colors.GlassBg)
                    .clickable { showPasswordDialog = true }
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        if (hiddenPassword.isEmpty()) "Set Password Catatan Tersembunyi" else "Ubah Password Catatan Tersembunyi",
                        color = GlassTheme.colors.TextPrimary
                    )
                    Text("›", color = GlassTheme.colors.TextMuted, fontSize = 18.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showPasswordDialog) {
        var passwordInput by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Password Hidden Folder", color = GlassTheme.colors.TextPrimary) },
            text = {
                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { passwordInput = it },
                    label = { Text("Password Baru") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setHiddenPassword(passwordInput)
                    showPasswordDialog = false
                }) {
                    Text("Simpan", color = GlassTheme.colors.Violet)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) {
                    Text("Batal", color = GlassTheme.colors.TextSecond)
                }
            },
            containerColor = GlassTheme.colors.BgPhone,
            shape = RoundedCornerShape(24.dp)
        )
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

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = GlassTheme.colors.TextSecond, fontSize = 14.sp)
        Text(value, color = GlassTheme.colors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
