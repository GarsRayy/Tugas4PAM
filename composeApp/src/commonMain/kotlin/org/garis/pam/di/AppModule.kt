package org.garis.pam.di

import org.garis.pam.data.local.DatabaseDriverFactory
import org.garis.pam.data.local.SettingsManager
import org.garis.pam.data.remote.HttpClientFactory
import org.garis.pam.data.repository.NewsRepository
import org.garis.pam.data.repository.NoteRepository
import org.garis.pam.db.NotesDatabase
import org.garis.pam.viewmodel.NewsViewModel
import org.garis.pam.viewmodel.NoteViewModel
import org.garis.pam.viewmodel.ProfileViewModel
import org.garis.pam.viewmodel.SettingsViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

val commonModule = module {
    single { NotesDatabase(get<DatabaseDriverFactory>().createDriver()) }
    single { NoteRepository(get()) }
    single { NewsRepository(HttpClientFactory.create()) }
    single { SettingsManager() }

    viewModelOf(::NoteViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::NewsViewModel)
    viewModelOf(::ProfileViewModel)
}

expect val platformModule: Module
