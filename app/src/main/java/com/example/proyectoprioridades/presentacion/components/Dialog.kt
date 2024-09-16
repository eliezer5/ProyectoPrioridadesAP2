package com.example.proyectoprioridades.presentacion.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch



@Composable
fun ConfirmarEliminarDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    snackbarHostState: SnackbarHostState,
    goTicketList: () -> Unit
) {
    val scope = rememberCoroutineScope()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "Confirmar eliminación") },
            text = { Text(text = "¿Estás seguro de que deseas eliminar esta prioridad?") },
            confirmButton = {
                OutlinedButton(
                    onClick = {
                        onConfirm()
                        onDismiss() // Cerrar el diálogo tras la confirmación
                        scope.launch {
                            // Mostrar Snackbar
                            snackbarHostState.showSnackbar(
                                "Ticket eliminado correctamente",
                                duration = SnackbarDuration.Short
                            )
                            // Ir a la lista de tickets después de mostrar el Snackbar
                            goTicketList()
                        }
                    }
                ) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { onDismiss() }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }
}
