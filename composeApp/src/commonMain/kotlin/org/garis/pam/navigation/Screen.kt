package org.garis.pam.navigation

// Sealed class untuk semua routes (best practice dari materi)
sealed class Screen(val route: String) {

    // ── Bottom Nav tabs ──
    object Notes     : Screen("notes")
    object Favorites : Screen("favorites")
    object Profile   : Screen("profile")
    object News : Screen("news")
    object Settings : Screen("settings")
    object NewsDetail : Screen("news_detail")
    object Archive : Screen("archive")

    // ── Notes stack ──
    object NoteList   : Screen("note_list")
    object AddNote    : Screen("add_note")

    // NoteDetail & EditNote pakai argument noteId
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Long) = "note_detail/$noteId"
    }
    object EditNote   : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Long) = "edit_note/$noteId"
    }
}