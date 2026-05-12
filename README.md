# 📱 NoteAI Synesthesia — Sprint 1

![CI](https://github.com/Garis-Rayya-Rabbani/Tugas3_ProfileApp/actions/workflows/ci.yml/badge.svg)

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-7C6EFA?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-2DD4BF?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Android](https://img.shields.io/badge/Android-4ADE80?style=for-the-badge&logo=android&logoColor=white)
![Clean Architecture](https://img.shields.io/badge/Architecture-Clean-red?style=for-the-badge)

**Tugas Project Akhir — IF25-22017 Pengembangan Aplikasi Mobile**  
Institut Teknologi Sumatera (ITERA) · Teknik Informatika 2026

</div>

---

## 👥 Identitas Tim

| Nama Lengkap | NIM | Peran | GitHub |
|---|---|---|---|
| **Garis Rayya Rabbani** | 123140018 | Lead Presentation | [@Garis-Rayya-Rabbani](https://github.com/Garis-Rayya-Rabbani) |
| **Arya (TBD)** | XXXXXXXX | Lead Data/Domain | [@Username](https://github.com/username) |

---

## 📋 Deskripsi Project

**NoteAI Synesthesia** adalah aplikasi manajemen catatan cerdas yang memanfaatkan kecerdasan buatan (Gemini AI) untuk membantu pengguna mengorganisir pikiran mereka. Nama "Synesthesia" merujuk pada fitur unik aplikasi yang secara otomatis memetakan nuansa emosional catatan ke dalam warna visual (Aurora Glass UI), menciptakan pengalaman menulis yang multisensorik.

---

## ✨ Fitur & Deliverables Sprint 1

### Fitur Utama (Minimum)
- [x] **Setup Project KMP**: Struktur folder Multiplatform (Android & iOS/Desktop).
- [x] **Clean Architecture Implementation**: Pemisahan layer Data, Domain, dan Presentation.
- [x] **DI with Koin**: Implementasi Dependency Injection modular.
- [x] **CI/CD Pipeline**: GitHub Actions untuk automated build & test.
- [x] **Note CRUD**: Dasar penyimpanan catatan dengan SQLDelight.

### Fitur Canggih (Bonus)
- [ ] **Gemini AI Integration**: Ringkasan otomatis dan analisis sentimen catatan.
- [ ] **Aurora Glass UI**: Antarmuka berbasis glassmorphism dengan animasi mesh gradient.
- [ ] **Synesthesia Color Mapping**: Perubahan warna tema berdasarkan isi catatan.

---

## 🏛 Arsitektur Project

Aplikasi ini menerapkan **Clean Architecture** dengan pola **MVVM**:

```
composeApp/src/commonMain/kotlin/org/garis/pam/
│
├── 📁 presentation/           # ViewModels, Screens, Components
├── 📁 domain/                 # Use Cases, Entities, Repository Interfaces
├── 📁 data/                   # Repository Impl, Local/Remote Data Sources
├── 📁 di/                     # Koin Modules
└── 📁 core/                   # Utilities, Network Config, Shared Constants
```

- **Data Layer**: Menangani SQLDelight (Local) dan Ktor (Remote).
- **Domain Layer**: Berisi logika bisnis murni tanpa ketergantungan pada library UI/Framework.
- **Presentation Layer**: UI deklaratif menggunakan Jetpack Compose.

---

## 🛠️ Tech Stack

- **Language**: Kotlin 2.1.0
- **UI Framework**: Compose Multiplatform
- **DI**: Koin 4.0.0
- **Database**: SQLDelight 2.0.1
- **Networking**: Ktor 3.0.1
- **Testing**: MockK, Turbine, Kover
- **CI/CD**: GitHub Actions

---

## 🚀 Cara Menjalankan

1. Clone repository ke branch project: `git checkout project/123140018-NoteAI`.
2. Buka di Android Studio Hedgehog+.
3. Gunakan JDK 17 atau 21.
4. Jalankan `./gradlew test` untuk verifikasi build.

---

<div align="center">

[📄 Lihat Project Plan Lengkap](PROJECT_PLAN.md)

</div>
