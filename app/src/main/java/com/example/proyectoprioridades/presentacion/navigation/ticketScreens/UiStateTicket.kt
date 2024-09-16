package com.example.proyectoprioridades.presentacion.navigation.ticketScreens

import com.example.proyectoprioridades.local.data.entities.TicketEntity
import com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens.UiState
import java.time.LocalDate
import java.util.Date

data class UiStateTicket(
    val ticketId: Int? = null,
    val fecha: String = "",
    val prioridadId: Int? = 0,
    val cliente: String = "",
    val asunto: String = "",
    val descripcion: String = "",
    var errorMessage: String? = "",
    val tickets: List<TicketEntity> = emptyList()
)


fun UiStateTicket.toEntity() = TicketEntity(
    ticketId = ticketId,
    fecha = fecha,
    prioridadId = prioridadId,
    cliente = cliente,
    asunto = asunto,
    descripcion = descripcion
)