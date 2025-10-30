package com.tecsup.metrolima.data.db // Esto debería ser lo primero

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.data.dao.EstacionDao
import com.tecsup.metrolima.data.dao.LineaDao
import com.tecsup.metrolima.data.dao.RutaDao
import com.tecsup.metrolima.data.model.Linea
import com.tecsup.metrolima.data.model.Ruta

@Suppress("DEPRECATION")
@Database(
    entities = [Estacion::class, Ruta::class, Linea::class], // Añadiendo Modelo Línea
    version = 5,
    exportSchema = false
)
abstract class MetroLimaDataBase : RoomDatabase() {

    abstract fun estacionDao(): EstacionDao
    abstract fun rutaDao(): RutaDao
    abstract fun lineaDao(): LineaDao // Dao Para las Lineas

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
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}