package com.tecsup.metrolima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.tecsup.metrolima.navigation.NavGraph
import com.tecsup.metrolima.presentacion.MyApp
import com.tecsup.metrolima.ui.theme.MetroLimaGoTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fun updateLocale(languageCode: String) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)
            val config = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)
        }

        setContent {
            var darkModeEnabled by remember { mutableStateOf(false) }
            var selectedLanguage by remember { mutableStateOf("Español") }
            val navController = rememberNavController()

            MyApp(selectedLanguage = selectedLanguage) {
                MetroLimaGoTheme(darkTheme = darkModeEnabled) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavGraph(
                            navController = navController,
                            darkModeEnabled = darkModeEnabled,
                            onDarkModeChange = { darkModeEnabled = it },
                            selectedLanguage = selectedLanguage,
                            onLanguageChange = { lang ->
                                selectedLanguage = lang
                                val code = when (lang) {
                                    "Español" -> "es"
                                    "Inglés" -> "en"
                                    else -> "es"
                                }
                                updateLocale(code)
                            }
                        )
                    }
                }
            }
        }
    }
}

