package org.garis.pam.data.repository

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.garis.pam.db.NotesDatabase
import kotlin.test.*

class NoteRepositoryTest {
    private lateinit var database: NotesDatabase
    private lateinit var repository: NoteRepository
    private lateinit var driver: JdbcSqliteDriver

    @BeforeTest
    fun setup() {
        driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        NotesDatabase.Schema.create(driver)
        database = NotesDatabase(driver)
        repository = NoteRepository(database)
    }

    @AfterTest
    fun tearDown() {
        driver.close()
    }

    @Test
    fun `save new note successfully`() = runTest {
        // Arrange
        val title = "Test Title"
        val content = "Test Content"

        // Act
        repository.insertNote(title, content)

        // Assert
        repository.getAllNotes().test {
            val notes = awaitItem()
            assertEquals(1, notes.size)
            assertEquals(title, notes[0].title)
            assertEquals(content, notes[0].content)
        }
    }

    @Test
    fun `get all notes returns list with sort order`() = runTest {
        // Arrange
        repository.insertNote("Note 1", "Content 1")
        repository.insertNote("Note 2", "Content 2")

        // Act & Assert
        repository.getAllNotes("newest").test {
            assertEquals(2, awaitItem().size)
        }
        repository.getAllNotes("oldest").test {
            assertEquals(2, awaitItem().size)
        }
    }

    @Test
    fun `search notes returns filtered list`() = runTest {
        repository.insertNote("Search Me", "Content")
        repository.insertNote("Ignore Me", "Content")

        repository.searchNotes("Search").test {
            val results = awaitItem()
            assertEquals(1, results.size)
            assertEquals("Search Me", results[0].title)
        }

        repository.searchNotes("Search", "oldest").test {
            assertEquals(1, awaitItem().size)
        }
    }

    @Test
    fun `get favorite notes returns favorites only`() = runTest {
        repository.insertNote("Fav", "Content")
        val id = database.noteQueries.selectAll().executeAsOne().id
        repository.toggleFavorite(id)

        repository.getFavoriteNotes().test {
            val favs = awaitItem()
            assertEquals(1, favs.size)
            assertTrue(favs[0].is_favorite == 1L)
        }
    }

    @Test
    fun `get archived notes returns archived only`() = runTest {
        repository.insertNote("Archived", "Content")
        val id = database.noteQueries.selectAll().executeAsOne().id
        repository.toggleArchive(id)

        repository.getArchivedNotes().test {
            assertEquals(1, awaitItem().size)
        }
    }

    @Test
    fun `get hidden notes returns hidden only`() = runTest {
        repository.insertNote("Hidden", "Content")
        val id = database.noteQueries.selectAll().executeAsOne().id
        repository.toggleHidden(id)

        repository.getHiddenNotes().test {
            assertEquals(1, awaitItem().size)
        }
    }

    @Test
    fun `toggle pin updates pin status`() = runTest {
        repository.insertNote("Pinned", "Content")
        val id = database.noteQueries.selectAll().executeAsOne().id
        repository.togglePin(id)

        val note = repository.getNoteById(id)
        assertTrue(note?.is_pinned == 1L)
    }

    @Test
    fun `get note detail by id`() = runTest {
        // Arrange
        repository.insertNote("Target Note", "Target Content")
        val notes = database.noteQueries.selectAll().executeAsList()
        val id = notes[0].id

        // Act
        val note = repository.getNoteById(id)

        // Assert
        assertNotNull(note)
        assertEquals("Target Note", note.title)
    }

    @Test
    fun `update existing note`() = runTest {
        // Arrange
        repository.insertNote("Old Title", "Old Content")
        val notes = database.noteQueries.selectAll().executeAsList()
        val id = notes[0].id

        // Act
        repository.updateNote(id, "New Title", "New Content", "tag1", "BLUE")

        // Assert
        val updatedNote = repository.getNoteById(id)
        assertNotNull(updatedNote)
        assertEquals("New Title", updatedNote.title)
        assertEquals("BLUE", updatedNote.color_name)
    }

    @Test
    fun `delete note from database`() = runTest {
        // Arrange
        repository.insertNote("To Delete", "Content")
        val notes = database.noteQueries.selectAll().executeAsList()
        val id = notes[0].id

        // Act
        repository.deleteNote(id)

        // Assert
        repository.getAllNotes().test {
            val remainingNotes = awaitItem()
            assertTrue(remainingNotes.isEmpty())
        }
    }
}
