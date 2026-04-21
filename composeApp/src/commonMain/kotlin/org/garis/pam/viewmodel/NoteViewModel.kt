package org.garis.pam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.garis.pam.data.repository.NoteRepository
import org.garis.pam.db.NoteEntity

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    // Menyimpan state text pencarian dan urutan sortir
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _sortOrder = MutableStateFlow("newest")
    val sortOrder: StateFlow<String> = _sortOrder.asStateFlow()

    // Menggabungkan aliran data database dengan input pencarian dan sortir
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val notes: StateFlow<List<NoteEntity>> = combine(_searchQuery, _sortOrder) { query, order ->
        query to order
    }.flatMapLatest { (query, order) ->
        if (query.isBlank()) {
            repository.getAllNotes(order)
        } else {
            repository.searchNotes(query, order)
        }
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favoriteNotes: StateFlow<List<NoteEntity>> = repository.getFavoriteNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val archivedNotes: StateFlow<List<NoteEntity>> = repository.getArchivedNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedNote = MutableStateFlow<NoteEntity?>(null)
    val selectedNote: StateFlow<NoteEntity?> = _selectedNote.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSortOrder(order: String) {
        _sortOrder.value = order
    }

    fun saveNote(title: String, content: String, tags: String = "", colorName: String = "VIOLET") {
        viewModelScope.launch {
            val currentNote = _selectedNote.value
            if (currentNote == null) {
                repository.insertNote(title, content, tags, colorName)
            } else {
                repository.updateNote(currentNote.id, title, content, tags, colorName)
            }
            // Kosongkan pilihan setelah disimpan
            clearSelectedNote()
        }
    }

    fun toggleFavorite(id: Long) {
        viewModelScope.launch {
            repository.toggleFavorite(id)
        }
    }

    fun togglePin(id: Long) {
        viewModelScope.launch {
            repository.togglePin(id)
        }
    }

    fun toggleArchive(id: Long) {
        viewModelScope.launch {
            repository.toggleArchive(id)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    fun selectNote(id: Long) {
        viewModelScope.launch {
            _selectedNote.value = repository.getNoteById(id)
        }
    }

    fun clearSelectedNote() {
        _selectedNote.value = null
    }
}
