# 📓 Aurora Glass Notes App - AI-Powered Features Integration ✨

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-7C6EFA?style=for-the-badge&logo=kotlin&logoColor=white)
![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-2DD4BF?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Gemini AI](https://img.shields.io/badge/Gemini_AI-8E75B2?style=for-the-badge&logo=googlebard&logoColor=white)
![Ktor](https://img.shields.io/badge/Ktor-0854C7?style=for-the-badge&logo=ktor&logoColor=white)

**Tugas Praktikum Minggu 9 — IF25-22017 Pengembangan Aplikasi Mobile** **Program Studi Teknik Informatika · Institut Teknologi Sumatera**

</div>

---

## 👤 Identitas Mahasiswa
* **Nama**: Garis Rayya Rabbani
* **NIM**: 123140018
* **Kelas**: IF25-22017
* **Mata Kuliah**: Pengembangan Aplikasi Mobile (PAM)

---

## 📋 Ringkasan Implementasi
Pada iterasi ini, aplikasi **Aurora Glass Notes** telah berevolusi menjadi aplikasi cerdas (*AI-powered*) melalui integrasi **Google Gemini API** (Model: `gemini-2.5-flash`). Fitur AI dirancang untuk membantu pengguna mengelola catatan dengan lebih efisien, mulai dari merangkum teks panjang, memberikan *insight* cerdas (termasuk rekomendasi tema UI), hingga menganalisis gambar menggunakan teknologi *Optical Character Recognition* (OCR).

---

## 🛠️ Detail Teknis & Kepatuhan Rubrik (Minggu 9)

### 1. AI Integration (Bobot: 30%)
Aplikasi menggunakan **Google Gemini API** yang diimplementasikan dengan arsitektur *Clean Code* dan pemisahan *layer* yang baik.
* **`GeminiService`**: Bertindak sebagai *API Client* menggunakan **Ktor** untuk mengirim request HTTP POST ke endpoint Gemini secara *asynchronous*.
* **`AiRepository`**: Lapisan abstraksi yang mengelola *business logic* untuk 3 fitur utama: `summarizeNote()`, `getNoteInsights()`, dan `analyzeImage()`.

### 2. Prompt Engineering (Bobot: 25%)
Penerapan *System Prompt* tingkat lanjut (*Advanced Prompt Engineering*) untuk memastikan konsistensi output LLM:
* **Role & Task definition**: AI diberikan persona spesifik (contoh: *"Anda adalah analis cerdas untuk aplikasi News & Notes"*).
* **Structured Output (JSON)**: Pada fitur *Insight*, *prompt* dirancang secara ketat (*STRICT JSON*) agar Gemini mengembalikan respons dalam bentuk *Data Model* yang dapat di-*parse* (berisi judul, *key insights*, *actionable items*, dan rekomendasi *HEX color theme*).
* **Format Cleaning**: Menggunakan regex/string manipulation (`.replace("```json", "")`) untuk membersihkan *Markdown formatting* yang tidak diinginkan dari respons LLM.

### 3. Error Handling (Bobot: 20%)
Penanganan *error* yang rapi di seluruh lapisan aplikasi:
* **`NetworkResult` Sealed Class**: Membungkus respons ke dalam 3 *state* aman: `Loading`, `Success`, dan `Error`.
* **Graceful Degradation**: Jika koneksi gagal atau format JSON dari AI tidak valid, UI tidak akan *crash*, melainkan menampilkan *error state* yang estetik beserta tombol "Coba Lagi" (*retry logic*).

### 4. UI/UX (Bobot: 15%)
Antarmuka pengguna terintegrasi penuh dengan gaya **Glassmorphism**:
* **Responsive State**: Penggunaan `CircularProgressIndicator` saat memuat respons AI (*Loading state*).
* **Interactive UI**: Komponen `AiInsightCard` merender hasil analisis secara dinamis menjadi poin-poin yang mudah dibaca, *chip button* interaktif untuk *Action*, dan tombol aplikasi tema dinamis.
* **Haptic Feedback**: Menerapkan getaran (*haptic*) saat berinteraksi dengan tombol AI untuk *feedback* fisik yang lebih baik.

### 5. Bonus Points (+10%) 🌟
* **Image Analysis (Multimodal AI)**: Mengimplementasikan fitur OCR yang memanfaatkan kemampuan *multimodal* Gemini. Pengguna dapat mengunggah/mengirim data gambar (Base64), dan AI akan mengekstrak teks serta mendeskripsikan elemen visual penting di dalam gambar ke dalam format Markdown.

---

## 🏗️ Arsitektur Integrasi AI

| Komponen | Deskripsi |
|---|---|
| `GeminiService.kt` | *Service layer* untuk HTTP Request menggunakan Ktor Client. |
| `AiRepository.kt` | Menyimpan *System Prompts* dan mengelola konfigurasi LLM (Temperature, MimeType). |
| `AiInsight.kt` | Data model untuk *parsing* respons JSON terstruktur dari AI (Title, Insights, Actions, Theme). |
| `NoteViewModel.kt` | *State management* (`StateFlow`) yang menghubungkan UI dengan `AiRepository`. |
| `NoteDetailScreen.kt` | Komponen UI presentasi (Bottom Sheet & Card) untuk menampilkan respons AI. |

---

## 📸 Screenshots & Demo AI

*(Tambahkan screenshot atau GIF di sini saat UI AI sedang Loading, Menampilkan Rangkuman, dan Menampilkan Insight Card)*

| AI Summary (Loading & Success) | AI Insight Card & Auto-Theming |
|---|---|
| <img width="1440" height="3120" alt="Screenshot_20260503_224704" src="https://github.com/user-attachments/assets/0aca621b-742d-482a-9a29-5b439ddc24ae" /> | <img width="1440" height="3120" alt="Screenshot_20260503_224844" src="https://github.com/user-attachments/assets/7809abaa-d937-497d-b2a9-125ec458b86d" />|

---

## 📚 Referensi Materi
* **Materi 09**: Integrasi AI API (OpenAI, Gemini, Claude, dan Prompt Engineering) - ITERA.
* **Google AI for Developers**: [Gemini API Documentation](https://ai.google.dev/docs)
* **Ktor Client Documentation**: [Ktor](https://ktor.io/docs/)
