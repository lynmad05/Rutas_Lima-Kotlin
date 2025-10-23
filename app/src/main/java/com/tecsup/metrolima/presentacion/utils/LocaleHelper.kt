package com.tecsup.metrolima.presentacion.utils


import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

fun Context.updateLocale(locale: Locale): Context {
    val config = Configuration(resources.configuration)
    Locale.setDefault(locale)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        config.setLocale(locale)
        return createConfigurationContext(config)
    } else {
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
        return this
    }
}
