package org.garis.pam

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.garis.pam.data.local.DatabaseDriverFactory

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Tugas3_ProfileApp",
    ) {
        App(databaseDriverFactory = DatabaseDriverFactory())
    }
}