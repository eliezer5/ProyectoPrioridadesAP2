package com.example.proyectoprioridades.local.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.proyectoprioridades.local.data.entities.PrioridadEntity
import com.example.proyectoprioridades.local.data.entities.TicketEntity

@Dao
interface TicketDao {

    @Upsert
    suspend fun save(ticket: TicketEntity)

    @Query(
        """select * from Tickets where TicketId=:id
            Limit 1
        """
    )
    suspend fun find(id: Int): TicketEntity?
    @Delete
    suspend fun delete(ticketEntity: TicketEntity)

    @Query("Select * From tickets")
    fun getAll(): kotlinx.coroutines.flow.Flow<List<TicketEntity>>
}