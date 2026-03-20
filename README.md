# 📱 My Profile App — Aurora Glass UI

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-7C6EFA?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-2DD4BF?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Android](https://img.shields.io/badge/Android-4ADE80?style=for-the-badge&logo=android&logoColor=white)
![Desktop](https://img.shields.io/badge/Desktop-38BDF8?style=for-the-badge&logo=windows&logoColor=white)

**Tugas Praktikum Minggu  4 — IF25-22017 Pengembangan Aplikasi Mobile**  
Institut Teknologi Sumatera (ITERA) · Teknik Informatika 2026

</div>

---

## 👤 Identitas Mahasiswa

| | |
|---|---|
| **Nama** | Garis Rayya Rabbani |
| **NIM** | 123140018 |
| **Kelas** | IF25-22017 |
| **Kampus** | Institut Teknologi Sumatera (ITERA) |
| **Email** | garis.123140018@student.itera.ac.id |

---

## 📋 Deskripsi Aplikasi

**My Profile App** adalah aplikasi profil pribadi yang dibangun menggunakan **Kotlin Compose Multiplatform (KMP)**, dapat berjalan di platform **Android** dan **Desktop** dari satu codebase yang sama.

Aplikasi ini menampilkan informasi profil lengkap dengan desain **Aurora Glass UI** — menggabungkan glassmorphism, gradient aurora animasi, dan dark/light mode yang dapat diubah secara dinamis.

---

## ✨ Fitur Utama

### Minggu 3 — Compose Multiplatform Basics
- **Aurora Glass UI** — hero section dengan animated mesh gradient + grid overlay
- **Profile Header** — avatar dengan spinning gradient ring, nama, badge, stats row
- **Contact Cards** — glassmorphism icon cards untuk email, telepon, lokasi
- **Bio Card** — collapsible dengan `AnimatedVisibility` *(BONUS +10%)*
- **Skills Grid** — 3-column grid tech stack (Kotlin, Compose, ITERA)
- **Bottom Navigation** — 4 tab dengan animasi aktif

### Minggu 4 — State Management & MVVM
- **ProfileViewModel** — `MutableStateFlow` + `StateFlow` + `ProfileUiState`
- **Edit Profile** — form edit nama & bio dengan state hoisting
- **Dark / Light Mode Toggle** — tema berubah dinamis via `CompositionLocalProvider` *(BONUS +10%)*
- 🔄 **Reactive UI** — semua perubahan state langsung terefleksi di UI

---

## 🏛 Arsitektur & Struktur Project

Aplikasi mengikuti pola **MVVM (Model-View-ViewModel)**:

```
composeApp/src/commonMain/kotlin/org/garis/pam/
│
├── 📁 data/
│   └── UserProfile.kt          # Data class + instance myProfile
│
├── 📁 viewmodel/
│   └── ProfileViewModel.kt     # ViewModel + ProfileUiState + StateFlow
│
├── 📁 ui/
│   ├── ProfileScreen.kt        # Layar utama profil (stateless)
│   ├── ProfileComponents.kt    # Semua @Composable reusable
│   └── EditProfileScreen.kt    # Form edit profil + GlassTextField
│
├── GlassTheme.kt               # Design tokens dinamis (dark/light)
└── App.kt                      # Entry point + ViewModel provider
```

### Alur Data (MVVM)
```
User Action
    ↓
ProfileComponents (UI / View)
    ↓  event (onClick → viewModel::startEditing)
ProfileViewModel
    ↓  _uiState.update { }
ProfileUiState (StateFlow)
    ↓  collectAsState()
App.kt → ProfileScreen / EditProfileScreen
    ↓  recompose
UI terupdate otomatis ✓
```

---

## 🧩 Composable Functions

| Fungsi | Lokasi | Deskripsi |
|---|---|---|
| `HeroSection()` | ProfileComponents | Hero 300dp + gradient mesh + back btn + nama |
| `StatsAndActions()` | ProfileComponents | Stats row + subtitle + tombol Edit & Dark Mode |
| `ContactSection()` | ProfileComponents | 2-col mini cards + full-width lokasi |
| `BioCard()` | ProfileComponents | Bio collapsible dengan AnimatedVisibility |
| `SkillsGrid()` | ProfileComponents | 3-col tech stack chips dengan canvas icon |
| `BottomNav()` | ProfileComponents | Bottom nav 4 tab dengan animasi spring |
| `GlassCard()` | ProfileComponents | Wrapper card glassmorphism reusable |
| `GlassIconBox()` | ProfileComponents | Icon box frosted glass dengan gradient blob |
| `EditProfileScreen()` | EditProfileScreen | Form edit nama & bio |
| `GlassTextField()` | EditProfileScreen | Stateless TextField — state dihoisting ke ViewModel |

---

## 🎨 Design System — Aurora Glass

| Token | Dark Mode | Light Mode |
|---|---|---|
| Background | `#060410` | `#F0EEFF` |
| Surface | `rgba(255,255,255, 7%)` | `rgba(0,0,0, 9%)` |
| Border | `rgba(255,255,255, 13%)` | `rgba(0,0,0, 16%)` |
| Text Primary | `rgba(255,255,255, 92%)` | `#1A1040` |
| Accent Violet | `#7C6EFA` | sama |
| Accent Pink | `#F472B6` | sama |
| Accent Teal | `#2DD4BF` | sama |

---

## 🚀 Cara Menjalankan

### Prasyarat
- Android Studio Hedgehog atau lebih baru
- JDK 17+
- Android SDK API 24+

### Clone Repository
```bash
git clone https://github.com/USERNAME/NAMA_REPO.git
cd NAMA_REPO
```

### Jalankan di Android
1. Buka project di Android Studio
2. Jalankan emulator atau sambungkan HP
3. Klik tombol **▶ Run** atau tekan `Shift + F10`

### Jalankan di Desktop
```bash
./gradlew desktopRun
```

---

## 📦 Dependencies

```kotlin
// build.gradle.kts — commonMain
implementation(compose.runtime)
implementation(compose.foundation)
implementation(compose.material3)
implementation(compose.ui)
implementation(compose.animation)
implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
```

---

## 📸 Screenshot

> *Tambahkan screenshot aplikasi di sini setelah build*

| Dark Mode | Light Mode | Edit Profile |
|---|---|---|
| *<img width="1440" height="3120" alt="image" src="https://github.com/user-attachments/assets/bac1c012-198d-4783-823e-78e8f824ac05" />* | *<img width="1440" height="3120" alt="image" src="https://github.com/user-attachments/assets/0c178141-67b9-4230-936e-9e0ccf531526" />* | *<img width="1440" height="3120" alt="image" src="https://github.com/user-attachments/assets/27814a83-0f08-4f82-b4b0-7a6a66585bb2" />* |

---

## 📚 Konsep yang Diterapkan

- ✅ **Layouts** — `Column`, `Row`, `Box`, `LazyColumn`
- ✅ **Modifiers** — `padding`, `clip`, `border`, `background`, `graphicsLayer`
- ✅ **State Management** — `remember`, `mutableStateOf`, `collectAsState`
- ✅ **State Hoisting** — `GlassTextField` menerima value + onValueChange dari ViewModel
- ✅ **ViewModel** — `ProfileViewModel` dengan `MutableStateFlow` dan `StateFlow`
- ✅ **Animations** — `AnimatedVisibility`, `animateFloatAsState`, `animateColorAsState`, `spring`
- ✅ **CompositionLocal** — `LocalGlassColors` untuk dynamic theming
- ✅ **Canvas** — custom drawing untuk icon glassmorphism dan gradient mesh

---

<div align="center">

Dibuat dengan ❤️ menggunakan Kotlin & Compose Multiplatform  
IF25-22017 · ITERA · 2026

</div>
