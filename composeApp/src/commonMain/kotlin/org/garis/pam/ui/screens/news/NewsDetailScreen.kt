package org.garis.pam.ui.screens.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.garis.pam.GlassTheme
import org.garis.pam.data.model.Article

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(
    article: Article,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Berita", color = GlassTheme.colors.TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = GlassTheme.colors.TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GlassTheme.colors.BgPage
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            GlassTheme.colors.BgPage,
                            GlassTheme.colors.BgPhone
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Gambar Artikel
                val imageUrl = article.urlToImage
                if (imageUrl != null) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Gambar Berita",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tanggal
                Text(
                    text = article.publishedAt.take(10), // Mengambil format YYYY-MM-DD
                    color = GlassTheme.colors.Sky,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Judul
                Text(
                    text = article.title,
                    color = GlassTheme.colors.TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Konten / Deskripsi
                Text(
                    text = article.description ?: "Tidak ada detail konten untuk berita ini.",
                    color = GlassTheme.colors.TextSecond,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }
        }
    }
}
