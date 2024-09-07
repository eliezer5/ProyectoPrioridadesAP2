package com.example.proyectoprioridades.local.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.proyectoprioridades.local.data.dao.PrioridadDao
import com.example.proyectoprioridades.local.data.entities.PrioridadEntity

@Database(
    entities = [
        PrioridadEntity::class
    ],
    version = 1,
    exportSchema = false
)


abstract class  PrioridadDb: RoomDatabase(){
    abstract fun PrioridadDao(): PrioridadDao

}