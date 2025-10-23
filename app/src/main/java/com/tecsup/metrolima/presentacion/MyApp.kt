package com.tecsup.metrolima.presentacion

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.tecsup.metrolima.presentacion.utils.updateLocale
import java.util.Locale

val LocalAppContext = compositionLocalOf<Context> { error("No Context provided") }

@Composable
fun MyApp(
    selectedLanguage: String,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val locale = when (selectedLanguage) {
        "Español" -> Locale("es")
        "Inglés" -> Locale("en")
        else -> Locale.getDefault()
    }

    val updatedContext = remember(locale) {
        context.updateLocale(locale)
    }

    CompositionLocalProvider(LocalAppContext provides updatedContext) {
        content()
    }
}
