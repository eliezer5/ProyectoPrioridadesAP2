package com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens

sealed interface PrioridadIntent {
    data class onPrioridadIdChange(val prioridadId: Int): PrioridadIntent
    data class onDescripcionChange(val descripcion: String): PrioridadIntent
    data class onDiasCompromisoChange(val diasCompromiso: Int) : PrioridadIntent
    data object savePrioridad: PrioridadIntent
    data object deletePrioridad: PrioridadIntent
    data object nuevo: PrioridadIntent
    data object getPrioridades: PrioridadIntent
    data class editarPrioridad(val prioridadId: Int) : PrioridadIntent
    data class buscarDescripcion(val descripcion: String): PrioridadIntent
}