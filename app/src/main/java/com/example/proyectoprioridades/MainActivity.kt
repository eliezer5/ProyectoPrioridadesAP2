package com.example.proyectoprioridades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.proyectoprioridades.presentacion.navigation.PrioridadesNavHost
import com.example.proyectoprioridades.presentacion.navigation.Screen
import com.example.proyectoprioridades.presentacion.navigation.menuScreens.MenuScreen
import com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens.PrioridadListScreen
import com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens.PrioridadScreen
import com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens.PrioridadViewModel
import com.example.proyectoprioridades.ui.theme.ProyectoPrioridadesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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







}
