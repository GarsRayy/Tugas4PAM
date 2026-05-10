package org.garis.pam.viewmodel

import app.cash.turbine.test
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.garis.pam.data.repository.NoteRepository
import org.garis.pam.data.repository.AiRepository
import org.garis.pam.db.NoteEntity
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class NoteViewModelTest {
    private lateinit var repository: NoteRepository
    private lateinit var aiRepository: AiRepository
    private lateinit var viewModel: NoteViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        aiRepository = mockk(relaxed = true)
        
        // Mock default behavior for init flows
        every { repository.getAllNotes(any()) } returns flowOf(emptyList())
        every { repository.getFavoriteNotes() } returns flowOf(emptyList())
        every { repository.getArchivedNotes() } returns flowOf(emptyList())
        every { repository.getHiddenNotes() } returns flowOf(emptyList())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `notes state collects from repository`() = runTest(testDispatcher) {
        // Arrange
        val mockNotes = listOf(
            NoteEntity(1, "Title", "Content", 0, 0, 0, 0, "", "VIOLET", 0, 0)
        )
        every { repository.getAllNotes(any()) } returns flowOf(mockNotes)

        viewModel = NoteViewModel(repository, aiRepository)

        // Act & Assert using Turbine
        viewModel.notes.test {
            val item = awaitItem()
            assertEquals(1, item.size)
            assertEquals("Title", item[0].title)
        }
    }

    @Test
    fun `save note triggers repository insert with correct parameters`() = runTest(testDispatcher) {
        // Arrange
        viewModel = NoteViewModel(repository, aiRepository)
        val title = "New Note"
        val content = "Content"
        val tags = "tag"
        val color = "VIOLET"
        coEvery { repository.insertNote(any(), any(), any(), any()) } returns Unit

        // Act
        viewModel.saveNote(title, content, tags, color)

        // Assert
        coVerify { repository.insertNote(title, content, tags, color) }
    }

    @Test
    fun `delete note triggers repository delete`() = runTest(testDispatcher) {
        // Arrange
        viewModel = NoteViewModel(repository, aiRepository)
        val noteId = 1L
        coEvery { repository.deleteNote(noteId) } returns Unit

        // Act
        viewModel.deleteNote(noteId)

        // Assert
        coVerify { repository.deleteNote(noteId) }
    }

    @Test
    fun `notes state reflects repository changes`() = runTest(testDispatcher) {
        // Arrange
        val mockNotes = listOf(
            NoteEntity(1, "Title", "Content", 0, 0, 0, 0, "", "VIOLET", 0, 0)
        )
        every { repository.getAllNotes(any()) } returns flowOf(mockNotes)
        viewModel = NoteViewModel(repository, aiRepository)

        // Act & Assert
        viewModel.notes.test {
            val item = awaitItem()
            assertEquals(1, item.size)
        }
    }
}
