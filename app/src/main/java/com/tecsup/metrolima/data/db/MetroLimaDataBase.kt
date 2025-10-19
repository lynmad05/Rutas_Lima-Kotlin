package com.tecsup.metrolima.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tecsup.metrolima.data.dao.EstacionDao
import com.tecsup.metrolima.data.model.Estacion

@Database(entities = [Estacion::class], version = 1)
abstract class MetroLimaDataBase : RoomDatabase(){
    abstract fun estacionDao(): EstacionDao
}