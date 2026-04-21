package org.garis.pam.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.garis.pam.db.NotesDatabase
import org.garis.pam.db.NoteEntity

class NoteRepository(database: NotesDatabase) {
    private val queries = database.noteQueries

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

    fun getFavoriteNotes(): Flow<List<NoteEntity>> {
        return queries.selectFavorites()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    fun getArchivedNotes(): Flow<List<NoteEntity>> {
        return queries.selectArchived()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    suspend fun getNoteById(id: Long): NoteEntity? {
        return withContext(Dispatchers.Default) {
            queries.selectById(id).executeAsOneOrNull()
        }
    }

    suspend fun insertNote(title: String, content: String, tags: String = "", colorName: String = "VIOLET") {
        val now = Clock.System.now().toEpochMilliseconds()
        withContext(Dispatchers.Default) {
            queries.insert(title, content, 0L, 0L, 0L, tags, colorName, now, now)
        }
    }

    suspend fun updateNote(id: Long, title: String, content: String, tags: String, colorName: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        withContext(Dispatchers.Default) {
            queries.update(title, content, colorName, tags, now, id)
        }
    }

    suspend fun toggleFavorite(id: Long) {
        withContext(Dispatchers.Default) {
            queries.toggleFavorite(id)
        }
    }

    suspend fun togglePin(id: Long) {
        withContext(Dispatchers.Default) {
            queries.togglePinned(id)
        }
    }

    suspend fun toggleArchive(id: Long) {
        withContext(Dispatchers.Default) {
            queries.toggleArchived(id)
        }
    }

    suspend fun deleteNote(id: Long) {
        withContext(Dispatchers.Default) {
            queries.delete(id)
        }
    }
}
