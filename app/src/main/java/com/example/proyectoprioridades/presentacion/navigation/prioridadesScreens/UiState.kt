package com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens

import com.example.proyectoprioridades.local.data.entities.PrioridadEntity

data class UiState(
    val prioridadId: Int? = null,
    val descripcion: String = "",
    val diasCompromiso: Int? = 0,
    var errorMessage: String? = null,
    val prioridades: List<PrioridadEntity> = emptyList()
)

fun UiState.toEntity() = PrioridadEntity(
    prioridadId = prioridadId,
    descripcion = descripcion,
    diasCompromiso = diasCompromiso?: 0
)


