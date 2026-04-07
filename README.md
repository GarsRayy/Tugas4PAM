# 📱 Aurora Glass Notes App

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-7C6EFA?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-2DD4BF?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Navigation](https://img.shields.io/badge/Navigation_Compose-F472B6?style=for-the-badge&logo=android&logoColor=white)
![Android](https://img.shields.io/badge/Android-4ADE80?style=for-the-badge&logo=android&logoColor=white)
![Desktop](https://img.shields.io/badge/Desktop-38BDF8?style=for-the-badge&logo=windows&logoColor=white)

**Tugas Praktikum Minggu 5 — IF25-22017 Pengembangan Aplikasi Mobile**
Branch: `week-5` | Institut Teknologi Sumatera (ITERA) · Teknik Informatika 2023

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

**Aurora Glass Notes App** adalah pengembangan lanjutan dari Profile App minggu sebelumnya, kini dilengkapi dengan fitur **navigasi multi-screen** menggunakan **Navigation Compose** untuk Kotlin Multiplatform.

Aplikasi ini memiliki 3 tab utama yang terhubung via Bottom Navigation, dengan alur navigasi lengkap antar screen menggunakan `NavHost`, `NavController`, dan passing arguments antar destination.

---

## ✨ Fitur Minggu 5 — Navigation

### Bottom Navigation (3 Tabs)
- 📝 **Notes** — list semua catatan dengan FAB tambah catatan baru
- ❤ **Favorites** — catatan yang ditandai favorit
- 👤 **Profile** — halaman profil dari minggu 3 & 4 (reuse!)

### Navigation Flow Lengkap
- **Note List → Note Detail** — passing `noteId` sebagai required argument
- **Note Detail → Edit Note** — passing `noteId` untuk edit
- **FAB (+) → Add Note** — navigate ke form tambah catatan baru
- **Profile → Edit Profile** — navigate ke form edit profil
- **Back navigation** — `popBackStack()` dari semua screen detail

### Fitur Notes
- 📝 Buat catatan baru dengan judul dan isi
- 👁 Lihat detail catatan lengkap
- ✏️ Edit catatan yang sudah ada
- 🗑 Hapus catatan
- ❤ Toggle favorit per catatan
- 🎨 5 warna aksen berbeda per catatan (Violet, Teal, Pink, Gold, Sky)

### Bonus (+10%) — Navigation Drawer *(jika diimplementasikan)*

---

## 🏛 Arsitektur & Struktur Project

```
composeApp/src/commonMain/kotlin/org/garis/pam/
│
├── 📁 data/
│   ├── UserProfile.kt          # Data class profil
│   └── Note.kt                 # Data class note + NoteColor enum + sampleNotes
│
├── 📁 viewmodel/
│   ├── ProfileViewModel.kt     # ViewModel profil + ProfileUiState
│   └── NoteViewModel.kt        # ViewModel notes + NoteUiState
│
├── 📁 navigation/              ★ BARU MINGGU 5
│   ├── Screen.kt               # Sealed class semua routes
│   └── AppNavigation.kt        # NavHost + NavController + GlassBottomNav
│
├── 📁 screens/                 ★ BARU MINGGU 5
│   ├── FavoritesScreen.kt      # Tab Favorites
│   └── 📁 notes/
│       ├── NoteListScreen.kt   # Daftar catatan + FAB
│       ├── NoteDetailScreen.kt # Detail catatan + edit + delete
│       └── AddEditNoteScreen.kt# Form tambah/edit catatan
│
├── 📁 ui/                      (dari minggu sebelumnya)
│   ├── ProfileScreen.kt
│   ├── ProfileComponents.kt
│   └── EditProfileScreen.kt
│
├── 📁 components/              ★ BARU MINGGU 5
│
├── GlassTheme.kt               # Dynamic dark/light design tokens
└── App.kt                      # Entry point + ViewModel + CompositionLocal
```

---

## 🗺 Navigation Flow Diagram

```
App Start
    │
    ▼
AppNavigation (NavHost)
    │
    ├── [Bottom Nav Tab 1] Notes ──────────────────────────┐
    │       │                                              │
    │       ├── navigate("note_detail/{noteId}") ──► NoteDetailScreen
    │       │                                              │
    │       │   [Back] popBackStack() ◄────────────────────┤
    │       │                                              │
    │       │                          navigate("edit_note/{noteId}")
    │       │                                              │
    │       │                                    ▼
    │       │                          EditNoteScreen
    │       │                          [Save/Back] popBackStack()
    │       │
    │       └── FAB navigate("add_note") ──► AddNoteScreen
    │                                        [Save/Back] popBackStack()
    │
    ├── [Bottom Nav Tab 2] Favorites
    │       │
    │       └── navigate("note_detail/{noteId}") ──► NoteDetailScreen
    │
    └── [Bottom Nav Tab 3] Profile
            │
            └── navigate("edit_profile") ──► EditProfileScreen
                                             [Save/Cancel] popBackStack()

Tab switching: popUpTo(startDestination) + launchSingleTop + restoreState
```

---

## 🧩 Komponen Navigation Utama

| Komponen | File | Deskripsi |
|---|---|---|
| `sealed class Screen` | Screen.kt | Semua route definitions — best practice type-safe |
| `AppNavigation()` | AppNavigation.kt | NavHost dengan semua composable destinations |
| `GlassBottomNav()` | AppNavigation.kt | Bottom nav 3 tab dengan animasi spring + color |
| `rememberNavController()` | AppNavigation.kt | NavController untuk mengatur perpindahan |
| `navArgument("noteId")` | AppNavigation.kt | Required argument `IntType` untuk passing noteId |
| `popBackStack()` | Semua detail screen | Back navigation yang proper |
| `popUpTo + launchSingleTop` | GlassBottomNav | Tab switching tanpa duplicate back stack |

---

## 🔄 Passing Arguments

### Required Argument — noteId
```kotlin
// Route definition
object NoteDetail : Screen("note_detail/{noteId}") {
    fun createRoute(noteId: Int) = "note_detail/$noteId"
}

// Navigate dengan argument
navController.navigate(Screen.NoteDetail.createRoute(noteId = 3))

// Ambil argument di destination
composable(
    route = Screen.NoteDetail.route,
    arguments = listOf(navArgument("noteId") { type = NavType.IntType })
) { backStackEntry ->
    val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
    NoteDetailScreen(note = noteViewModel.getNoteById(noteId))
}
```

---

## 🎨 Design System — Aurora Glass

Particle network animasi di background hero profile tab, dengan:
- 14 partikel bergerak sinusoidal secara independen
- Garis koneksi antar partikel yang berdekatan (jarak < 30% lebar layar)
- Grid dots tekstur tipis sebagai latar
- Warna aksen: Violet, Lavender, Pink, Teal, Sky, Gold

| Token | Dark Mode | Light Mode |
|---|---|---|
| Background | `#060410` | `#F0EEFF` |
| Glass Surface | `rgba(255,255,255, 7%)` | `rgba(0,0,0, 9%)` |
| Text Primary | `rgba(255,255,255, 92%)` | `#1A1040` |
| Accent Violet | `#7C6EFA` | sama |

---

## 🚀 Cara Menjalankan

### Clone & Checkout Branch
```bash
git clone https://github.com/USERNAME/NAMA_REPO.git
cd NAMA_REPO
git checkout week-5
```

### Android
Buka di Android Studio → jalankan emulator → klik **▶ Run**

### Desktop
```bash
./gradlew desktopRun
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
    // ★ BARU MINGGU 5
    implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")
}
```

---

## 📸 Screenshot


| Notes Tab | Note Detail | Add Note |
|---|---|---|
| <img width="1440" height="3120" alt="Screenshot_20260406_135031" src="https://github.com/user-attachments/assets/975be527-a5c9-4404-9fce-ef420e788f31" /> | <img width="1440" height="3120" alt="Screenshot_20260406_135058" src="https://github.com/user-attachments/assets/d46c4830-0f2b-4ff8-a225-dca95af98b37" /> | <img width="1440" height="3120" alt="Screenshot_20260407_201511" src="https://github.com/user-attachments/assets/f656e660-5358-48fb-981d-dca984b89ea3" />
 |

| Favorites Tab | Profile Tab | Edit Profile |
|---|---|---|
| <img width="1440" height="3120" alt="Screenshot_20260406_135045" src="https://github.com/user-attachments/assets/ba6aa040-3057-4060-9978-9e9294e0aab3" /> | <img width="1440" height="3120" alt="Screenshot_20260407_200857" src="https://github.com/user-attachments/assets/89dfb315-fd03-42a4-923e-7bb98e28d50d" /> | <img width="1440" height="3120" alt="Screenshot_20260407_201145" src="https://github.com/user-attachments/assets/8ef89fd2-8b55-4064-ab02-d1af5926ae85" /> |

---

## 📚 Konsep yang Diterapkan Minggu 5

- ✅ **NavHost & NavController** — container dan pengatur navigasi
- ✅ **Sealed class Routes** — type-safe route definitions
- ✅ **Required Arguments** — `noteId: Int` passing antar screen
- ✅ **navigate()** — forward navigation ke destination baru
- ✅ **popBackStack()** — back navigation yang proper
- ✅ **popUpTo + launchSingleTop** — tab switching tanpa duplicate stack
- ✅ **Bottom Navigation** — 3 tabs dengan `NavigationBar` pattern
- ✅ **Scaffold + bottomBar** — integrasi bottom nav dengan konten
- ✅ **Conditional bottomBar** — hide bottom nav di detail screens
- ✅ **Multi-ViewModel** — `ProfileViewModel` + `NoteViewModel` di satu app

---

## 📝 Catatan Pengembangan

Aplikasi ini dikembangkan secara bertahap:
- **Minggu 3** — Profile App dasar, Compose layouts, custom composables
- **Minggu 4** — MVVM, ViewModel, StateFlow, dark/light mode
- **Minggu 5** — Navigation Component, multi-screen, Bottom Navigation

---

<div align="center">

Dibuat dengan ❤️ menggunakan Kotlin & Compose Multiplatform
IF25-22017 · ITERA · 2025/2026

</div>
