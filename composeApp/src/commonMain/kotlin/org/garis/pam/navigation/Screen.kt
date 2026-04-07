package org.garis.pam.navigation

// Sealed class untuk semua routes (best practice dari materi)
sealed class Screen(val route: String) {

    // ── Bottom Nav tabs ──
    object Notes     : Screen("notes")
    object Favorites : Screen("favorites")
    object Profile   : Screen("profile")

    // ── Notes stack ──
    object NoteList   : Screen("note_list")
    object AddNote    : Screen("add_note")

    // NoteDetail & EditNote pakai argument noteId
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }
    object EditNote   : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Int) = "edit_note/$noteId"
    }
}