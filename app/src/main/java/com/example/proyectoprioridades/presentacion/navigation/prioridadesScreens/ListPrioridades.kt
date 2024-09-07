package com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens

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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectoprioridades.local.data.entities.PrioridadEntity
import com.example.proyectoprioridades.presentacion.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadListScreen(
    prioridadList: List<PrioridadEntity>,
    onAddPriordad: () -> Unit,
    onPrioridadSelected: (Int) -> Unit
) {


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPriordad,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir Prioridad")
            }

        }

    ) {
        innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Lista de prioridades",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Fila de títulos de las columnas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 10.dp),


                ) {



                Text(
                    text = "ID",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Descripción",
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Días",
                    modifier = Modifier.weight(2f),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {


                items(prioridadList) {
                    PrioridadRow(it, onPrioridadSelected)
                }

            }

        }
    }
}

@Composable
private fun PrioridadRow(it: PrioridadEntity, onPrioridadSelected: (Int) -> Unit) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onPrioridadSelected(it.prioridadId?: 0)}.padding(12.dp)
        ) {
        Text(
            modifier = Modifier.weight(1f),
            text = it.prioridadId.toString(),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            modifier = Modifier.weight(2f),
            text = it.descripcion,
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            modifier = Modifier.weight(2f),
            text = it.diasCompromiso.toString(),
            style = MaterialTheme.typography.bodyLarge
        )

    }
    HorizontalDivider()
}
