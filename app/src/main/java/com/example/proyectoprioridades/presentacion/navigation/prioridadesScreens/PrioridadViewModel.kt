package com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.proyectoprioridades.local.data.entities.PrioridadEntity
import com.example.proyectoprioridades.repository.PrioridadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrioridadViewModel @Inject constructor(
    private val prioridadRepository: PrioridadRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPrioridades()
    }

    private fun save() {
        viewModelScope.launch {
            if (validar()) {
                prioridadRepository.save(_uiState.value.toEntity())

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
                prioridadId = null,
                descripcion = "",
                diasCompromiso = 0,
                errorMessage = ""

            )
        }
    }

    private fun editarPrioridad(prioridadId: Int) {
        viewModelScope.launch {
            if (prioridadId > 0) {
                val prioridad = prioridadRepository.getPrioridad(prioridadId)
                _uiState.update {
                    it.copy(
                        prioridadId = prioridad?.prioridadId,
                        descripcion = prioridad?.descripcion.toString(),
                        diasCompromiso = prioridad?.diasCompromiso

                    )
                }
            }
        }
    }

    private fun delete() {
        viewModelScope.launch {
            prioridadRepository.delete(_uiState.value.toEntity())
        }
    }

    private fun getPrioridades() {
        viewModelScope.launch {
            prioridadRepository.getPrioridades().collect { prioridades ->
                _uiState.update {
                    it.copy(prioridades = prioridades)
                }
            }
        }
    }

    fun getPrioridadesInput(): Flow<List<PrioridadEntity>> {
        return prioridadRepository.getPrioridades()
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

    private fun onDiasCompromisoChange(diaCompromiso: Int) {
        _uiState.update {
            it.copy(diasCompromiso = diaCompromiso)
        }
    }

    private suspend fun onBuscarDescripcion(descripcion: String): PrioridadEntity? {

            return prioridadRepository.buscarDescripcion(descripcion)

    }

    suspend fun validar(): Boolean {
        if (uiState.value.descripcion.isBlank() && uiState.value.diasCompromiso == 0) {
            _uiState.update {
                it.copy(errorMessage = "todos los campos estan vacios")
            }
            return false
        }

        if (uiState.value.descripcion.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "La descripción está vacía")
            }
            return false
        }

        if (uiState.value.diasCompromiso == 0) {
            _uiState.update {
                it.copy(errorMessage = "El día de compromiso no puede ser menor que 0")
            }
            return false
        }
        val descripcionExistente = onBuscarDescripcion(uiState.value.descripcion)
        if (descripcionExistente != null) {
            _uiState.update {
                it.copy(errorMessage = "Esta descripción ya existe")
            }
            return false
        }
        _uiState.update {
            it.copy(errorMessage = null)
        }
        return true
    }

    fun getDescripcionById(prioridadId: Int): String {

        val descripcion =
            uiState.value.prioridades.firstOrNull { it.prioridadId == prioridadId }?.descripcion
                ?: ""

        return descripcion
    }

    fun onEvent(event: PrioridadIntent) {
        when (event) {
            is PrioridadIntent.onPrioridadIdChange -> onPrioridadIdChange(event.prioridadId)
            PrioridadIntent.deletePrioridad -> delete()
            PrioridadIntent.nuevo -> nuevo()
            is PrioridadIntent.onDescripcionChange -> onDescripcionChange(event.descripcion)
            is PrioridadIntent.onDiasCompromisoChange -> onDiasCompromisoChange(event.diasCompromiso)
            PrioridadIntent.savePrioridad -> save()
            PrioridadIntent.getPrioridades -> getPrioridades()
            is PrioridadIntent.editarPrioridad -> editarPrioridad(event.prioridadId)
            PrioridadIntent.getPrioridadesInput -> getPrioridadesInput()
        }
    }
}