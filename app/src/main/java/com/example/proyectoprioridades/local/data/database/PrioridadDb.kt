package com.example.proyectoprioridades.local.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.proyectoprioridades.local.data.converters.Converter
import com.example.proyectoprioridades.local.data.dao.PrioridadDao
import com.example.proyectoprioridades.local.data.dao.TicketDao
import com.example.proyectoprioridades.local.data.entities.PrioridadEntity
import com.example.proyectoprioridades.local.data.entities.TicketEntity

@Database(
    entities = [
        PrioridadEntity::class,
        TicketEntity::class

    ],
    version = 4,
    exportSchema = false
)

@TypeConverters(Converter::class)
abstract class  PrioridadDb: RoomDatabase(){
    abstract fun PrioridadDao(): PrioridadDao

    abstract fun TicketDao(): TicketDao

}