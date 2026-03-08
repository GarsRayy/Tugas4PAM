package org.garis.pam

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.layout.ContentScale
import tugas3_profileapp.composeapp.generated.resources.Res
import tugas3_profileapp.composeapp.generated.resources.profil


// ╔══════════════════════════════════════════╗
//  COMPOSABLE 1 — HeroSection
//  Padanan: <div class="hero"> di HTML
//  Berisi: gradient bg + grid overlay + back btn
//          + badge + foto profil (Image) + nama
// ╚══════════════════════════════════════════╝
@Composable
fun HeroSection(
    name: String,
    badge: String,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    // Animasi mesh pulse (opacity 0.8 → 1.0)
    val infiniteTransition = rememberInfiniteTransition(label = "mesh")
    val meshAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue  = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "meshAlpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp) // Sedikit ditinggikan agar foto muat
            .clipToBounds()
    ) {
        // ── Layer 1: hero-bg gradient ──
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colorStops = arrayOf(
                            0.0f to Color(0xFF7C6EFA).copy(alpha = 0.6f),
                            0.4f to Color(0xFFF472B6).copy(alpha = 0.4f),
                            0.8f to Color(0xFF2DD4BF).copy(alpha = 0.3f),
                            1.0f to Color.Transparent
                        ),
                        start = Offset(0f, 0f),
                        end   = Offset(1000f, 1000f)
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(GlassTheme.BgHeroTop, GlassTheme.BgPhone)
                    )
                )
        )

        // ── Layer 2: hero-mesh radial gradients ──
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = meshAlpha }
        ) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF7C6EFA).copy(alpha = 0.45f), Color.Transparent),
                    center = Offset(size.width * 0.30f, size.height * 0.40f),
                    radius = size.width * 0.5f
                )
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFF472B6).copy(alpha = 0.35f), Color.Transparent),
                    center = Offset(size.width * 0.75f, size.height * 0.60f),
                    radius = size.width * 0.45f
                )
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF38BDF8).copy(alpha = 0.30f), Color.Transparent),
                    center = Offset(size.width * 0.55f, size.height * 0.25f),
                    radius = size.width * 0.38f
                )
            )
        }

        // ── Layer 3: hero-grid (garis kotak-kotak tipis) ──
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridSize = 32.dp.toPx()
            val lineColor = Color.White.copy(alpha = 0.04f)
            var x = 0f
            while (x <= size.width) {
                drawLine(lineColor, Offset(x, 0f), Offset(x, size.height), strokeWidth = 1f)
                x += gridSize
            }
            var y = 0f
            while (y <= size.height) {
                drawLine(lineColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
                y += gridSize
            }
        }

        // ── Layer 4: bottom fade ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, GlassTheme.BgPhone.copy(alpha = 0.95f))
                    )
                )
        )

        // ── Back button ──
        Box(
            modifier = Modifier
                .padding(18.dp)
                .size(36.dp)
                .align(Alignment.TopStart)
                .clip(CircleShape)
                .border(1.dp, GlassTheme.GlassBorder, CircleShape)
                .background(Color.White.copy(alpha = 0.08f))
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Text("‹", fontSize = 20.sp, color = GlassTheme.TextPrimary)
        }

        // ── Profil Info (Foto + Badge + Nama) ──
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(700)) + slideInVertically(tween(700)) { 16 },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 22.dp, end = 22.dp, bottom = 18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                // ── MENGGUNAKAN KOMPONEN IMAGE (Syarat Tugas) ──
                Image(
                    painter = painterResource(Res.drawable.profil),
                    contentDescription = "Foto Profil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White.copy(alpha = 0.6f), CircleShape)
                )

                // Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.10f))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = badge,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = GlassTheme.Gold,
                        letterSpacing = 0.5.sp
                    )
                }

                // Nama + verified tick
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = name,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = GlassTheme.TextPrimary,
                        lineHeight = 32.sp
                    )
                    Spacer(Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(GlassTheme.Violet, GlassTheme.Sky)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("✓", fontSize = 10.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

// ╔══════════════════════════════════════════╗
//  COMPOSABLE 2 — StatsAndActions
//  Padanan: .stats-row + .profile-subtitle
//           + .action-row di HTML
// ╚══════════════════════════════════════════╝
@Composable
fun StatsAndActions(
    subtitle: String,
    onMessageClick: () -> Unit = {},
    onAddContactClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 20.dp)) {

        // ── Stats row ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem("Smtr 6", "Semester")
            // Divider vertikal
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(36.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                GlassTheme.GlassBorder,
                                Color.Transparent
                            )
                        )
                    )
            )
            StatItem("KMP", "Spesialisasi")
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(36.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                GlassTheme.GlassBorder,
                                Color.Transparent
                            )
                        )
                    )
            )
            StatItem("ITERA", "Kampus")
        }

        // Garis bawah stats
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(GlassTheme.GlassBorder2)
        )

        // ── Subtitle ──
        Text(
            text = subtitle,
            fontSize = 13.sp,
            color = GlassTheme.TextSecond,
            textAlign = TextAlign.Center,
            letterSpacing = 0.2.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp)
        )

        // ── Action buttons ──
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // btn-primary: gradient violet → pink
            Button(
                onClick = onMessageClick,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                listOf(GlassTheme.Violet, GlassTheme.Pink)
                            ),
                            RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "✉ Kirim Pesan",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            // btn-secondary: glass
            OutlinedButton(
                onClick = onAddContactClick,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, GlassTheme.GlassBorder),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = GlassTheme.GlassBg
                )
            ) {
                Text(
                    "⊕ Tambah Kontak",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = GlassTheme.TextPrimary
                )
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = GlassTheme.TextPrimary
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = GlassTheme.TextMuted,
            letterSpacing = 0.4.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

// ╔══════════════════════════════════════════╗
//  COMPOSABLE 3 — ContactSection
//  Padanan: 2-col mini cards (Email+Phone)
//           + full-width Location card
// ╚══════════════════════════════════════════╝
@Composable
fun ContactSection(
    email: String,
    phone: String,
    location: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 18.dp)) {

        SectionLabel("Informasi Kontak")

        // ── 2-col grid: Email + Phone ──
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Email card
            GlassCard(modifier = Modifier.weight(1f)) {
                // Icon box violet
                GlassIconBox(
                    gradientColors = listOf(
                        Color(0xFF7C6EFA).copy(alpha = 0.35f),
                        Color(0xFFA78BFA).copy(alpha = 0.20f)
                    ),
                    blobColor = Color(0xFF7C6EFA).copy(alpha = 0.5f)
                ) {
                    // Mail icon shape
                    Canvas(modifier = Modifier.size(22.dp)) {
                        val w = size.width; val h = size.height
                        // kotak mail
                        drawRoundRect(
                            brush = Brush.linearGradient(
                                listOf(Color(0xFFA78BFA), Color(0xFF7C6EFA))
                            ),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
                        )
                        // garis V di atas
                        drawLine(
                            Color.White, Offset(0f, h * 0.25f),
                            Offset(w / 2f, h * 0.65f), strokeWidth = 2f
                        )
                        drawLine(
                            Color.White, Offset(w / 2f, h * 0.65f),
                            Offset(w, h * 0.25f), strokeWidth = 2f
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    "EMAIL",
                    fontSize = 10.sp, fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.75f), letterSpacing = 0.8.sp
                )
                Text(
                    email,
                    fontSize = 12.sp, fontWeight = FontWeight.Medium,
                    color = Color.White,
                    lineHeight = 18.sp
                )
            }

            // Phone card
            GlassCard(modifier = Modifier.weight(1f)) {
                // Icon box teal
                GlassIconBox(
                    gradientColors = listOf(
                        Color(0xFF2DD4BF).copy(alpha = 0.35f),
                        Color(0xFF38BDF8).copy(alpha = 0.20f)
                    ),
                    blobColor = Color(0xFF2DD4BF).copy(alpha = 0.5f)
                ) {
                    Canvas(modifier = Modifier.size(22.dp)) {
                        val w = size.width; val h = size.height
                        drawRoundRect(
                            brush = Brush.linearGradient(
                                listOf(Color(0xFF2DD4BF), Color(0xFF38BDF8))
                            ),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
                        )
                        drawCircle(Color.White, radius = h * 0.08f,
                            center = Offset(w / 2f, h * 0.82f))
                        drawLine(
                            Color.White.copy(alpha = 0.6f),
                            Offset(w * 0.35f, h * 0.22f),
                            Offset(w * 0.65f, h * 0.22f), strokeWidth = h * 0.10f
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    "TELEPON",
                    fontSize = 10.sp, fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.75f), letterSpacing = 0.8.sp
                )
                Text(
                    phone,
                    fontSize = 12.sp, fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // ── Location full-width card ──
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                GlassIconBox(
                    size = 44.dp, corner = 14.dp,
                    gradientColors = listOf(
                        Color(0xFFF472B6).copy(alpha = 0.35f),
                        Color(0xFFFB923C).copy(alpha = 0.20f)
                    ),
                    blobColor = Color(0xFFF472B6).copy(alpha = 0.5f)
                ) {
                    // Pin location icon
                    Canvas(modifier = Modifier.size(20.dp)) {
                        val cx = size.width / 2f
                        val path = androidx.compose.ui.graphics.Path().apply {
                            moveTo(cx, size.height)
                            cubicTo(
                                cx - size.width * 0.5f, size.height * 0.65f,
                                0f, size.height * 0.45f,
                                cx, size.height * 0.08f
                            )
                            cubicTo(
                                size.width, size.height * 0.45f,
                                cx + size.width * 0.5f, size.height * 0.65f,
                                cx, size.height
                            )
                        }
                        drawPath(
                            path,
                            brush = Brush.linearGradient(
                                listOf(Color(0xFFF472B6), Color(0xFFFB923C))
                            )
                        )
                        drawCircle(
                            Color.White,
                            radius = size.width * 0.16f,
                            center = Offset(cx, size.height * 0.38f)
                        )
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "LOKASI",
                        fontSize = 10.sp, fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.75f), letterSpacing = 1.sp
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        location,
                        fontSize = 14.sp, fontWeight = FontWeight.Medium,
                        color = Color.White,
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
                Text("›", fontSize = 18.sp, color = GlassTheme.TextMuted)
            }
        }
    }
}

// ╔══════════════════════════════════════════╗
//  COMPOSABLE 4 — BioCard
//  Padanan: <div class="gcard"> bio
//           AnimatedVisibility (BONUS +10%)
// ╚══════════════════════════════════════════╝
@Composable
fun BioCard(bio: String, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(true) }
    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 0f else 180f,
        animationSpec = tween(300),
        label = "arrow"
    )

    Column(modifier = modifier.padding(horizontal = 18.dp)) {
        SectionLabel("Tentang Saya")

        GlassCard(modifier = Modifier.fillMaxWidth()) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Person icon box violet→pink
                    GlassIconBox(
                        gradientColors = listOf(
                            Color(0xFF7C6EFA).copy(alpha = 0.30f),
                            Color(0xFFF472B6).copy(alpha = 0.20f)
                        ),
                        blobColor = Color(0xFFA78BFA).copy(alpha = 0.4f)
                    ) {
                        Canvas(modifier = Modifier.size(18.dp)) {
                            val cx = size.width / 2f
                            // kepala
                            drawCircle(
                                brush = Brush.linearGradient(
                                    listOf(Color(0xFFA78BFA), Color(0xFFF472B6))
                                ),
                                radius = size.width * 0.28f,
                                center = Offset(cx, size.height * 0.30f)
                            )
                            // badan
                            val bodyPath = androidx.compose.ui.graphics.Path().apply {
                                moveTo(0f, size.height)
                                cubicTo(
                                    0f, size.height * 0.60f,
                                    cx - size.width * 0.15f, size.height * 0.55f,
                                    cx, size.height * 0.55f
                                )
                                cubicTo(
                                    cx + size.width * 0.15f, size.height * 0.55f,
                                    size.width, size.height * 0.60f,
                                    size.width, size.height
                                )
                            }
                            drawPath(
                                bodyPath,
                                brush = Brush.linearGradient(
                                    listOf(Color(0xFFA78BFA), Color(0xFFF472B6))
                                )
                            )
                        }
                    }
                    Text(
                        "Bio",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = GlassTheme.TextPrimary
                    )
                }

                // Toggle button
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .border(1.dp, GlassTheme.GlassBorder, CircleShape)
                        .background(GlassTheme.GlassBg)
                        .clickable { expanded = !expanded },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "▲",
                        fontSize = 10.sp,
                        color = GlassTheme.TextMuted,
                        modifier = Modifier.graphicsLayer { rotationZ = arrowRotation }
                    )
                }
            }

            // BONUS: AnimatedVisibility expand/collapse
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                exit  = fadeOut(tween(300)) + shrinkVertically(tween(300))
            ) {
                Column {
                    GlassDivider()
                    Text(
                        bio,
                        fontSize = 13.5.sp,
                        lineHeight = 22.sp,
                        color = GlassTheme.TextSecond,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(Modifier.height(12.dp))
                    // Skill tags
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Kotlin", "Compose MP", "KMP", "Mobile Dev", "UI/UX")
                            .forEach { GlassTag(it) }
                    }
                }
            }
        }
    }
}

// ╔══════════════════════════════════════════╗
//  COMPOSABLE 5 — SkillsGrid
//  Padanan: <div class="skills-grid"> 3-col
// ╚══════════════════════════════════════════╝
@Composable
fun SkillsGrid(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 18.dp)) {
        SectionLabel("Tech Stack")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Kotlin chip
            SkillChip(
                modifier = Modifier.weight(1f),
                label = "Kotlin",
                gradientColors = listOf(
                    Color(0xFF7C6EFA).copy(alpha = 0.35f),
                    Color(0xFFF472B6).copy(alpha = 0.20f)
                )
            ) {
                // Kotlin K logo shape
                Canvas(modifier = Modifier.size(22.dp)) {
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(0f, 0f)
                        lineTo(size.width, 0f)
                        lineTo(size.width / 2f, size.height / 2f)
                        lineTo(size.width, size.height)
                        lineTo(0f, size.height)
                        close()
                    }
                    drawPath(
                        path,
                        brush = Brush.linearGradient(
                            listOf(Color(0xFFA78BFA), Color(0xFFF472B6))
                        )
                    )
                }
            }

            // Compose chip
            SkillChip(
                modifier = Modifier.weight(1f),
                label = "Compose",
                gradientColors = listOf(
                    Color(0xFF38BDF8).copy(alpha = 0.35f),
                    Color(0xFF2DD4BF).copy(alpha = 0.20f)
                )
            ) {
                Canvas(modifier = Modifier.size(22.dp)) {
                    // hexagon
                    val cx = size.width / 2f; val cy = size.height / 2f
                    val r = size.width / 2f
                    val path = androidx.compose.ui.graphics.Path().apply {
                        for (i in 0..5) {
                            val angle = ((60.0 * i) - 30.0) * (PI / 180.0) // Rumus manual toRadians
                            val x = (cx + r * cos(angle)).toFloat()
                            val y = (cy + r * sin(angle)).toFloat()
                            if (i == 0) moveTo(x, y) else lineTo(x, y)
                        }
                        close()
                    }
                    drawPath(
                        path,
                        brush = Brush.linearGradient(
                            listOf(Color(0xFF38BDF8), Color(0xFF2DD4BF))
                        )
                    )
                    drawCircle(Color.White.copy(alpha = 0.9f), radius = size.width * 0.15f)
                }
            }

            // ITERA chip
            SkillChip(
                modifier = Modifier.weight(1f),
                label = "ITERA",
                gradientColors = listOf(
                    Color(0xFFFCD34D).copy(alpha = 0.35f),
                    Color(0xFFFB923C).copy(alpha = 0.20f)
                )
            ) {
                Canvas(modifier = Modifier.size(22.dp)) {
                    // rumah / gedung kampus
                    val path = androidx.compose.ui.graphics.Path().apply {
                        moveTo(0f, size.height)
                        lineTo(0f, size.height * 0.45f)
                        lineTo(size.width / 2f, 0f)
                        lineTo(size.width, size.height * 0.45f)
                        lineTo(size.width, size.height)
                        close()
                    }
                    drawPath(
                        path,
                        brush = Brush.linearGradient(
                            listOf(Color(0xFFFCD34D), Color(0xFFFB923C))
                        )
                    )
                    drawRect(
                        Color.White.copy(alpha = 0.7f),
                        topLeft = Offset(size.width * 0.35f, size.height * 0.58f),
                        size = androidx.compose.ui.geometry.Size(
                            size.width * 0.30f, size.height * 0.42f
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SkillChip(
    modifier: Modifier = Modifier,
    label: String,
    gradientColors: List<Color>,
    icon: @Composable () -> Unit
) {
    GlassCard(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
        ) {
            GlassIconBox(
                size = 40.dp,
                corner = 12.dp,
                gradientColors = gradientColors,
                blobColor = gradientColors.first()
            ) { icon() }
            Spacer(Modifier.height(8.dp))
            Text(
                label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = GlassTheme.TextSecond,
                letterSpacing = 0.3.sp
            )
        }
    }
}

// ╔══════════════════════════════════════════╗
//  COMPOSABLE 6 — BottomNav
//  Padanan: <div class="bottom-nav"> di HTML
// ╚══════════════════════════════════════════╝
@Composable
fun BottomNav(modifier: Modifier = Modifier) {
    var selected by remember { mutableStateOf(0) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(GlassTheme.BgPhone.copy(alpha = 0.6f))
            .border(
                width = 1.dp,
                color = GlassTheme.GlassBorder2,
                shape = RoundedCornerShape(0.dp)
            )
            .padding(horizontal = 8.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val navItems = listOf("🏠" to "Home", "🔍" to "Cari", "❤" to "Simpan", "☰" to "Menu")
        navItems.forEachIndexed { index, (icon, label) ->
            NavItem(
                icon = icon,
                label = label,
                isActive = selected == index,
                onClick = { selected = index }
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: String,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .then(
                    if (isActive) Modifier.background(
                        Brush.linearGradient(
                            listOf(
                                GlassTheme.Violet.copy(alpha = 0.4f),
                                GlassTheme.Lavender.copy(alpha = 0.2f)
                            )
                        )
                    ).border(
                        1.dp,
                        GlassTheme.Violet.copy(alpha = 0.35f),
                        RoundedCornerShape(10.dp)
                    )
                    else Modifier.background(GlassTheme.GlassBg)
                        .border(1.dp, GlassTheme.GlassBorder2, RoundedCornerShape(10.dp))
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 16.sp)
        }
        Spacer(Modifier.height(4.dp))
        Text(
            label,
            fontSize = 10.sp,
            color = if (isActive) GlassTheme.Lavender else GlassTheme.TextMuted,
            letterSpacing = 0.3.sp
        )
    }
}

// ════════════════════════════════════════════
//  PRIVATE HELPERS — shared building blocks
// ════════════════════════════════════════════

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, GlassTheme.GlassBorder, RoundedCornerShape(18.dp))
            .background(GlassTheme.GlassBg)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun GlassIconBox(
    modifier: Modifier = Modifier,
    size: Dp = 38.dp,
    corner: Dp = 12.dp,
    gradientColors: List<Color>,
    blobColor: Color,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(corner))
            .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(corner))
            .background(
                Brush.linearGradient(gradientColors)
            ),
        contentAlignment = Alignment.Center
    ) {
        // Blob cahaya di pojok kiri atas (efek glassmorphism icon)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(blobColor, Color.Transparent),
                        center = Offset(0f, 0f),
                        radius = size.value * 2.5f
                    )
                )
        )
        content()
    }
}

@Composable
fun GlassDivider() {
    Spacer(Modifier.height(10.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(GlassTheme.GlassBorder2)
    )
    Spacer(Modifier.height(10.dp))
}

@Composable
private fun GlassTag(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, GlassTheme.GlassBorder, RoundedCornerShape(20.dp))
            .background(GlassTheme.GlassBg)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = GlassTheme.TextSecond
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = GlassTheme.TextMuted,
        letterSpacing = 1.2.sp,
        modifier = Modifier.padding(bottom = 12.dp, top = 4.dp)
    )
}