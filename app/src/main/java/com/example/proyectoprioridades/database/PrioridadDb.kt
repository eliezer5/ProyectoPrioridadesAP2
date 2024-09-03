package com.example.proyectoprioridades.database

import androidx.room.Database
import com.example.proyectoprioridades.dao.PrioridadDao
import com.example.proyectoprioridades.entities.PrioridadEntity

@Database(
    entities = [
        PrioridadEntity::class
    ],
    version = 1,
    exportSchema = false
)


abstract class  PrioridadDb{
    abstract fun PrioridadDao(): PrioridadDao

}