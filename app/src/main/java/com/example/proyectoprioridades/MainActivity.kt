package com.example.proyectoprioridades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Room
import com.example.proyectoprioridades.presentacion.navigation.Screen
import com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens.PrioridadListScreen
import com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens.PrioridadScreen
import com.example.proyectoprioridades.local.data.database.PrioridadDb
import com.example.proyectoprioridades.local.data.entities.PrioridadEntity
import com.example.proyectoprioridades.ui.theme.ProyectoPrioridadesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import androidx.lifecycle.compose.LocalLifecycleOwner

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
                val navHost = rememberNavController()
                PrioridadesNavHost(navHost)
            }
        }
    }



    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun PrioridadesPreview() {
        ProyectoPrioridadesTheme {
            val navHost = rememberNavController()
            PrioridadesNavHost(navHost)

                                    scope.launch {

                                        savePrioridad(
                                            PrioridadEntity(
                                            descripcion = descripcion,
                                            diasCompromiso = diaCompromiso.toInt()

                                        )
                                        )


                                        descripcion = ""
                                        diaCompromiso = ""
                                        errorMessage = ""

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
                val lifecycleOwner = LocalLifecycleOwner.current
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

    @Composable
    fun PrioridadesNavHost(
        navHostController: NavHostController
    ) {
        val lifecycleOwner = LocalLifecycleOwner.current
        val prioridadList by prioridadDb.PrioridadDao().getAll()
            .collectAsStateWithLifecycle(
                initialValue = emptyList(),
                lifecycleOwner = lifecycleOwner,
                minActiveState = Lifecycle.State.STARTED
            )
        NavHost(navController = navHostController, startDestination = Screen.PrioridadList) {
            composable<Screen.PrioridadList> {
                PrioridadListScreen(
                    prioridadList = prioridadList,
                    onAddPriordad = {navHostController.navigate(Screen.Prioridad(0))},
                    onPrioridadSelected = {navHostController.navigate(Screen.Prioridad(it))}
                )
               
            }

            composable<Screen.Prioridad>{
                val prioridadId = it.toRoute<Screen.Prioridad>().prioridadId
                PrioridadScreen(
                    goPriordadList = {navHostController.navigate(Screen.PrioridadList)},
                    prioridadDb,
                    prioridadId
                )
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
    fun PrioridadesPreview() {
        ProyectoPrioridadesTheme {
            PrioridadScreen()
        }
    }

}
