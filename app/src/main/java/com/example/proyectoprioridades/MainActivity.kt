package com.example.proyectoprioridades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Room
import com.example.proyectoprioridades.local.data.database.PrioridadDb
import com.example.proyectoprioridades.presentacion.navigation.Screen
import com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens.PrioridadListScreen
import com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens.PrioridadScreen
import com.example.proyectoprioridades.ui.theme.ProyectoPrioridadesTheme

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
    fun GreetingPreview() {
        ProyectoPrioridadesTheme {
            val navHost = rememberNavController()
            PrioridadesNavHost(navHost)
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


}
