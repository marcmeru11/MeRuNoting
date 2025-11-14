package com.marcmeru.merunoting.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.marcmeru.merunoting.viewModel.ItemViewModel
import kotlinx.coroutines.launch

@Composable
fun MainView(
    viewModel: ItemViewModel,
    initialFolderId: Long? = null
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Estado para carpeta seleccionada, inicializado con el parámetro
    var selectedFolderId by remember { mutableStateOf(initialFolderId) }

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
        floatingActionButton = {
            AddFolderFab(
                viewModel = viewModel,
                currentFolderId = selectedFolderId
            )
                               },
        bottomBar = { /* Tu barra inferior si la tienes */ }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTabIndex) {
                0 -> ItemsView(
                    viewModel = viewModel,
                    selectedFolderId = selectedFolderId,
                    onFolderSelected = { folderId ->
                        selectedFolderId = folderId
                    }
                )
                1 -> RecientesView()
            }
        }
    }
}


@Composable
fun RecientesView() {
    Text("Contenido de Recientes aún no implementado")
}
