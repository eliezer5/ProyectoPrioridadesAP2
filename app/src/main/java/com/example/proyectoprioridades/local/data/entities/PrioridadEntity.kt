package com.example.proyectoprioridades.local.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Prioridades")
class PrioridadEntity (
    @PrimaryKey
    val prioridadId: Int? = null,
    val descripcion: String = "",
    val diasCompromiso: Int
)