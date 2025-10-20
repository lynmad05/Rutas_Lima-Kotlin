package com.tecsup.metrolima.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tecsup.metrolima.data.dao.EstacionDao
import com.tecsup.metrolima.data.model.Estacion

@Database(entities = [Estacion::class], version = 1, exportSchema = false)
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
                    "metro_lima_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
