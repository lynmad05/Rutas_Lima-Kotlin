package com.tecsup.metrolima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.tecsup.metrolima.navigation.NavGraph
import com.tecsup.metrolima.ui.theme.MetroLimaGoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MetroLimaGoTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
