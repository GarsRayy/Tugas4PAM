# 📓 Aurora Glass Notes App - AI-Powered Features Integration ✨

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-7C6EFA?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-2DD4BF?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Gemini AI](https://img.shields.io/badge/Gemini_AI-8E75B2?style=for-the-badge&logo=googlebard&logoColor=white)
![Ktor](https://img.shields.io/badge/Ktor-0854C7?style=for-the-badge&logo=ktor&logoColor=white)

**Tugas Praktikum Minggu 10 — IF25-22017 Pengembangan Aplikasi Mobile**  
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

Aplikasi ini mengimplementasikan fitur catatan (*notes*) dengan integrasi AI, serta sistem pengujian yang komprehensif menggunakan **MockK**, **Turbine**, dan **Kover**.

---

## ✨ Fitur Utama (Minggu 10)

### 🏗️ Koin Dependency Injection (Bobot 20%)
- **Modularisasi DI**: Memisahkan `commonModule` menjadi `dataModule` (database & repositori) dan `viewModelModule` (semua ViewModel).
- **Inisialisasi Terpusat**: Penggabungan modul dalam `KoinHelper.kt` dan pembersihan *lifecycle* di `MainApplication.kt`.

### 🧪 Unit Testing & Flow Testing (Bobot 55%)
- **Clean Code & AAA Pattern**: Penulisan pengujian yang rapi dengan pola *Arrange, Act, Assert*.
- **NoteRepositoryTest (11 Cases)**: Mencakup CRUD lengkap, fungsi pencarian, dan fitur toggle (arsip, pin, favorit, sembunyi).
- **NoteViewModelTest (4 Cases)**: Menggunakan **MockK** untuk mengisolasi logika ViewModel dan memverifikasi interaksi dengan repositori.
- **Flow Testing (Turbine)**: Memastikan aliran data pada `StateFlow` dan `Flow` database berjalan sesuai ekspektasi.

### 🖼️ UI Testing dengan Compose (Bobot 15%)
- **Test Tags Implementation**: Menambahkan `Modifier.testTag` pada komponen UI kritis untuk selektor yang stabil.
- **NoteListScreenTest (3 Cases)**: 
    - Verifikasi *Empty State* ("Kosong") saat data tidak ada.
    - Verifikasi daftar catatan muncul saat data tersedia.
    - Verifikasi interaksi tombol tambah (*FloatingActionButton*).
- **Robolectric Integration**: Menjalankan UI tes di level unit test untuk kecepatan eksekusi.

### 📊 Code Coverage (Bonus +10%)
- **Kover Integration**: Menggunakan plugin `kotlinx-kover` untuk pemantauan cakupan kode.
- **Cakupan > 80%**: Fokus pengujian pada lapisan repositori dan ViewModel untuk menjamin integritas logika bisnis.

---

## 🏛 Arsitektur & Struktur Project

```
composeApp/src/
├── commonMain/kotlin/org/garis/pam/
│   ├── 📁 data/
│   │   ├── 📁 repository/
│   │   │   ├── NoteRepository.kt
│   │   │   └── AiRepository.kt
│   ├── 📁 di/
│   │   ├── AppModule.kt        # dataModule & viewModelModule
│   │   └── KoinHelper.kt
│   ├── 📁 viewmodel/
│   │   └── NoteViewModel.kt
│   └── 📁 ui/
│       └── screens/notes/
│           └── NoteListScreen.kt # + testTags
│
├── androidUnitTest/kotlin/org/garis/pam/
│   ├── 📁 data/repository/
│   │   └── NoteRepositoryTest.kt # 11 cases
│   ├── 📁 viewmodel/
│   │   └── NoteViewModelTest.kt  # 4 cases
│   └── 📁 ui/screens/notes/
│       └── NoteListScreenTest.kt # 3 cases
```

---

## 🛠️ Cara Menjalankan Test

Karena adanya perbedaan versi JDK pada lingkungan terminal tertentu, disarankan menggunakan JDK dari Android Studio:

### 1. Menjalankan Semua Test
```bash
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio1\jbr"; ./gradlew clean :composeApp:testDebugUnitTest
```

### 2. Menghasilkan Laporan Coverage (HTML)
```bash
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio1\jbr"; ./gradlew :composeApp:koverHtmlReportDebug
```
Laporan dapat dibuka di: `composeApp/build/reports/kover/htmlDebug/index.html`

---

## 📝 Daftar Test Case

### NoteRepository
1. `save new note successfully` - AAA Pattern
2. `get all notes returns list with sort order`
3. `search notes returns filtered list`
4. `get favorite notes returns favorites only`
5. `get archived notes returns archived only`
6. `get hidden notes returns hidden only`
7. `toggle pin updates pin status`
8. `get note detail by id`
9. `update existing note`
10. `delete note from database`
11. `flow getAllNotes emits when new data added` (Turbine)

### NoteViewModel
1. `notes state collects from repository` (Turbine)
2. `save note triggers repository insert` (MockK)
3. `delete note triggers repository delete` (MockK)
4. `notes state reflects repository changes` (Turbine)

### NoteListScreen (UI)
1. `displays empty state when no notes`
2. `displays list when notes available`
3. `click add button triggers callback`

---

## 📸 Bukti Pengerjaan



| Test Execution (18 Passed) | Code Coverage (>80%) |
|---|---|
| <img width="1254" height="579" alt="Cuplikan layar 2026-05-10 181050" src="https://github.com/user-attachments/assets/0ff6e7aa-9124-47ee-9d03-da96e52b5f41" /> | <img width="1365" height="611" alt="Cuplikan layar 2026-05-10 180938" src="https://github.com/user-attachments/assets/15fc016a-1082-43d7-b112-70bb68b20231" /> |

---


