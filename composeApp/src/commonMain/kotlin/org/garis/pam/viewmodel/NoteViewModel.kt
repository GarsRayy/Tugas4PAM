package org.garis.pam.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.garis.pam.core.network.NetworkResult
import org.garis.pam.data.model.remote.AiInsightResponse
import org.garis.pam.data.model.remote.AiAction
import org.garis.pam.data.repository.AiRepository
import org.garis.pam.data.repository.NoteRepository
import org.garis.pam.db.NoteEntity

class NoteViewModel(
    private val repository: NoteRepository,
    private val aiRepository: AiRepository
) : ViewModel() {

    var aiSummaryState by mutableStateOf<NetworkResult<String>?>(null)
        private set

    var aiInsightState by mutableStateOf<NetworkResult<AiInsightResponse>?>(null)
        private set

    var aiImageAnalysisState by mutableStateOf<NetworkResult<String>?>(null)
        private set

    fun analyzeImage(base64Image: String, mimeType: String) {
        viewModelScope.launch {
            aiImageAnalysisState = NetworkResult.Loading
            aiImageAnalysisState = aiRepository.analyzeImage(base64Image, mimeType)
        }
    }

    fun summarizeNote(content: String) {
        viewModelScope.launch {
            aiSummaryState = NetworkResult.Loading
            aiSummaryState = aiRepository.summarizeNote(content)
        }
    }

    fun getInsights(content: String) {
        viewModelScope.launch {
            aiInsightState = NetworkResult.Loading
            aiInsightState = aiRepository.getNoteInsights(content)
        }
    }

    fun resetAiState() {
        aiSummaryState = null
        aiInsightState = null
        aiImageAnalysisState = null
    }

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

    val hiddenNotes: StateFlow<List<NoteEntity>> = repository.getHiddenNotes()
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

    fun toggleHidden(id: Long) {
        viewModelScope.launch {
            repository.toggleHidden(id)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    fun performAction(action: AiAction) {
        when (action.type) {
            "TODO" -> {
                saveNote(
                    title = "Tugas: ${action.label}",
                    content = "- [ ] ${action.value}",
                    tags = "#todo",
                    colorName = "GOLD"
                )
            }
            "SEARCH" -> {
                updateSearchQuery(action.value)
            }
            "CALENDAR" -> {
                // Future implementation: Calendar integration
            }
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
