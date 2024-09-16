package com.example.proyectoprioridades.presentacion.navigation.menuScreens

import android.annotation.SuppressLint
import android.icu.text.CaseMap.Title
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectoprioridades.presentacion.navigation.prioridadesScreens.PrioridadIntent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MenuScreen(
    goPriordadList: () -> Unit,
    goTicketList: () -> Unit

) {
    menuDesplegable(goPriordadList =goPriordadList, goTicketList)

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun menuDesplegable(goPriordadList: () -> Unit, goTicketList: () -> Unit ){

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(text = "Menu")
                Spacer(modifier = Modifier.padding(10.dp))


                NavigationDrawerItem(label = { Text(text = "Lista Prioridades")}, selected = false, onClick =  goPriordadList )
                NavigationDrawerItem(label = { Text(text = "Lista Tickets")}, selected = false, onClick =  goTicketList )

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
                    },)

            }
        ) { innerPadding ->
            // Screen content


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                horizontalArrangement = Arrangement.Center

            ) {



            }
        }
    }
}