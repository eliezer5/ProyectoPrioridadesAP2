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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import com.example.proyectoprioridades.local.data.database.PrioridadDb
import com.example.proyectoprioridades.local.data.entities.PrioridadEntity
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PrioridadScreen(
    viewModel: PrioridadViewModel = hiltViewModel(),
    onEvent: (PrioridadIntent) -> Unit,
    goPriordadList: () -> Unit,

    ) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PrioridadBodyScreen(onEvent, uiState, goPriordadList)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadBodyScreen(
    onEvent: (PrioridadIntent) -> Unit,
    uiState: UiState,
    goPriordadList: () -> Unit,


    ) {
    var descripcion by remember { mutableStateOf("") }
    var diaCompromiso by remember { mutableStateOf("") }
    var errorMessage: String? by remember { mutableStateOf(null) }
    var showDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val prioridadId = uiState.prioridadId ?: 0

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = goPriordadList,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Añadir Prioridad")
            }

        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            )
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

                    if (prioridadId > 0) {

                        Text(
                            text = "Editar Prioridad",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom = 15.dp)
                        )
                    } else {
                        Text(
                            text = "Agregar Prioridad",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom = 15.dp)
                        )
                    }

                    OutlinedTextField(
                        label = { Text(text = "Descripción") },
                        value = uiState.descripcion,
                        onValueChange = { it -> onEvent(PrioridadIntent.onDescripcionChange(it.toString())) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        label = { Text(text = "Días Compromiso") },
                        value = uiState.diasCompromiso?.toString() ?: "",
                        onValueChange = { newValue ->
                            onEvent(PrioridadIntent.onDiasCompromisoChange(newValue.toIntOrNull() ?: 0))
                        },
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

                        if (uiState.prioridadId != 0)

                        onEvent(PrioridadIntent.editarPrioridad(prioridadId))


                    }
                }


                if (uiState.prioridadId != 0) {

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
                }

                val intDiaCompromiso = diaCompromiso.toIntOrNull()

                val descripcionExiste =
                    onEvent(PrioridadIntent.buscarDescripcion(descripcion))


                OutlinedButton(
                    onClick = {


                        onEvent(PrioridadIntent.savePrioridad)
//                        keyboardController?.hide()
//                                    snackbarHostState.showSnackbar(
//                                        "Prioridad editada correctamente"
//                                    )
                        goPriordadList()



//                        onEvent(PrioridadIntent.nuevo)
//                        keyboardController?.hide()

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

                if (prioridadId > 0) {
                    OutlinedButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.Red
                        )

                        Text(text = "Eliminar", color = Color.Red)
                    }

                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text(text = "Confirmar eliminación") },
                            text = { Text(text = "Estas seguro de que desea eliminar esta prioridad?") },
                            confirmButton = {
                                OutlinedButton(
                                    onClick = {

                                            onEvent(PrioridadIntent.deletePrioridad)
                                            showDialog = false
//                                            snackbarHostState.showSnackbar(
//                                                "Prioridad eliminada correctamente",
//                                                duration = SnackbarDuration.Short
//                                            )
                                            goPriordadList()


                                    }
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

