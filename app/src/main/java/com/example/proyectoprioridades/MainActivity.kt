package com.example.proyectoprioridades

import android.graphics.Paint.Style
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.Room
import com.example.proyectoprioridades.database.PrioridadDb
import com.example.proyectoprioridades.entities.PrioridadEntity
import com.example.proyectoprioridades.ui.theme.ProyectoPrioridadesTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Scope

class MainActivity : ComponentActivity() {
    private lateinit var prioridadDb: PrioridadDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        prioridadDb = Room.databaseBuilder(
            applicationContext, PrioridadDb::class.java, "Prioridad.db"
        ).fallbackToDestructiveMigration().build()

        setContent {
            ProyectoPrioridadesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        PrioridadScreen()
                    }
                }
            }
        }
    }
    @Composable
    fun PrioridadScreen() {
        var descripcion by remember { mutableStateOf("") }
        var diaCompromiso by remember { mutableStateOf("") }
        var errorMessage: String? by remember { mutableStateOf(null) }

        Scaffold { innerPadding ->
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
                            label = { Text(text = "Dias Compromiso")},
                            value = diaCompromiso,
                            onValueChange = {diaCompromiso = it},
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.padding(2.dp))
                        errorMessage?.let { Text(text = it, color= Color.Red) }
                        val scope = rememberCoroutineScope()
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                            )
                        {
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

                            val descripcionExiste = runBlocking {buscarPorDescripcion(descripcion)}
                            OutlinedButton(

                                onClick = {
                                    if (descripcion.isBlank()){

                                        errorMessage = "Descripcion vacia"
                                        return@OutlinedButton
                                    }
                                    if (intDiaCompromiso == null || intDiaCompromiso < 0  ){

                                        errorMessage = "Dias Compromiso vacio"
                                        return@OutlinedButton
                                    }

                                    if( descripcionExiste != null){

                                        errorMessage ="Esta descripción ya existe"
                                        return@OutlinedButton
                                    }


                                    scope.launch {

                                        savePrioridad(PrioridadEntity(
                                            descripcion = descripcion,
                                            diasCompromiso = diaCompromiso.toInt()

                                        ))


                                        descripcion = ""
                                        diaCompromiso = ""

                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "save button"
                                )
                                Text(text = "Guardar")

                            }
                        }

                    }
                }
                val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
                val prioridadList by prioridadDb.PrioridadDao().getAll()
                    .collectAsStateWithLifecycle(
                        initialValue = emptyList(),
                        lifecycleOwner = lifecycleOwner,
                        minActiveState = Lifecycle.State.STARTED
                    )
                    PrioridadListScreen(prioridadList )


            }
        }
    }

    private suspend fun savePrioridad(prioridad: PrioridadEntity){
        prioridadDb.PrioridadDao().save(prioridad)
    }

    @Composable
    fun PrioridadListScreen(prioridadList: List<PrioridadEntity>){

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Lista de prioridades", modifier = Modifier.align(Alignment.CenterHorizontally), style = MaterialTheme.typography.headlineMedium)
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
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                ) {




                items(prioridadList) {
                    PrioridadRow(it)
                }

            }

        }
    }

    @Composable
    private fun PrioridadRow(it: PrioridadEntity) {

        Row(
            verticalAlignment = Alignment.CenterVertically,

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

            Text(modifier = Modifier.weight(2f),
                text = it.diasCompromiso.toString(),
                style = MaterialTheme.typography.bodyLarge
            )

        }
        HorizontalDivider()
    }

    private suspend fun buscarPorDescripcion(descripcion: String): PrioridadEntity? {
        val existe =prioridadDb.PrioridadDao().buscarDescripcion(descripcion)
        return existe
    }
    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun GreetingPreview() {
        ProyectoPrioridadesTheme {
            PrioridadScreen()
        }
    }

}
