package com.example.proyectoprioridades.presentacion.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.proyectoprioridades.presentacion.navigation.menuScreens.MenuScreen
import com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens.PrioridadListScreen
import com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens.PrioridadScreen
import com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens.PrioridadViewModel
import com.example.proyectoprioridades.presentacion.navigation.ticketScreens.TicketListScreen
import com.example.proyectoprioridades.presentacion.navigation.ticketScreens.TicketScreen
import com.example.proyectoprioridades.presentacion.navigation.ticketScreens.TicketViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadesNavHost(
    navHostController: NavHostController
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(text = "Menu")
                Spacer(modifier = Modifier.padding(10.dp))


                NavigationDrawerItem(
                    label = { Text(text = "Home") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }

                        navHostController.navigate(Screen.Menu)
                    })
                NavigationDrawerItem(
                    label = { Text(text = "Lista Prioridades") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navHostController.navigate(Screen.PrioridadList)
                    })
                NavigationDrawerItem(
                    label = { Text(text = "Lista Tickets") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navHostController.navigate(Screen.TicketList)
                    })

            }
        },
    ) {
        Scaffold(
            topBar = {

                TopAppBar(
                    title = { "menu" },
                    navigationIcon = {
                        Icon(Icons.Filled.Add, contentDescription = "")
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }
                        ) {

                        }
                    },
                )

            }
        ) { innerPadding ->
            // Screen content


            NavHost(
                navController = navHostController,
                startDestination = Screen.Menu,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<Screen.PrioridadList> {
                    PrioridadListScreen(
                        onAddPriordad = { navHostController.navigate(Screen.Prioridad(0)) },
                        onPrioridadSelected = { navHostController.navigate(Screen.Prioridad(it)) }
                    )
                }


                composable<Screen.TicketList> {
                    TicketListScreen(
                        onAddTicket = { navHostController.navigate(Screen.Ticket(0)) },
                        onTicketSelected = {navHostController.navigate(Screen.Ticket(it)) }
                    )
                }

                composable<Screen.Ticket>{
                    val viewModel: TicketViewModel = hiltViewModel()
                    val ticketId = it.toRoute<Screen.Ticket>().tickedId
                    TicketScreen(
                        onEvent = {event -> viewModel.onEvent(event)},
                        ticketId = ticketId,
                        goTicketList = {navHostController.navigate(Screen.TicketList)},


                    )

                }


                composable<Screen.Prioridad> {
                    val viewModel: PrioridadViewModel = hiltViewModel()
                    val prioridadId = it.toRoute<Screen.Prioridad>().prioridadId
                    PrioridadScreen(
                        goPriordadList = { navHostController.navigate(Screen.PrioridadList) },
                        onEvent = { event -> viewModel.onEvent(event) },
                        prioridadId = prioridadId,
                    )
                }
                composable<Screen.Menu> {
                    Text(text = "Home")
                }


            }
        }
    }


}
