# 📓 Aurora Glass Notes App - Local Data Storage

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-7C6EFA?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-2DD4BF?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![SQLDelight](https://img.shields.io/badge/SQLDelight-087CFA?style=for-the-badge&logo=sqlite&logoColor=white)
![Android](https://img.shields.io/badge/Android-4ADE80?style=for-the-badge&logo=android&logoColor=white)
![Desktop](https://img.shields.io/badge/Desktop-38BDF8?style=for-the-badge&logo=windows&logoColor=white)

**Tugas Praktikum Minggu 7 — IF25-22017 Pengembangan Aplikasi Mobile**  
Branch: `week-7` | Institut Teknologi Sumatera (ITERA) · Teknik Informatika 2023

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

**Aurora Glass Notes App** adalah pengembangan lebih lanjut yang berfokus pada **Penyimpanan Data Lokal (Local Data Storage)**. Aplikasi ini mengintegrasikan **SQLDelight** untuk manajemen basis data relasional yang *type-safe* dan **Multiplatform Settings (DataStore)** untuk manajemen preferensi pengguna.

Dengan pendekatan **Offline-First Architecture**, aplikasi ini memastikan seluruh data catatan pengguna tersimpan secara permanen di perangkat, dapat diakses tanpa koneksi internet, dan tetap mempertahankan estetika desain **Glassmorphism** yang reaktif.

---

## ✨ Fitur Minggu 7 — Local Data Storage

### 🗄️ SQLDelight Database (CRUD & Search)
- **Full CRUD Operations** — Membuat, membaca, memperbarui, dan menghapus catatan secara persisten.
- **Advanced Features** — Mendukung fitur *Pinned*, *Favorite*, dan *Archived* untuk pengorganisasian catatan.
- **Real-time Search** — Pencarian catatan berdasarkan judul, isi, maupun tag menggunakan kueri SQL `LIKE` yang reaktif.
- **Auto-Update UI** — Sinkronisasi otomatis antara basis data dan tampilan menggunakan Kotlin Flow.

### ⚙️ DataStore Settings
- **App Theme Selector** — Menyimpan preferensi tema (Aurora Glass, Dark, Light) secara permanen.
- **Sort Order Preference** — Pengaturan urutan catatan (Terbaru/Terlama) yang tetap tersimpan meskipun aplikasi ditutup.

### 🏗️ Architecture & UX
- **Offline-First** — Data lokal sebagai *Single Source of Truth*.
- **Reactive UI States** — Penanganan *state* aplikasi (Loading, Empty, Content) yang elegan.

---

## 🗃️ Database Schema (SQLDelight)

Skema basis data dirancang untuk fleksibilitas tinggi dengan dukungan meta-data lengkap:

```sql
CREATE TABLE NoteEntity (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    is_favorite INTEGER NOT NULL DEFAULT 0,
    is_pinned INTEGER NOT NULL DEFAULT 0,
    is_archived INTEGER NOT NULL DEFAULT 0,
    tags TEXT NOT NULL DEFAULT '',
    color_name TEXT NOT NULL DEFAULT 'VIOLET',
    created_at INTEGER NOT NULL,
    updated_at INTEGER NOT NULL
);
```

---

## 🏛 Arsitektur & Struktur Project

```
composeApp/src/commonMain/kotlin/org/garis/pam/
│
├── 📁 data/
│   ├── 📁 local/
│   │   ├── DatabaseDriverFactory.kt  # Driver platform-specific (Android/JVM)
│   │   └── SettingsManager.kt       # Multiplatform Settings (DataStore)
│   └── 📁 repository/
│       └── NoteRepository.kt        # Abstraksi data SQLDelight
│
├── 📁 viewmodel/
│   ├── NoteViewModel.kt             # Logika bisnis & Reactive State Flow
│   └── SettingsViewModel.kt         # Manajemen state preferensi tema/sortir
│
├── 📁 sqldelight/
│   └── org/garis/pam/db/Note.sq     # Definisi Schema & SQL Queries
│
├── 📁 ui/
│   └── 📁 screens/
│       ├── 📁 notes/                # CRUD UI (List, Add, Edit)
│       └── 📁 settings/             # Settings UI (Theme & Sort)
│
└── App.kt                           # Inisialisasi Database & Driver
```

---

## 🧩 Komponen Penyimpanan Utama

| Komponen | Deskripsi |
|---|---|
| `NoteRepository` | Mengelola kueri SQLDelight dan konversi data ke Flow. |
| `SettingsManager` | Mengatur persistensi tema dan sortir menggunakan *ObservableSettings*. |
| `NoteViewModel` | Menggabungkan kueri pencarian dan urutan sortir secara dinamis (`combine`). |
| `DatabaseDriverFactory` | Menyediakan `SqlDriver` yang sesuai untuk Android maupun Desktop. |

---

## 📸 Screenshot

| Note List & Search | Add/Edit Note | Settings & Theme |
|---|---|---|
| <img width="1440" height="3120" alt="Screenshot_20260423_084605" src="https://github.com/user-attachments/assets/a12daaa8-f3e6-4469-8a4e-b337a7c3987f" /> | <img width="1440" height="3120" alt="Screenshot_20260423_084621" src="https://github.com/user-attachments/assets/4d17ba25-fd51-49d0-b0ce-2f531918a1fd" /> | <img width="1440" height="3120" alt="Screenshot_20260423_084640" src="https://github.com/user-attachments/assets/fd235d64-fbb4-42ce-ae72-97c50c8f606c" /> |

---

## 🎥 Video Demo

Tonton demonstrasi fitur CRUD, Search, dan Settings Offline-mode di sini:  
https://youtube.com/shorts/tU3m0wy9G3M

---

## 📚 Konsep yang Diterapkan

- ✅ **SQLDelight Setup** — Implementasi driver per platform dan skema `.sq`.
- ✅ **Repository Pattern** — Pemisahan logika akses data dari UI.
- ✅ **Reactive Data** — Penggunaan `asFlow()` dan `mapToList()` untuk pembaruan UI otomatis.
- ✅ **Key-Value Storage** — Persistensi pengaturan aplikasi dengan *Multiplatform Settings*.
- ✅ **Complex Flow Logic** — Penggunaan `flatMapLatest` untuk pencarian dinamis yang efisien.

---

<div align="center">

Dibuat dengan ❤️ menggunakan Kotlin & SQLDelight  
IF25-22017 · ITERA · 2025/2026

</div>
