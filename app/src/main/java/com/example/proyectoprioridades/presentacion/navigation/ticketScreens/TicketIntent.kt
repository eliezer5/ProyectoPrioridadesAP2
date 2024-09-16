package com.example.proyectoprioridades.presentacion.navigation.ticketScreens

import com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens.UiState

sealed interface TicketIntent {
    data class onTicketIdChange(val tickedId: Int): TicketIntent
    data class onDescripcionChange(val descripcion: String): TicketIntent
    data class onPrioridadIdChange(val prioridadId: Int) : TicketIntent
    data class onAsuntoChange(val asunto : String): TicketIntent
    data class onFechaChange(val fecha: String): TicketIntent
    data class onClienteChange(val cliente: String): TicketIntent
    data object saveTicket: TicketIntent
    data object deleteTicket: TicketIntent
    data object nuevo: TicketIntent
    data object getTickets: TicketIntent
    data class editarTicket(val ticketId: Int) : TicketIntent
    data class validar(val uiState: UiState): TicketIntent

}