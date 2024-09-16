package com.example.proyectoprioridades.presentacion.navigation.ticketScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyectoprioridades.local.data.converters.Converter
import com.example.proyectoprioridades.presentacion.components.ConfirmarEliminarDialog
import com.example.proyectoprioridades.presentacion.components.InputSelect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun TicketScreen(
    viewModel: TicketViewModel = hiltViewModel(),
    onEvent: (TicketIntent) -> Unit,
    ticketId: Int,
    goTicketList: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TicketBodyScreen(
        onEvent,
        uiState,
        ticketId,
        goTicketList,
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketBodyScreen(
    onEvent: (TicketIntent) -> Unit,
    uiState: UiStateTicket,
    ticketId: Int,
    goTicketList: () -> Unit,

    ) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var showDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val ticketView: TicketViewModel = hiltViewModel()
    val convert = Converter()

    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { selectedDateMillis ->
            val selectedDate = convert.convertToDate(selectedDateMillis)
            onEvent(TicketIntent.onFechaChange(selectedDate))
            delay(300)
            showDatePicker = false
        }
    }

    LaunchedEffect(key1 = true) {
        if (ticketId > 0) {
            onEvent(TicketIntent.editarTicket(ticketId))
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = goTicketList,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Añadir Ticket")
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

                    InputSelect(
                        label = "Selecciona una Prioridad",
                        onOptionSelected = { onEvent(TicketIntent.onPrioridadIdChange(it)) }
                    )

                    OutlinedTextField(
                        value = uiState.fecha,
                        onValueChange = {},
                        label = { Text("DOB") },
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = !showDatePicker }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Select date"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                    )

                    if (showDatePicker) {
                        Popup(
                            onDismissRequest = { showDatePicker = false },
                            alignment = Alignment.TopStart
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(y = 80.dp)
                                    .shadow(elevation = 4.dp)
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(16.dp)
                            ) {
                                DatePicker(
                                    state = datePickerState,
                                    showModeToggle = false
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        label = { Text(text = "Cliente") },
                        value = uiState.cliente,
                        onValueChange = { onEvent(TicketIntent.onClienteChange(it)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        label = { Text(text = "Descripción") },
                        value = uiState.descripcion,
                        onValueChange = { onEvent(TicketIntent.onDescripcionChange(it)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        label = { Text(text = "Asunto") },
                        value = uiState.asunto,
                        onValueChange = { onEvent(TicketIntent.onAsuntoChange(it)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.padding(2.dp))

                    uiState.errorMessage?.let { error ->
                        Text(text = error, color = Color.Red)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                onEvent(TicketIntent.nuevo)
                            },
                            modifier = Modifier.padding(0.dp, 0.dp, 7.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Nuevo"
                            )
                            Text(text = "Nuevo")
                        }
                        OutlinedButton(
                            onClick = {

                                if (ticketView.validar()) {

                                    onEvent(TicketIntent.saveTicket)
                                    scope.launch {

                                        if (ticketId > 0) {
                                            snackbarHostState.showSnackbar(
                                                "Ticket editado correctamente"
                                            )
                                        } else {

                                            snackbarHostState.showSnackbar(
                                                "Ticket guardado correctamente"
                                            )
                                        }
                                    }
                                    keyboardController?.hide()

                                }

                            }
                        ) {

                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Guardar"
                            )
                            if (ticketId > 0) {
                                Text(text = "Editar")
                            } else {

                                Text(text = "Guardar")
                            }
                        }
                        if (ticketId > 0) {
                            OutlinedButton(onClick = { showDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = Color.Red
                                )

                                Text(text = "Eliminar", color = Color.Red)
                            }
                            ConfirmarEliminarDialog(
                                showDialog = showDialog,
                                onDismiss = { showDialog = false },
                                onConfirm = {
                                    onEvent(TicketIntent.deleteTicket)
                                },
                                snackbarHostState = snackbarHostState,
                                goTicketList = {
                                    goTicketList() // Redirigir a la lista de tickets
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}