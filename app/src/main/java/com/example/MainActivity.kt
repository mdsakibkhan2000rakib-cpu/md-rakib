package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.theme.CyberBgDark
import com.example.ui.theme.RakibGameLauncherTheme

class MainActivity : ComponentActivity() {

    private val launcherViewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RakibGameLauncherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CyberBgDark
                ) {
                    LauncherScreen(viewModel = launcherViewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        launcherViewModel.checkGameInstallation()
        launcherViewModel.refreshHardwareStats()
    }
}
