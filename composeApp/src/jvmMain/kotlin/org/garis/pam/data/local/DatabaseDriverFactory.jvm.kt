package org.garis.pam.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.garis.pam.db.NotesDatabase
import java.io.File

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        // Menyimpan database di folder user desktop
        val databasePath = File(System.getProperty("user.home"), "notes.db")
        val driver = JdbcSqliteDriver(url = "jdbc:sqlite:${databasePath.absolutePath}")
        
        // Buat tabel jika belum ada
        if (!databasePath.exists()) {
            NotesDatabase.Schema.create(driver)
        }
        return driver
    }
}
