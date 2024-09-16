package com.example.proyectoprioridades.presentacion.navigation.ticketScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyectoprioridades.local.data.entities.TicketEntity
import com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens.PrioridadViewModel
import com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    viewModel: TicketViewModel = hiltViewModel(),
    onAddTicket: () -> Unit,
    onTicketSelected: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val viewModelPrioridad: PrioridadViewModel = hiltViewModel()
    TicketListBodyScreen(uiState, onAddTicket, onTicketSelected,viewModelPrioridad)

}


@Composable
fun TicketListBodyScreen(
    uiState: UiStateTicket,
    onAddTicket: () -> Unit,
    onTicketSelected: (Int) -> Unit,
    viewModel: PrioridadViewModel
) {

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTicket,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Prioridad")
            }

        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Lista de Tickets",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),
                ) {
                Text(
                    text = "ID",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "Cliente",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "Fecha",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Prioridad",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "Asunto",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    text = "Descripción",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(uiState.tickets) {
                    TicketRow(it, onTicketSelected, viewModel)
                }
            }
        }
    }
}

@Composable
private fun TicketRow(it: TicketEntity, onTicketSelected: (Int) -> Unit, viewModel: PrioridadViewModel) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onTicketSelected(it.ticketId ?: 0) }
            .padding(bottom = 17.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = it.ticketId.toString(),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier.weight(1f),
            text = it.cliente,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier.weight(1f),
            text = it.fecha,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier.weight(1f),
            text = viewModel.getDescripcionById(it.prioridadId?: 0),
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier.weight(1f),
            text = it.asunto,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            modifier = Modifier.weight(1f),
            text = it.descripcion,
            style = MaterialTheme.typography.bodyMedium
        )
    }
    HorizontalDivider()
}