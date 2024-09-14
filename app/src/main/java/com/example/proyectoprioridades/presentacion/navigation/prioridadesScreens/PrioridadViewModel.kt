package com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoprioridades.repository.PrioridadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrioridadViewModel @Inject constructor(
    private val prioridadRepository: PrioridadRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPrioridades()
    }

    private fun save() {
        viewModelScope.launch {
            if (validar()) {
                prioridadRepository.save(_uiState.value.toEntity())
                nuevo()

            } else {
                // Muestra el error
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

    private fun editarPrioridad(prioridadId: Int){
        viewModelScope.launch {
            if (prioridadId> 0){
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

    private fun delete(){
        viewModelScope.launch {
            prioridadRepository.delete(_uiState.value.toEntity())
        }
    }

    private fun getPrioridades(){
        viewModelScope.launch {
            prioridadRepository.getPrioridades().collect{ prioridades ->
                _uiState.update {
                    it.copy(prioridades = prioridades)
                }
            }
        }
    }

    private fun onPrioridadIdChange(prioridadId: Int){
        _uiState.update {
            it.copy(prioridadId = prioridadId)
        }
    }

    private fun onDescripcionChange(descripcion:String){
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }

    private fun onDiasCompromisoChange(diaCompromiso: Int){
        _uiState.update {
            it.copy(diasCompromiso =diaCompromiso )
        }
    }

    private fun onBuscarDescripcion(descripcion: String) {
            viewModelScope.launch {

                prioridadRepository.buscarDescripcion(descripcion)


            }
    }
    private fun validar(): Boolean {
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

         else if (uiState.value.diasCompromiso == 0) { // Cambiado para verificar que no sea menor a 0
            _uiState.update {
                it.copy(errorMessage = "El día de compromiso no puede ser menor que 0")
            }
            return false
        }
        _uiState.update {
            it.copy(errorMessage = null) // Limpia el error si la validación es correcta
        }
        return true
    }


    fun onEvent(event: PrioridadIntent){
        when(event) {
            is PrioridadIntent.onPrioridadIdChange -> onPrioridadIdChange(event.prioridadId)
            PrioridadIntent.deletePrioridad -> delete()
            PrioridadIntent.nuevo -> nuevo()
            is PrioridadIntent.onDescripcionChange -> onDescripcionChange(event.descripcion)
            is PrioridadIntent.onDiasCompromisoChange -> onDiasCompromisoChange(event.diasCompromiso)
            PrioridadIntent.savePrioridad -> save()
            PrioridadIntent.getPrioridades -> getPrioridades()
            is PrioridadIntent.editarPrioridad -> editarPrioridad(event.prioridadId)
            is PrioridadIntent.buscarDescripcion -> onBuscarDescripcion(event.descripcion)
            is PrioridadIntent.validar -> validar()
        }
    }


}