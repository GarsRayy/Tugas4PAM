package org.garis.pam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.garis.pam.data.local.DatabaseDriverFactory

import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(databaseDriverFactory = koinInject())
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(databaseDriverFactory = DatabaseDriverFactory(androidx.compose.ui.platform.LocalContext.current))
}