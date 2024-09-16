package com.example.proyectoprioridades.presentacion.navigation.ticketScreens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens.PrioridadViewModel
import com.example.proyectoprioridades.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiStateTicket())
    val uiState = _uiState.asStateFlow()

    init {
        getTickets()
    }

    private fun save() {
        viewModelScope.launch {
            if (validar()) {
                ticketRepository.save(_uiState.value.toEntity())
                nuevo()

            } else {
                _uiState.update {
                    it.copy(errorMessage = uiState.value.errorMessage)
                }
            }
        }
    }

    private fun nuevo() {
        _uiState.update {
            it.copy(
                ticketId = 0,
                prioridadId = 0,
                descripcion = "",
                cliente = "",
                asunto = "",
                fecha = "",
                errorMessage = null

            )
        }
    }

    private fun editarTicket(ticketId: Int) {
        viewModelScope.launch {
            if (ticketId > 0) {
                val ticket = ticketRepository.getTicket(ticketId)
                _uiState.update {
                    it.copy(
                        ticketId = ticket?.ticketId,
                        fecha = ticket?.fecha ?: "",
                        prioridadId = ticket?.prioridadId,
                        asunto = ticket?.asunto.toString(),
                        descripcion = ticket?.descripcion.toString(),
                    )
                }
            }
        }
    }

    private fun delete() {
        viewModelScope.launch {
            ticketRepository.delete(_uiState.value.toEntity())
        }
    }

    private fun getTickets() {
        viewModelScope.launch {
            ticketRepository.getTickets().collect { tickets ->
                _uiState.update {
                    it.copy(tickets = tickets)
                }
            }
        }
    }

    private fun onTicketIdChange(ticketId: Int) {
        _uiState.update {
            it.copy(ticketId = ticketId)
        }
    }

    private fun onFechaChange(fecha: String) {
        _uiState.update {
            it.copy(fecha = fecha)
        }
    }

    private fun onPrioridadIdChange(prioridadId: Int) {
        _uiState.update {
            it.copy(prioridadId = prioridadId)
        }
    }

    private fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }

    private fun onAsuntoChange(asunto: String) {
        _uiState.update {
            it.copy(asunto = asunto)
        }
    }

    private fun onClienteChange(cliente: String) {
        _uiState.update {
            it.copy(cliente = cliente)
        }
    }
    fun validar(): Boolean {
        if (uiState.value.descripcion.isBlank() && uiState.value.asunto.isBlank() && uiState.value.prioridadId == 0 && uiState.value.fecha == null) {
            _uiState.update {
                it.copy(errorMessage = "Todos los campos estan vacios")
            }
            return false
        }

        if (uiState.value.descripcion.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "La descripción está vacía")
            }
            return false
        }

        if (uiState.value.asunto.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "El asunto está vacío")
            }
            return false
        }
        if (uiState.value.fecha.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "La fecha está vacía")
            }
            return false
        }
        if (uiState.value.prioridadId == 0) {
            _uiState.update {
                it.copy(errorMessage = "Debe seleccionar una prioridad")
            }
            return false
        }

        if (uiState.value.cliente.isBlank()){
            _uiState.update {
                it.copy(errorMessage = "El cliente está vacio")
            }
            return false
        }

        _uiState.update {
            it.copy(errorMessage = null)
        }

        return true
    }

    fun onEvent(event: TicketIntent) {
        when (event) {
            TicketIntent.deleteTicket -> delete()
            is TicketIntent.editarTicket -> editarTicket(event.ticketId)
            TicketIntent.getTickets -> getTickets()
            TicketIntent.nuevo -> nuevo()
            is TicketIntent.onAsuntoChange -> onAsuntoChange(event.asunto)
            is TicketIntent.onDescripcionChange -> onDescripcionChange(event.descripcion)
            is TicketIntent.onFechaChange -> onFechaChange(event.fecha)
            is TicketIntent.onPrioridadIdChange -> onPrioridadIdChange(event.prioridadId)
            is TicketIntent.onTicketIdChange -> onTicketIdChange(event.tickedId)
            TicketIntent.saveTicket -> save()
            is TicketIntent.validar -> validar()
            is TicketIntent.onClienteChange -> onClienteChange(event.cliente)
        }
    }


}