package com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectoprioridades.database.PrioridadDb
import com.example.proyectoprioridades.entities.PrioridadEntity
import com.example.proyectoprioridades.presentacion.navigation.Screen
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PrioridadScreen(
    goPriordadList: () -> Unit,
    prioridadDb: PrioridadDb,
    prioridadId: Int
) {
    var descripcion by remember { mutableStateOf("") }
    var diaCompromiso by remember { mutableStateOf("") }
    var errorMessage: String? by remember { mutableStateOf(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = goPriordadList,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Añadir Prioridad")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        label = { Text(text = "Descripción") },
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        label = { Text(text = "Días Compromiso") },
                        value = diaCompromiso,
                        onValueChange = { diaCompromiso = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.padding(2.dp))

                    errorMessage?.let { Text(text = it, color = Color.Red) }

                    val scope = rememberCoroutineScope()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LaunchedEffect(prioridadId) {
                            if (prioridadId > 0) {
                                val verificarPrioridad = runBlocking {
                                    prioridadDb.PrioridadDao().find(prioridadId)
                                }
                                if (verificarPrioridad != null) {
                                    descripcion = verificarPrioridad.descripcion
                                    diaCompromiso = verificarPrioridad.diasCompromiso.toString()
                                }
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                descripcion = ""
                                diaCompromiso = ""
                                errorMessage = ""
                            },
                            modifier = Modifier.padding(0.dp, 0.dp, 7.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Nuevo"
                            )
                            Text(text = "Nuevo")
                        }

                        val intDiaCompromiso = diaCompromiso.toIntOrNull()

                        val descripcionExiste = runBlocking {
                            prioridadDb.PrioridadDao().buscarDescripcion(descripcion)
                        }

                        OutlinedButton(
                            onClick = {
                                if (descripcion.isBlank()) {
                                    errorMessage = "Descripción vacía"
                                    return@OutlinedButton
                                }

                                if (diaCompromiso.isBlank()) {
                                    errorMessage = "Días Compromiso vacío"
                                    return@OutlinedButton
                                }

                                if (intDiaCompromiso == null || intDiaCompromiso <= 0) {
                                    errorMessage =
                                        "Días Compromiso debe ser mayor que cero y un entero válido"
                                    return@OutlinedButton
                                }

                                if (descripcionExiste != null) {
                                    errorMessage = "Esta descripción ya existe"
                                    return@OutlinedButton
                                }

                                scope.launch {
                                    if (prioridadId > 0) {

                                        prioridadDb.PrioridadDao().save(
                                            PrioridadEntity(
                                                prioridadId = prioridadId,
                                                descripcion = descripcion, // Usamos directamente descripcion
                                                diasCompromiso = intDiaCompromiso // Usamos directamente intDiaCompromiso
                                            )
                                        )
                                    } else {
                                        prioridadDb.PrioridadDao().save(
                                            PrioridadEntity(
                                                descripcion = descripcion, // Usamos directamente descripcion
                                                diasCompromiso = intDiaCompromiso // Usamos directamente intDiaCompromiso
                                            )
                                        )
                                    }
                                    descripcion = ""
                                    diaCompromiso = ""
                                    errorMessage = ""
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Guardar"
                            )
                            if (prioridadId > 0) {
                                Text(text = "Editar")
                            } else {

                                Text(text = "Guardar")
                            }
                        }

                        if (prioridadId >= 0) {
                            OutlinedButton(onClick = { showDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = Color.Red
                                )

                                Text(text = "Eliminar", color = Color.Red)
                            }

                           if (showDialog){
                               AlertDialog(
                                   onDismissRequest = { showDialog = false },
                                   title = { Text(text = "Confirmar eliminación")},
                                   text = { Text(text = "Estas seguro de que desea eliminar esta prioridad?")},
                                   confirmButton = {
                                       OutlinedButton(
                                           onClick = { scope.launch {
                                               prioridadDb.PrioridadDao().delete(
                                                   PrioridadEntity(
                                                       prioridadId = prioridadId,
                                                       descripcion = descripcion,
                                                       diasCompromiso = diaCompromiso.toIntOrNull() ?: 0
                                                   )
                                               )
                                           }
                                               descripcion = ""
                                               diaCompromiso = ""
                                               errorMessage = "Prioridad Eliminada Correctamente"
                                               showDialog = false  }
                                       ) {
                                           Text("Eliminar", color = Color.Red)
                                       }
                                   },
                                   dismissButton = {
                                       OutlinedButton(onClick = { showDialog = false }) {
                                           Text(text = "cancelar")
                                       }
                                   })
                           }



                        }
                    }
                }
            }
        }
    }
}
