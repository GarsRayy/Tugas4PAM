package org.garis.pam.ui.screens.notes

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.garis.pam.db.NoteEntity
import org.garis.pam.viewmodel.NoteViewModel
import org.garis.pam.viewmodel.SettingsViewModel
import org.garis.pam.platform.NetworkMonitor
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.junit.Before
import org.junit.After

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class NoteListScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val viewModel: NoteViewModel = mockk(relaxed = true)
    private val settingsViewModel: SettingsViewModel = mockk(relaxed = true)
    private val networkMonitor: NetworkMonitor = mockk(relaxed = true)

    private val notesFlow = MutableStateFlow<List<NoteEntity>>(emptyList())
    private val searchQueryFlow = MutableStateFlow("")
    private val sortOrderFlow = MutableStateFlow("newest")
    private val isConnectedFlow = MutableStateFlow(true)

    @Before
    fun setup() {
        stopKoin()
        every { viewModel.notes } returns notesFlow
        every { viewModel.searchQuery } returns searchQueryFlow
        every { settingsViewModel.currentSortOrder } returns sortOrderFlow
        every { networkMonitor.isConnected } returns isConnectedFlow

        try {
            startKoin {
                modules(module {
                    single { networkMonitor }
                })
            }
        } catch (e: Exception) {}
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Test
    fun `displays empty state when no notes`() {
        notesFlow.value = emptyList()

        composeTestRule.setContent {
            SharedTransitionLayout {
                AnimatedVisibility(visible = true) {
                    NoteListScreen(
                        viewModel = viewModel,
                        settingsViewModel = settingsViewModel,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedVisibility,
                        onNoteClick = {},
                        onAddClick = {},
                        onToggleFavorite = {},
                        onTogglePin = {},
                        onArchiveClick = {},
                        onHiddenClick = {},
                        networkMonitor = networkMonitor
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("empty_state_text").assertIsDisplayed()
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Test
    fun `displays list when notes available`() {
        notesFlow.value = listOf(
            NoteEntity(1, "Test Note", "Content", 0, 0, 0, 0, "", "VIOLET", 0, 0)
        )

        composeTestRule.setContent {
            SharedTransitionLayout {
                AnimatedVisibility(visible = true) {
                    NoteListScreen(
                        viewModel = viewModel,
                        settingsViewModel = settingsViewModel,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedVisibility,
                        onNoteClick = {},
                        onAddClick = {},
                        onToggleFavorite = {},
                        onTogglePin = {},
                        onArchiveClick = {},
                        onHiddenClick = {},
                        networkMonitor = networkMonitor
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("note_list").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Note").assertIsDisplayed()
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Test
    fun `click add button triggers callback`() {
        var addClicked = false
        composeTestRule.setContent {
            SharedTransitionLayout {
                AnimatedVisibility(visible = true) {
                    NoteListScreen(
                        viewModel = viewModel,
                        settingsViewModel = settingsViewModel,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedVisibility,
                        onNoteClick = {},
                        onAddClick = { addClicked = true },
                        onToggleFavorite = {},
                        onTogglePin = {},
                        onArchiveClick = {},
                        onHiddenClick = {},
                        networkMonitor = networkMonitor
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("add_note_button").performClick()
        assert(addClicked)
    }
}
