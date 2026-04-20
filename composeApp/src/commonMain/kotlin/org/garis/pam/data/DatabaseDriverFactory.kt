package org.garis.pam.data

import app.cash.sqldelight.db.SqlDriver

// Expect class: "Kontrak" bahwa setiap platform harus punya cara buat bikin Driver
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
