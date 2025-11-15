package com.marcmeru.merunoting.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marcmeru.merunoting.data.entity.Item
import com.marcmeru.merunoting.viewModel.ItemViewModel
import kotlinx.coroutines.launch

@Composable
fun ItemsView(
    viewModel: ItemViewModel,
    selectedFolderId: Long? = null,
    onFolderSelected: (Long?) -> Unit,
    onNoteSelected: (Item) -> Unit
) {
    val rootItems by viewModel.rootItems.collectAsState()
    val childrenMap by viewModel.children.collectAsState()

    val items = if (selectedFolderId == null) rootItems else childrenMap[selectedFolderId] ?: emptyList()

    LaunchedEffect(selectedFolderId) {
        if (selectedFolderId != null) {
            viewModel.loadChildren(selectedFolderId)
        } else {
            viewModel.loadRootItems()
        }
    }

    var path by remember { mutableStateOf(emptyList<Item>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(selectedFolderId) {
        scope.launch {
            val tempPath = mutableListOf<Item>()
            var folderId = selectedFolderId
            while (folderId != null) {
                val item = viewModel.getItemById(folderId)
                tempPath.add(0, item)
                folderId = item.parentId
            }
            path = tempPath
        }
    }

    var noteToDelete by remember { mutableStateOf<Item?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        BreadcrumbPath(
            folders = path,
            onFolderSelected = onFolderSelected
        )

        if (items.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay elementos.",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(items) { item ->
                    if (item.type == "folder") {
                        FolderCard(
                            item = item,
                            modifier = Modifier
                                .padding(8.dp)
                                .height(160.dp)
                                .clickable { onFolderSelected(item.id) },
                            onDeleteClicked = { folderToDelete ->
                                viewModel.deleteItem(folderToDelete)
                            }
                        )
                    } else if (item.type == "note") {
                        NoteCard(
                            item = item,
                            modifier = Modifier
                                .padding(8.dp)
                                .height(160.dp)
                                .clickable { onNoteSelected(item) },
                            onDeleteClicked = {
                                noteToDelete = it
                            }
                        )
                    }
                }
            }
        }
    }

    if (noteToDelete != null) {
        AlertDialog(
            onDismissRequest = { noteToDelete = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Eliminar la nota \"${noteToDelete?.name}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    noteToDelete?.let { viewModel.deleteItem(it) }
                    noteToDelete = null
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { noteToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
