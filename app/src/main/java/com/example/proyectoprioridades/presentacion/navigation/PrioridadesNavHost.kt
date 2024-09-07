package com.example.proyectoprioridades.presentacion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun PrioridadesNavHost(
    navHostController: NavHostController,


    ) {
    NavHost(navController = navHostController, startDestination = Screen.PrioridadList) {
        composable<Screen.PrioridadList> {

        }
    }

}
