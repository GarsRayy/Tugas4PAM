package org.garis.pam.data

data class Note(
    val id: Int,
    val title: String,
    val content: String,
    val isFavorite: Boolean = false,
    val color: NoteColor = NoteColor.VIOLET,
    val createdAt: String = ""
)

enum class NoteColor { VIOLET, TEAL, PINK, GOLD, SKY }

val sampleNotes = listOf(
    Note(1, "Belajar Kotlin",
        "Kotlin adalah bahasa pemrograman modern yang berjalan di JVM. Sangat cocok untuk Android development.",
        false, NoteColor.VIOLET, "Hari ini"),
    Note(2, "Compose Multiplatform",
        "KMP memungkinkan satu codebase untuk Android, iOS, dan Desktop menggunakan Compose UI.",
        true, NoteColor.TEAL, "Kemarin"),
    Note(3, "Tugas Praktikum",
        "Minggu 5: Implementasi Navigation dengan NavHost, NavController, dan passing arguments.",
        false, NoteColor.PINK, "2 hari lalu"),
    Note(4, "Tips MVVM",
        "ViewModel menyimpan UI state dengan StateFlow. Jangan pass NavController ke ViewModel!",
        true, NoteColor.GOLD, "3 hari lalu"),
    Note(5, "Aurora Glass UI",
        "Glassmorphism + gradient mesh + dark/light mode = tampilan premium di Compose.",
        false, NoteColor.SKY, "Seminggu lalu"),
)