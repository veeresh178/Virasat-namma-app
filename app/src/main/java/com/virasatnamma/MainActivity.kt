package com.virasatnamma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.virasatnamma.data.local.VirasatDatabase
import com.virasatnamma.navigation.AppNavigation
import com.virasatnamma.ui.theme.VirasatNammaTheme

/**
 * Main Activity - Entry point for Virasat-Namma app
 * Initializes database and sets up Jetpack Compose UI
 */
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        
        // Initialize database
        val database = VirasatDatabase.getDatabase(this)
        
        setContent {
            VirasatNammaTheme {
                AppNavigation(database)
            }
        }
    }
}
