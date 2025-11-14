package com.marcmeru.merunoting.ui.view

import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import com.marcmeru.merunoting.data.entity.Item
import com.marcmeru.merunoting.viewModel.ItemViewModel

@Composable
fun AddNoteFab(
    viewModel: ItemViewModel,
    currentFolderId: Long?,
    onNoteCreated: (Item) -> Unit,  // Callback para abrir editor al crear nota
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var isCreating by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = {
            if (!isCreating) {
                isCreating = true
                val newNote = Item(
                    id = System.currentTimeMillis(),
                    parentId = currentFolderId,
                    name = "Nueva Nota",
                    type = "note",
                    content = "Nota",
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                scope.launch {
                    viewModel.addItem(newNote)  // Añadimos la nueva nota
                    isCreating = false
                    onNoteCreated(newNote)  // Avisamos para abrir el editor
                }
            }
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = androidx.compose.material.icons.Icons.Default.Description,
            contentDescription = "Añadir nota"
        )
    }
}
