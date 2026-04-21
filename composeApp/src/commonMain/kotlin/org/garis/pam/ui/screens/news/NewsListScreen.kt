package org.garis.pam.ui.screens.news

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.garis.pam.GlassTheme
import org.garis.pam.data.model.Article
import org.garis.pam.viewmodel.NewsUiState
import org.garis.pam.viewmodel.NewsViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsListScreen(
    viewModel: NewsViewModel,
    onNavigateToDetail: (Article) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GlassTheme.colors.BgPage)
            .pullRefresh(pullRefreshState)
    ) {
        // --- Efek Aurora Background ---
        AuroraBackground()

        when (val state = uiState) {
            is NewsUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = GlassTheme.colors.Violet
                )
            }
            is NewsUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    item {
                        Text(
                            "Berita Utama",
                            style = MaterialTheme.typography.headlineMedium,
                            color = GlassTheme.colors.TextPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    itemsIndexed(state.articles) { index, article ->
                        if (index == 0) {
                            // Berita Utama (Featured)
                            FeaturedNewsCard(
                                article = article,
                                onClick = { onNavigateToDetail(article) }
                            )
                        } else {
                            // Berita Reguler
                            NewsItemGlassCard(
                                article = article,
                                onClick = { onNavigateToDetail(article) }
                            )
                        }
                    }
                }
            }
            is NewsUiState.Error -> {
                ErrorView(message = state.message, onRetry = { viewModel.loadNews() })
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = GlassTheme.colors.Violet,
            backgroundColor = GlassTheme.colors.BgPhone
        )
    }
}

@Composable
fun AuroraBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-100).dp, y = (-50).dp)
                .blur(100.dp)
                .clip(CircleShape)
                .background(GlassTheme.colors.Violet.copy(alpha = 0.2f))
        )
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 50.dp, y = 50.dp)
                .blur(80.dp)
                .clip(CircleShape)
                .background(GlassTheme.colors.Sky.copy(alpha = 0.15f))
        )
    }
}

@Composable
fun FeaturedNewsCard(article: Article, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
            .padding(16.dp)
            .clickable { onClick() }
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(GlassTheme.colors.Violet.copy(0.5f), Color.Transparent)
                ),
                RoundedCornerShape(24.dp)
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = article.urlToImage,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Overlay Gradien
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 400f
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Surface(
                    color = GlassTheme.colors.Violet.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "TERBARU",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = article.title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 3
                )
            }
        }
    }
}

@Composable
fun NewsItemGlassCard(article: Article, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(GlassTheme.colors.GlassBg)
            .border(0.5.dp, GlassTheme.colors.GlassBorder2, RoundedCornerShape(20.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = article.urlToImage,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = article.title,
                    color = GlassTheme.colors.TextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = article.publishedAt.take(10),
                    color = GlassTheme.colors.TextMuted,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message, color = Color.Red.copy(0.7f))
        Button(onClick = onRetry) { Text("Coba Lagi") }
    }
}
