package com.tecsup.metrolima.data.db // Esto debería ser lo primero

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.data.db.EstacionDao

@Database(entities = [Estacion::class], version = 2, exportSchema = false) // Revisa tu versión
abstract class MetroLimaDataBase : RoomDatabase() {

    abstract fun estacionDao(): EstacionDao

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