package com.marcmeru.merunoting.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun MainView() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Carpetas", "Recientes")


    Scaffold(
        topBar = {
            TopNavigationTabs(
                tabTitles = tabTitles,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it },
                openDrawer = { scope.launch { drawerState.open() } }
            )
        },
        floatingActionButton = { /* Tu FAB personalizado */ },
        bottomBar = { /* Tu barra inferior si la tienes */ }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTabIndex) {
                0 -> FoldersView()
                1 -> RecientesView()
            }
        }
    }

}

@Composable
fun RecientesView() {
    Text("Contenido de Recientes aún no implementado")
    // TODO("Not yet implemented")  --> comentar o eliminar esta línea
}
