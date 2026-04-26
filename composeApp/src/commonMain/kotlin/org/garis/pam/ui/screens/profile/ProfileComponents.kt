package org.garis.pam.ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.garis.pam.GlassTheme
import org.jetbrains.compose.resources.painterResource
import tugas3_profileapp.composeapp.generated.resources.Res
import tugas3_profileapp.composeapp.generated.resources.profil
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.cos


// ╔══════════════════════════════════════════╗
//  COMPOSABLE 1 — HeroSection
// ╚══════════════════════════════════════════╝
@Composable
fun HeroSection(
    name: String,
    badge: String,
    profileImage: String? = null,
    onImageClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    val infiniteTransition = rememberInfiniteTransition(label = "hero")

    val particleTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue  = 1f,
        animationSpec = infiniteRepeatable(
            tween(12000, easing = LinearEasing),
            RepeatMode.Restart
        ), label = "particleTime"
    )

    val dotPulse by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue  = 1f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ), label = "dotPulse"
    )

    val particleSeeds = remember {
        listOf(
            Triple(0.10f, 0.20f, 0.0f),  // x%, y%, phase offset
            Triple(0.75f, 0.15f, 0.3f),
            Triple(0.40f, 0.60f, 0.6f),
            Triple(0.85f, 0.70f, 0.1f),
            Triple(0.20f, 0.80f, 0.8f),
            Triple(0.60f, 0.10f, 0.4f),
            Triple(0.30f, 0.40f, 0.2f),
            Triple(0.90f, 0.40f, 0.7f),
            Triple(0.50f, 0.85f, 0.5f),
            Triple(0.15f, 0.55f, 0.9f),
            Triple(0.68f, 0.45f, 0.15f),
            Triple(0.45f, 0.30f, 0.65f),
            Triple(0.80f, 0.90f, 0.35f),
            Triple(0.05f, 0.75f, 0.85f),
        )
    }

    val particleColors = remember {
        listOf(
            Color(0xFF7C6EFA), Color(0xFFA78BFA), Color(0xFFF472B6),
            Color(0xFF2DD4BF), Color(0xFF38BDF8), Color(0xFFFCD34D),
            Color(0xFF7C6EFA), Color(0xFF2DD4BF), Color(0xFFF472B6),
            Color(0xFFA78BFA), Color(0xFF38BDF8), Color(0xFFFCD34D),
            Color(0xFF7C6EFA), Color(0xFF2DD4BF),
        )
    }

    Box(
        modifier = modifier.fillMaxWidth().height(300.dp).clipToBounds()
    ) {
        Box(modifier = Modifier.fillMaxSize()
            .background(Brush.verticalGradient(
                listOf(GlassTheme.colors.BgHeroTop, GlassTheme.colors.BgPhone)))
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            val positions = particleSeeds.mapIndexed { i, (xSeed, ySeed, phase) ->
                val t = (particleTime + phase) % 1f
                val dx = sin(t * 2 * PI.toFloat()) * 30f
                val dy = cos(t * 2 * PI.toFloat() * 0.7f) * 20f
                Offset(xSeed * w + dx, ySeed * h + dy)
            }

            for (i in positions.indices) {
                for (j in i + 1 until positions.size) {
                    val dist = (positions[i] - positions[j]).getDistance()
                    val maxDist = w * 0.30f
                    if (dist < maxDist) {
                        val alpha = (1f - dist / maxDist) * 0.25f
                        drawLine(
                            color       = particleColors[i].copy(alpha = alpha),
                            start       = positions[i],
                            end         = positions[j],
                            strokeWidth = 1f
                        )
                    }
                }
            }

            positions.forEachIndexed { i, pos ->
                drawCircle(
                    color  = particleColors[i].copy(alpha = 0.15f * dotPulse),
                    radius = 10f,
                    center = pos
                )
                drawCircle(
                    color  = particleColors[i].copy(alpha = 0.7f * dotPulse),
                    radius = 3f,
                    center = pos
                )
            }

            val gridStep = 36f
            var gx = 0f
            while (gx < w) {
                var gy = 0f
                while (gy < h) {
                    drawCircle(
                        color  = Color.White.copy(alpha = 0.04f),
                        radius = 1f,
                        center = Offset(gx, gy)
                    )
                    gy += gridStep
                }
                gx += gridStep
            }
        }

        Box(modifier = Modifier.fillMaxWidth().height(120.dp)
            .align(Alignment.BottomCenter)
            .background(Brush.verticalGradient(
                listOf(Color.Transparent,
                    GlassTheme.colors.BgPhone.copy(alpha = 0.95f))))
        )

        Box(modifier = Modifier.padding(18.dp).size(36.dp)
            .align(Alignment.TopStart)
            .clip(CircleShape)
            .border(1.dp, GlassTheme.colors.GlassBorder, CircleShape)
            .background(Color.White.copy(alpha = 0.08f))
            .clickable { },
            contentAlignment = Alignment.Center
        ) { Text("‹", fontSize = 20.sp, color = GlassTheme.colors.TextPrimary) }

        AnimatedVisibility(
            visible = visible,
            enter   = fadeIn(tween(700)) + slideInVertically(tween(700)) { 16 },
            modifier = Modifier.align(Alignment.BottomStart)
                .padding(start = 22.dp, end = 22.dp, bottom = 18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clickable { onImageClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            brush = Brush.sweepGradient(
                                listOf(
                                    Color(0xFF7C6EFA), Color(0xFFF472B6),
                                    Color(0xFF2DD4BF), Color(0xFFFCD34D),
                                    Color(0xFF7C6EFA)
                                )
                            )
                        )
                        drawCircle(
                            color = Color(0xFF0B0912),
                            radius = size.minDimension / 2f - 3.dp.toPx()
                        )
                    }

                    if (profileImage == null) {
                        Image(
                            painter = painterResource(Res.drawable.profil),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(84.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(84.dp)
                                .clip(CircleShape)
                                .background(GlassTheme.colors.Violet),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = profileImage,
                                fontSize = 40.sp,
                                color = Color.White
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.BottomEnd)
                            .offset((-6).dp, (-6).dp)
                            .clip(CircleShape)
                            .background(GlassTheme.colors.Green)
                            .border(2.5.dp, Color(0xFF0B0912), CircleShape)
                    )
                }

                Box(modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(20.dp))
                    .background(Color.White.copy(alpha = 0.10f))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(badge, fontSize = 11.sp, fontWeight = FontWeight.Medium,
                        color = GlassTheme.colors.Gold, letterSpacing = 0.5.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(name, fontSize = 28.sp, fontWeight = FontWeight.Bold,
                        color = GlassTheme.colors.TextPrimary, lineHeight = 32.sp)
                    Spacer(Modifier.width(6.dp))
                    Box(modifier = Modifier.size(22.dp).clip(CircleShape)
                        .background(Brush.linearGradient(
                            listOf(GlassTheme.colors.Violet, GlassTheme.colors.Sky))),
                        contentAlignment = Alignment.Center
                    ) { Text("✓", fontSize = 10.sp, color = Color.White) }
                }
            }
        }
    }
}

// ╔══════════════════════════════════════════╗
//  COMPOSABLE 2 — StatsAndActions
// ╚══════════════════════════════════════════╝
@Composable
fun StatsAndActions(
    subtitle: String,
    totalNotes: Int = 0,
    totalFavorites: Int = 0,
    totalNewsRead: Int = 0,
    onSettingsClick: () -> Unit,
    onEditClick: () -> Unit,
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem("$totalNotes", "Catatan")
            Box(modifier = Modifier.width(1.dp).height(36.dp)
                .background(Brush.verticalGradient(
                    listOf(Color.Transparent, GlassTheme.colors.GlassBorder, Color.Transparent))))
            StatItem("$totalFavorites", "Favorit")
            Box(modifier = Modifier.width(1.dp).height(36.dp)
                .background(Brush.verticalGradient(
                    listOf(Color.Transparent, GlassTheme.colors.GlassBorder, Color.Transparent))))
            StatItem("$totalNewsRead", "Berita")
        }

        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(GlassTheme.colors.GlassBorder2))

        Text(
            text = subtitle,
            fontSize = 13.sp, color = GlassTheme.colors.TextSecond,
            textAlign = TextAlign.Center, letterSpacing = 0.2.sp,
            modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onEditClick,
                modifier = Modifier.weight(1f).height(44.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(
                            Brush.linearGradient(listOf(GlassTheme.colors.Violet, GlassTheme.colors.Pink)),
                            RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✏ Edit Profil", fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold, color = GlassTheme.colors.TextPrimary)
                }
            }

            OutlinedButton(
                onClick = onSettingsClick,
                modifier = Modifier.weight(1f).height(44.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, GlassTheme.colors.GlassBorder),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = GlassTheme.colors.GlassBg
                )
            ) {
                Text(
                    "⚙️ Pengaturan",
                    fontSize = 13.sp, color = GlassTheme.colors.TextPrimary
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
            color = GlassTheme.colors.TextPrimary
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = GlassTheme.colors.TextMuted,
            letterSpacing = 0.4.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

// ╔══════════════════════════════════════════╗
//  COMPOSABLE 3 — ContactSection
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            GlassCard(modifier = Modifier.weight(1f)) {
                GlassIconBox(
                    gradientColors = listOf(
                        Color(0xFF7C6EFA).copy(alpha = 0.35f),
                        Color(0xFFA78BFA).copy(alpha = 0.20f)
                    ),
                    blobColor = Color(0xFF7C6EFA).copy(alpha = 0.5f)
                ) {
                    Canvas(modifier = Modifier.size(22.dp)) {
                        val w = size.width; val h = size.height
                        drawRoundRect(
                            brush = Brush.linearGradient(
                                listOf(Color(0xFFA78BFA), Color(0xFF7C6EFA))
                            ),
                            cornerRadius = CornerRadius(6f, 6f)
                        )
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
                    color = GlassTheme.colors.TextPrimary.copy(alpha = 0.75f), letterSpacing = 0.8.sp
                )
                Text(
                    email,
                    fontSize = 12.sp, fontWeight = FontWeight.Medium,
                    color = GlassTheme.colors.TextPrimary,
                    lineHeight = 18.sp
                )
            }

            GlassCard(modifier = Modifier.weight(1f)) {
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
                            cornerRadius = CornerRadius(8f, 8f)
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
                    color = GlassTheme.colors.TextPrimary.copy(alpha = 0.75f), letterSpacing = 0.8.sp
                )
                Text(
                    phone,
                    fontSize = 12.sp, fontWeight = FontWeight.Medium,
                    color = GlassTheme.colors.TextPrimary
                )
            }
        }

        Spacer(Modifier.height(10.dp))

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
                    Canvas(modifier = Modifier.size(20.dp)) {
                        val cx = size.width / 2f
                        val path = Path().apply {
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
                        color = GlassTheme.colors.TextPrimary.copy(alpha = 0.75f), letterSpacing = 1.sp
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        location,
                        fontSize = 14.sp, fontWeight = FontWeight.Medium,
                        color = GlassTheme.colors.TextPrimary,
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
                Text("›", fontSize = 18.sp, color = GlassTheme.colors.TextMuted)
            }
        }
    }
}

// ╔══════════════════════════════════════════╗
//  COMPOSABLE 4 — BioCard
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GlassIconBox(
                        gradientColors = listOf(
                            Color(0xFF7C6EFA).copy(alpha = 0.30f),
                            Color(0xFFF472B6).copy(alpha = 0.20f)
                        ),
                        blobColor = Color(0xFFA78BFA).copy(alpha = 0.4f)
                    ) {
                        Canvas(modifier = Modifier.size(18.dp)) {
                            val cx = size.width / 2f
                            drawCircle(
                                brush = Brush.linearGradient(
                                    listOf(Color(0xFFA78BFA), Color(0xFFF472B6))
                                ),
                                radius = size.width * 0.28f,
                                center = Offset(cx, size.height * 0.30f)
                            )
                            val bodyPath = Path().apply {
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
                        color = GlassTheme.colors.TextPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .border(1.dp, GlassTheme.colors.GlassBorder, CircleShape)
                        .background(GlassTheme.colors.GlassBg)
                        .clickable { expanded = !expanded },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "▲",
                        fontSize = 10.sp,
                        color = GlassTheme.colors.TextMuted,
                        modifier = Modifier.graphicsLayer { rotationZ = arrowRotation }
                    )
                }
            }

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
                        color = GlassTheme.colors.TextSecond,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(Modifier.height(12.dp))
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
// ╚══════════════════════════════════════════╝
@Composable
fun SkillsGrid(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 18.dp)) {
        SectionLabel("Tech Stack")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SkillChip(
                modifier = Modifier.weight(1f),
                label = "Kotlin",
                gradientColors = listOf(
                    Color(0xFF7C6EFA).copy(alpha = 0.35f),
                    Color(0xFFF472B6).copy(alpha = 0.20f)
                )
            ) {
                Canvas(modifier = Modifier.size(22.dp)) {
                    val path = Path().apply {
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

            SkillChip(
                modifier = Modifier.weight(1f),
                label = "Compose",
                gradientColors = listOf(
                    Color(0xFF38BDF8).copy(alpha = 0.35f),
                    Color(0xFF2DD4BF).copy(alpha = 0.20f)
                )
            ) {
                Canvas(modifier = Modifier.size(22.dp)) {
                    val cx = size.width / 2f; val cy = size.height / 2f
                    val r = size.width / 2f
                    val path = Path().apply {
                        for (i in 0..5) {
                            val angle = ((60.0 * i) - 30.0) * (PI / 180.0)
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

            SkillChip(
                modifier = Modifier.weight(1f),
                label = "ITERA",
                gradientColors = listOf(
                    Color(0xFFFCD34D).copy(alpha = 0.35f),
                    Color(0xFFFB923C).copy(alpha = 0.20f)
                )
            ) {
                Canvas(modifier = Modifier.size(22.dp)) {
                    val path = Path().apply {
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
                        size = Size(
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
                color = GlassTheme.colors.TextSecond,
                letterSpacing = 0.3.sp
            )
        }
    }
}

// ════════════════════════════════════════════
//  PRIVATE HELPERS
// ════════════════════════════════════════════

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, GlassTheme.colors.GlassBorder, RoundedCornerShape(18.dp))
            .background(GlassTheme.colors.GlassBg)
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
            .background(GlassTheme.colors.GlassBorder2)
    )
    Spacer(Modifier.height(10.dp))
}

@Composable
private fun GlassTag(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, GlassTheme.colors.GlassBorder, RoundedCornerShape(20.dp))
            .background(GlassTheme.colors.GlassBg)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = GlassTheme.colors.TextSecond
        )
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = GlassTheme.colors.TextMuted,
        letterSpacing = 1.2.sp,
        modifier = Modifier.padding(bottom = 12.dp, top = 4.dp)
    )
}

@Composable
fun SocialSection(
    githubUrl: String,
    linkedinUrl: String,
    instagramUrl: String,
    onUrlClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 18.dp)) {
        SectionLabel("Tautan Media Sosial")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SocialIconCard(
                label = "GitHub",
                icon = "GitHub",
                color = Color(0xFF333333),
                modifier = Modifier.weight(1f),
                onClick = { onUrlClick(githubUrl) }
            )
            SocialIconCard(
                label = "LinkedIn",
                icon = "LinkedIn",
                color = Color(0xFF0077B5),
                modifier = Modifier.weight(1f),
                onClick = { onUrlClick(linkedinUrl) }
            )
            SocialIconCard(
                label = "Instagram",
                icon = "Instagram",
                color = Color(0xFFE4405F),
                modifier = Modifier.weight(1f),
                onClick = { onUrlClick(instagramUrl) }
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun SocialIconCard(
    label: String,
    icon: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, GlassTheme.colors.GlassBorder, RoundedCornerShape(16.dp))
            .background(GlassTheme.colors.GlassBg)
            .clickable { onClick() }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon.take(1),
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                color = GlassTheme.colors.TextSecond,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun AvatarSelectionDialog(
    onDismiss: () -> Unit,
    onAvatarSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pilih Avatar Default", color = GlassTheme.colors.TextPrimary) },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val avatars = listOf("🦊", "🦁", "🤖", "🚀", "🐱", "🐶")
                avatars.forEach { avatar ->
                    Text(
                        text = avatar,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .clickable {
                                onAvatarSelected(avatar)
                                onDismiss()
                            }
                            .padding(8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = GlassTheme.colors.Violet)
            }
        },
        containerColor = GlassTheme.colors.BgPhone,
        shape = RoundedCornerShape(24.dp)
    )
}
