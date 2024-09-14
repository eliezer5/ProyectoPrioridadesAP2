package com.example.proyectoprioridades.repository

import com.example.proyectoprioridades.local.data.dao.PrioridadDao
import com.example.proyectoprioridades.local.data.database.PrioridadDb
import com.example.proyectoprioridades.local.data.entities.PrioridadEntity
import javax.inject.Inject

class PrioridadRepository @Inject constructor(
    private val prioridadDao: PrioridadDao
) {
    suspend fun save(prioridad: PrioridadEntity) = prioridadDao.save(prioridad)

    suspend fun getPrioridad(id: Int) = prioridadDao.find(id)

    suspend fun delete(prioridad: PrioridadEntity) = prioridadDao.delete(prioridad)

    fun getPrioridades() = prioridadDao.getAll()
    suspend fun buscarDescripcion(descripcion: String) = prioridadDao.buscarDescripcion(descripcion)

}