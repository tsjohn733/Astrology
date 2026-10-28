package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.screens.MainScreen
import com.example.ui.theme.CelestialNavy
import com.example.ui.theme.JyotishVedaTheme
import com.example.ui.viewmodel.AstrologyViewModel
import com.example.util.configureAppCheck

class MainActivity : ComponentActivity() {

    private val astrologyViewModel: AstrologyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure Firebase App Check with launch intent before UI/Firebase calls
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        setContent {
            JyotishVedaTheme {
                Surface(
                    color = CelestialNavy,
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainScreen(viewModel = astrologyViewModel)
                }
            }
        }
    }
}
