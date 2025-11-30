package com.marcmeru.merunoting.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marcmeru.merunoting.data.entity.Item
import com.marcmeru.merunoting.viewModel.ItemViewModel
import com.marcmeru.merunoting.viewModel.ThemeViewModel
import kotlinx.coroutines.launch

@Composable
fun MainView(
    viewModel: ItemViewModel,
    themeViewModel: ThemeViewModel,
    initialFolderId: Long? = null
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    var selectedFolderId by remember { mutableStateOf(initialFolderId) }
    var selectedNote by remember { mutableStateOf<Item?>(null) }

    val tabTitles = listOf("Carpetas", "Recientes", "Ajustes")

    Scaffold(
        topBar = {
            if (selectedNote == null) {
                TopNavigationTabs(
                    tabTitles = tabTitles,
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { selectedTabIndex = it },
                    openDrawer = { scope.launch { drawerState.open() } }
                )
            }
        },
        floatingActionButton = {
            if (selectedNote == null && selectedTabIndex == 0) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(bottom = 56.dp, end = 16.dp)
                ) {
                    AddFolderFab(
                        viewModel = viewModel,
                        currentFolderId = selectedFolderId
                    )
                    AddNoteFab(
                        viewModel = viewModel,
                        currentFolderId = selectedFolderId,
                        onNoteCreated = { note ->
                            selectedNote = note
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                selectedNote != null -> {
                    NoteDetailView(
                        viewModel = viewModel,
                        note = selectedNote!!,
                        onNoteUpdated = {
                            selectedNote = null
                        },
                        onCancel = {
                            selectedNote = null
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                selectedTabIndex == 0 -> {
                    ItemsView(
                        viewModel = viewModel,
                        selectedFolderId = selectedFolderId,
                        onFolderSelected = { folderId -> selectedFolderId = folderId },
                        onNoteSelected = { note -> selectedNote = note }
                    )
                }
                selectedTabIndex == 1 -> RecentsView(
                    viewModel = viewModel,
                    onNoteSelected = { note -> selectedNote = note }
                )
                selectedTabIndex == 2 -> SettingsView(viewModel = themeViewModel)
            }
        }
    }
}
