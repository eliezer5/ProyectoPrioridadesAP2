package com.example.proyectoprioridades.local.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.proyectoprioridades.local.data.entities.PrioridadEntity

@Dao
interface PrioridadDao {
    @Upsert
    suspend fun save(prioridad: PrioridadEntity)

    @Query(
        """
        SELECT * 
        FROM Prioridades 
        WHERE prioridadId=:id 
        LIMIT 1
        """
    )
    suspend fun find(id: Int): PrioridadEntity?

    @Query(
        """
        SELECT * FROM Prioridades WHERE descripcion LIKE :descripcion
        """
    )
    suspend fun buscarDescripcion(descripcion: String): PrioridadEntity?

    @Delete
    suspend fun delete(prioridad: PrioridadEntity)

    @Query("Select * From prioridades")
    fun getAll(): kotlinx.coroutines.flow.Flow<List<PrioridadEntity>>
}