# 📰 News Reader App - Compose Multiplatform

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-7C6EFA?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-2DD4BF?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Ktor](https://img.shields.io/badge/Ktor-087CFA?style=for-the-badge&logo=ktor&logoColor=white)
![Android](https://img.shields.io/badge/Android-4ADE80?style=for-the-badge&logo=android&logoColor=white)
![Desktop](https://img.shields.io/badge/Desktop-38BDF8?style=for-the-badge&logo=windows&logoColor=white)

**Tugas Praktikum Minggu 6 — IF25-22017 Pengembangan Aplikasi Mobile**
Branch: `week-6` | Institut Teknologi Sumatera (ITERA) · Teknik Informatika 2023

</div>

---

## 👤 Identitas Mahasiswa

| | |
|---|---|
| **Nama** | Garis Rayya Rabbani |
| **NIM** | 123140018 |
| **Kelas** | IF25-22017 |
| **Kampus** | Institut Teknologi Sumatera (ITERA) |
| **Email** | garisrayya@gmail.com |

---

## 📋 Deskripsi Aplikasi

**Aurora Glass News Reader** adalah pengembangan lanjutan dari Notes App minggu sebelumnya, kini dilengkapi dengan fitur **Networking & REST API** menggunakan **Ktor Client** untuk Kotlin Multiplatform.

Aplikasi ini mengambil berita utama secara *real-time* dari [NewsAPI](https://newsapi.org/), menampilkannya dengan antarmuka **Aurora Glass / Glassmorphism** yang elegan, lengkap dengan penanganan state *Loading*, *Success*, dan *Error* secara reaktif berbasis `StateFlow`.

---

## ✨ Fitur Minggu 6 — Networking & REST API

### REST API Integration
- 🌐 **Top Headlines** — mengambil berita utama secara *real-time* dari NewsAPI
- 🔄 **Pull-to-Refresh** — memperbarui umpan berita dengan gestur tarik ke bawah
- 🚦 **Reactive State** — penanganan *Loading*, *Success*, dan *Error* berbasis `StateFlow`
- 🔁 **Error Recovery** — tombol coba ulang saat koneksi gagal

### Asynchronous Image Loading
- 🖼️ **Coil 3 Multiplatform** — memuat gambar artikel secara asinkron dengan mulus
- 🖼️ **Fallback Handler** — placeholder elegan jika gambar tidak tersedia atau gagal dimuat

### Navigation Flow
- **News List → News Detail** — navigasi ke halaman detail konten artikel penuh
- **Back navigation** — `popBackStack()` dari semua screen detail

### Design System
- 🎨 **Aurora Glassmorphism** — tema kustom `GlassTheme` dengan efek kaca transparan
- 🌙 **Dark / Light Mode** — desain adaptif sesuai preferensi sistem

---

## 🏛 Arsitektur & Struktur Project

```
composeApp/src/commonMain/kotlin/org/garis/pam/
│
├── 📁 data/                        ★ BARU MINGGU 6
│   ├── Article.kt                  # Data class artikel dari API
│   ├── NewsApiResponse.kt          # Data class response JSON (wrapper)
│   └── NewsRepository.kt          # Repository — Ktor HTTP client & endpoint
│
├── 📁 viewmodel/
│   ├── ProfileViewModel.kt         # ViewModel profil (reuse minggu 4)
│   ├── NoteViewModel.kt            # ViewModel notes (reuse minggu 5)
│   └── NewsViewModel.kt            ★ BARU MINGGU 6
│                                   # ViewModel berita + NewsUiState
│
├── 📁 navigation/
│   ├── Screen.kt                   # Sealed class semua routes (+ NewsDetail)
│   └── AppNavigation.kt            # NavHost + NavController + BottomNav
│
├── 📁 screens/
│   ├── FavoritesScreen.kt
│   └── 📁 news/                    ★ BARU MINGGU 6
│       ├── NewsListScreen.kt       # Daftar artikel + Pull-to-Refresh + state
│       ├── NewsDetailScreen.kt     # Konten artikel lengkap dengan gambar hero
│       └── NewsComponents.kt       # GlassArticleCard, LoadingShimmer, ErrorView
│
├── 📁 ui/                          (dari minggu sebelumnya)
│   ├── ProfileScreen.kt
│   ├── ProfileComponents.kt
│   └── EditProfileScreen.kt
│
├── GlassTheme.kt                   # Dynamic dark/light design tokens
└── App.kt                          # Entry point + ViewModel + CompositionLocal
```

---

## 🗺 Navigation Flow Diagram

```
App Start
    │
    ▼
AppNavigation (NavHost)
    │
    ├── [Bottom Nav Tab 1] News ────────────────────────────┐
    │       │                                               │
    │       ├── [Loading State] ──► GlassShimmer Animation  │
    │       │                                               │
    │       ├── [Error State] ──► ErrorView + Retry Button  │
    │       │                                               │
    │       ├── [Success State] ──► ArticleCard List        │
    │       │       │                                       │
    │       │       └── navigate("news_detail/{articleUrl}")│
    │       │                       │                       │
    │       │                       ▼                       │
    │       │               NewsDetailScreen                │
    │       │               [Back] popBackStack() ◄─────────┘
    │       │
    │       └── Pull-to-Refresh ──► reload dari NewsRepository
    │
    ├── [Bottom Nav Tab 2] Favorites  (reuse minggu 5)
    │
    └── [Bottom Nav Tab 3] Profile    (reuse minggu 3 & 4)
            │
            └── navigate("edit_profile") ──► EditProfileScreen
                                             [Save/Cancel] popBackStack()

Tab switching: popUpTo(startDestination) + launchSingleTop + restoreState
```

---

## 🧩 Komponen Networking Utama

| Komponen | File | Deskripsi |
|---|---|---|
| `NewsRepository` | NewsRepository.kt | Ktor HTTP client, base URL, & endpoint NewsAPI |
| `NewsViewModel` | NewsViewModel.kt | StateFlow `NewsUiState` — Loading / Success / Error |
| `Article` | Article.kt | Data class artikel hasil deserialisasi JSON |
| `NewsApiResponse` | NewsApiResponse.kt | Wrapper response JSON dari NewsAPI |
| `NewsListScreen` | NewsListScreen.kt | UI daftar berita + Pull-to-Refresh + state handling |
| `NewsDetailScreen` | NewsDetailScreen.kt | UI detail artikel lengkap dengan gambar hero |
| `GlassArticleCard` | NewsComponents.kt | Kartu artikel dengan efek glassmorphism + Coil image |
| `ErrorView` | NewsComponents.kt | Tampilan error + tombol retry |

---

## 🔄 Alur Data — REST API

### Setup Ktor Client
```kotlin
// NewsRepository.kt
private val client = HttpClient {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}

private val baseUrl = "https://newsapi.org/v2"
private val apiKey  = "MASUKKAN_API_KEY_ANDA_DI_SINI"

suspend fun getTopHeadlines(country: String = "us"): List<Article> {
    val response: NewsApiResponse = client.get("$baseUrl/top-headlines") {
        parameter("country", country)
        parameter("apiKey", apiKey)
    }.body()
    return response.articles
}
```

### State Management dengan StateFlow
```kotlin
// NewsViewModel.kt
sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<Article>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()
    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init { fetchNews() }

    fun fetchNews() {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            try {
                val articles = repository.getTopHeadlines()
                _uiState.value = NewsUiState.Success(articles)
            } catch (e: Exception) {
                _uiState.value = NewsUiState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
}
```

### Asynchronous Image Loading dengan Coil 3
```kotlin
// GlassArticleCard — NewsComponents.kt
AsyncImage(
    model = article.urlToImage,
    contentDescription = article.title,
    contentScale = ContentScale.Crop,
    error = painterResource(Res.drawable.placeholder),
    placeholder = painterResource(Res.drawable.placeholder),
    modifier = Modifier.fillMaxWidth().height(180.dp)
)
```

---

## 🎨 Design System — Aurora Glass

Efek glassmorphism kustom dengan latar particle network animasi, diterapkan konsisten di seluruh app:

| Token | Dark Mode | Light Mode |
|---|---|---|
| Background | `#060410` | `#F0EEFF` |
| Glass Surface | `rgba(255,255,255, 7%)` | `rgba(0,0,0, 9%)` |
| Text Primary | `rgba(255,255,255, 92%)` | `#1A1040` |
| Accent Violet | `#7C6EFA` | sama |
| Accent Teal | `#2DD4BF` | sama |
| Accent Pink | `#F472B6` | sama |

---

## 🚀 Cara Menjalankan

### Clone & Checkout Branch
```bash
git clone https://github.com/USERNAME/NAMA_REPO.git
cd NAMA_REPO
git checkout week-6
```

### Konfigurasi API Key

Buka file berikut:
```
composeApp/src/commonMain/kotlin/org/garis/pam/data/NewsRepository.kt
```
Ganti nilai `apiKey` dengan kunci rahasia Anda:
```kotlin
private val apiKey = "MASUKKAN_API_KEY_ANDA_DI_SINI"
```
> Dapatkan API Key gratis di [newsapi.org](https://newsapi.org/)

### Android
Buka di Android Studio → jalankan emulator → klik **▶ Run**

### Desktop
```bash
./gradlew :composeApp:run
```

---

## 📦 Dependencies

```kotlin
commonMain.dependencies {
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(compose.ui)
    implementation(compose.animation)
    implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")

    // ★ BARU MINGGU 6
    implementation("io.ktor:ktor-client-core:2.3.12")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation("io.coil-kt.coil3:coil-compose:3.0.0-rc01")
    implementation("io.coil-kt.coil3:coil-network-ktor3:3.0.0-rc01")
}

androidMain.dependencies {
    // ★ BARU MINGGU 6
    implementation("io.ktor:ktor-client-okhttp:2.3.12")
}

desktopMain.dependencies {
    // ★ BARU MINGGU 6
    implementation("io.ktor:ktor-client-okhttp:2.3.12")
}
```

---

## 📸 Screenshot

| News List | News Detail | 
|---|---|
| <img width="1440" height="3120" alt="Screenshot_20260419_211020" src="https://github.com/user-attachments/assets/ce590788-09b9-4c3b-a9f4-7c3e533dc7e9" /> | <img width="1440" height="3120" alt="Screenshot_20260419_211055" src="https://github.com/user-attachments/assets/e847beb7-8a89-48f2-9030-6ae8794da167" /> | 



---

## 📚 Konsep yang Diterapkan Minggu 6

- ✅ **Ktor HTTP Client** — networking multiplatform untuk Android & Desktop
- ✅ **REST API Integration** — konsumsi endpoint NewsAPI dengan parameter
- ✅ **Kotlinx Serialization** — deserialisasi JSON ke data class Kotlin
- ✅ **Repository Pattern** — abstraksi sumber data dari ViewModel
- ✅ **StateFlow + Sealed Class** — reactive UI state (Loading / Success / Error)
- ✅ **Coroutines + viewModelScope** — async network call yang aman
- ✅ **Coil 3 Multiplatform** — asynchronous image loading lintas platform
- ✅ **Pull-to-Refresh** — `PullRefreshIndicator` dari Material 3
- ✅ **Error Handling & Retry** — penanganan exception + UI pemulihan
- ✅ **Conditional UI** — tampilan berbeda setiap state antarmuka

---

## 📝 Catatan Pengembangan

Aplikasi ini dikembangkan secara bertahap:
- **Minggu 3** — Profile App dasar, Compose layouts, custom composables
- **Minggu 4** — MVVM, ViewModel, StateFlow, dark/light mode
- **Minggu 5** — Navigation Component, multi-screen, Bottom Navigation
- **Minggu 6** — Networking, REST API, Ktor Client, Coil image loading

---

<div align="center">

Link Vidio Demo :
https://youtube.com/shorts/J37R0dRmwLQ 

Dibuat dengan ❤️ menggunakan Kotlin & Compose Multiplatform
IF25-22017 · ITERA · 2025/2026

</div>
