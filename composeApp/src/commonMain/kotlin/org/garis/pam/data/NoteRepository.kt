package org.garis.pam.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.garis.pam.db.NotesDatabase
import org.garis.pam.db.NoteEntity // Model hasil generate SQLDelight

class NoteRepository(database: NotesDatabase) {
    // NoteQueries di-generate otomatis dari file Note.sq
    private val queries = database.noteQueries

    // READ: Mengambil semua catatan secara reaktif
    fun getAllNotes(sortOrder: String = "newest"): Flow<List<NoteEntity>> {
        return if (sortOrder == "oldest") {
            queries.selectAllOldest()
                .asFlow()
                .mapToList(Dispatchers.Default)
        } else {
            queries.selectAll()
                .asFlow()
                .mapToList(Dispatchers.Default)
        }
    }

    // SEARCH: Mencari catatan berdasarkan judul atau isi (Fitur Wajib Tugas)
    fun searchNotes(query: String, sortOrder: String = "newest"): Flow<List<NoteEntity>> {
        return if (sortOrder == "oldest") {
            queries.searchOldest(query)
                .asFlow()
                .mapToList(Dispatchers.Default)
        } else {
            queries.search(query)
                .asFlow()
                .mapToList(Dispatchers.Default)
        }
    }

    // READ: Mengambil catatan favorit
    fun getFavoriteNotes(): Flow<List<NoteEntity>> {
        return queries.selectFavorites()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    // READ: Mengambil catatan yang diarsip
    fun getArchivedNotes(): Flow<List<NoteEntity>> {
        return queries.selectArchived()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    // READ: Mengambil 1 catatan spesifik
    suspend fun getNoteById(id: Long): NoteEntity? {
        return withContext(Dispatchers.Default) {
            queries.selectById(id).executeAsOneOrNull()
        }
    }

    // CREATE: Menambahkan catatan baru
    suspend fun insertNote(title: String, content: String, tags: String = "", colorName: String = "VIOLET") {
        val now = Clock.System.now().toEpochMilliseconds()
        withContext(Dispatchers.Default) {
            queries.insert(title, content, 0L, 0L, 0L, tags, colorName, now, now)
        }
    }

    // UPDATE: Memperbarui catatan yang sudah ada
    suspend fun updateNote(id: Long, title: String, content: String, tags: String, colorName: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        withContext(Dispatchers.Default) {
            queries.update(title, content, colorName, tags, now, id)
        }
    }

    // TOGGLE FAVORITE
    suspend fun toggleFavorite(id: Long) {
        withContext(Dispatchers.Default) {
            queries.toggleFavorite(id)
        }
    }

    // TOGGLE PIN
    suspend fun togglePin(id: Long) {
        withContext(Dispatchers.Default) {
            queries.togglePinned(id)
        }
    }

    // TOGGLE ARCHIVE
    suspend fun toggleArchive(id: Long) {
        withContext(Dispatchers.Default) {
            queries.toggleArchived(id)
        }
    }

    // DELETE: Menghapus catatan
    suspend fun deleteNote(id: Long) {
        withContext(Dispatchers.Default) {
            queries.delete(id)
        }
    }
}
