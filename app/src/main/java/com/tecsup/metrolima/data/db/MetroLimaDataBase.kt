package com.tecsup.metrolima.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tecsup.metrolima.data.dao.EstacionDao
import com.tecsup.metrolima.data.dao.LineaDao
import com.tecsup.metrolima.data.dao.RutaDao
import com.tecsup.metrolima.data.dao.TransbordoDao
import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.data.model.EstacionExtendida
import com.tecsup.metrolima.data.model.Linea
import com.tecsup.metrolima.data.model.Ruta
import com.tecsup.metrolima.data.model.Transbordo

@Suppress("DEPRECATION")
@Database(
    entities = [
        Estacion::class,           // 🔹 Mantienes compatibilidad con lo anterior
        EstacionExtendida::class,  // 🔹 Nueva versión extendida
        Linea::class,              // 🔹 Creada por Medrano
        Ruta::class,               // 🔹 Ya la tienes en tu app
        Transbordo::class          // 🔹 Nueva tabla de unión
    ],
    version = 8, // 🔄
    exportSchema = false
)
abstract class MetroLimaDataBase : RoomDatabase() {

    abstract fun estacionDao(): EstacionDao
    abstract fun rutaDao(): RutaDao
    abstract fun lineaDao(): LineaDao

    abstract fun transbordoDao(): TransbordoDao

    companion object {
        @Volatile
        private var INSTANCE: MetroLimaDataBase? = null

        fun getDatabase(context: Context): MetroLimaDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MetroLimaDataBase::class.java,
                    "metrolima_database"
                )
                    .fallbackToDestructiveMigration() // 🔹 Permite actualizar sin errores
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
