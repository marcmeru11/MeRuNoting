package com.marcmeru.merunoting.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marcmeru.merunoting.data.entity.Item
import com.marcmeru.merunoting.viewModel.ItemViewModel

@Composable
fun RecentsView(
    viewModel: ItemViewModel,
    onNoteSelected: (Item) -> Unit
) {
    val notes by viewModel.rootItems.collectAsState()
    val recentNotes = remember(notes) {
        notes
            .filter { it.type == "note" }
            .sortedByDescending { it.updatedAt }
    }
    var noteToDelete by remember { mutableStateOf<Item?>(null) }

    if (recentNotes.isEmpty()) {
        Text("No hay notas recientes", modifier = Modifier.padding(8.dp))
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recentNotes) { note ->
                RecentNoteCard(
                    item = note,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = { onNoteSelected(note) },
                    onDeleteClicked = { noteToDelete = it }
                )
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
                    noteToDelete?.let {
                        viewModel.deleteItem(it)
                    }
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
