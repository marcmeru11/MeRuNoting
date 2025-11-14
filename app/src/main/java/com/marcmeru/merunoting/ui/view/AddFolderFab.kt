package com.marcmeru.merunoting.ui.view

import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.marcmeru.merunoting.viewModel.ItemViewModel
import kotlinx.coroutines.launch

@Composable
fun AddFolderFab(
    viewModel: ItemViewModel,
    currentFolderId: Long?,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var folderName by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (folderName.isNotBlank()) {
                            val newFolder = com.marcmeru.merunoting.data.entity.Item(
                                id = System.currentTimeMillis(),  // usa un id único, mejor generar correctamente
                                parentId = currentFolderId,
                                name = folderName,
                                type = "folder",
                                content = null,
                                createdAt = System.currentTimeMillis(),
                                updatedAt = System.currentTimeMillis()
                            )
                            scope.launch {
                                viewModel.addItem(newFolder)
                                showDialog = false
                                folderName = ""
                            }
                        }
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            },
            title = { Text("Nueva Carpeta") },
            text = {
                TextField(
                    value = folderName,
                    onValueChange = { folderName = it },
                    label = { Text("Nombre de la carpeta") },
                    singleLine = true
                )
            }
        )
    }

    FloatingActionButton(
        modifier = modifier,
        onClick = { showDialog = true }
    ) {
        Icon(
            imageVector = androidx.compose.material.icons.Icons.Default.CreateNewFolder,
            contentDescription = "Añadir carpeta"
        )
    }
}
