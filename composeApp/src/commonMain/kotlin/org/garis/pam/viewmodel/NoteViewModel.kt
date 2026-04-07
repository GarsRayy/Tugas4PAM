package org.garis.pam.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.garis.pam.data.Note
import org.garis.pam.data.NoteColor
import org.garis.pam.data.sampleNotes

data class NoteUiState(
    val notes: List<Note> = sampleNotes,
    val selectedNote: Note? = null,
    val editTitle: String = "",
    val editContent: String = ""
)

class NoteViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    // Ambil note berdasarkan id
    fun getNoteById(id: Int): Note? =
        _uiState.value.notes.find { it.id == id }

    // Pilih note untuk dilihat detailnya
    fun selectNote(note: Note) {
        _uiState.update { it.copy(
            selectedNote = note,
            editTitle   = note.title,
            editContent = note.content
        )}
    }

    // Toggle favorite
    fun toggleFavorite(noteId: Int) {
        _uiState.update { state ->
            state.copy(
                notes = state.notes.map { note ->
                    if (note.id == noteId) note.copy(isFavorite = !note.isFavorite)
                    else note
                }
            )
        }
    }

    // Update field saat edit (state hoisting)
    fun onTitleChange(title: String) {
        _uiState.update { it.copy(editTitle = title) }
    }
    fun onContentChange(content: String) {
        _uiState.update { it.copy(editContent = content) }
    }

    // Simpan perubahan note
    fun saveNote(noteId: Int) {
        _uiState.update { state ->
            state.copy(
                notes = state.notes.map { note ->
                    if (note.id == noteId) note.copy(
                        title   = state.editTitle.ifBlank { note.title },
                        content = state.editContent.ifBlank { note.content }
                    ) else note
                }
            )
        }
    }

    // Tambah note baru
    fun addNote(title: String, content: String, color: NoteColor = NoteColor.VIOLET) {
        val newNote = Note(
            id        = (_uiState.value.notes.maxOfOrNull { it.id } ?: 0) + 1,
            title     = title,
            content   = content,
            color     = color,
            createdAt = "Baru saja"
        )
        _uiState.update { it.copy(notes = it.notes + newNote) }
    }

    // Hapus note
    fun deleteNote(noteId: Int) {
        _uiState.update { state ->
            state.copy(notes = state.notes.filter { it.id != noteId })
        }
    }

    // Note yang difavoritkan
    fun getFavoriteNotes() = _uiState.value.notes.filter { it.isFavorite }
}