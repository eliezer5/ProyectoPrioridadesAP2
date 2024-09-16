package com.example.proyectoprioridades.local.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Date


@Entity(tableName = "Tickets")
class TicketEntity(
    @PrimaryKey
    val ticketId: Int? = null,
    val fecha: String = "",
    val prioridadId: Int? = null,
    val cliente: String = "",
    val asunto: String = "",
    val descripcion: String = ""

)