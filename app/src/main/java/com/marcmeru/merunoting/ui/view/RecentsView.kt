package com.marcmeru.merunoting.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
    var query by remember { mutableStateOf("") }

    val recentNotes = remember(notes, query) {
        notes
            .filter { it.type == "note" }
            .sortedByDescending { it.updatedAt }
            .filter {
                query.isBlank() ||
                        it.name.contains(query, ignoreCase = true) ||
                        (it.content?.contains(query, ignoreCase = true) ?: false)
            }
    }
    var noteToDelete by remember { mutableStateOf<Item?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Buscar notas…") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (recentNotes.isEmpty()) {
            Text(
                "No hay notas${if (query.isNotBlank()) " que coincidan" else " recientes"}",
                modifier = Modifier.padding(16.dp)
            )
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
                            .padding(horizontal = 8.dp),
                        onClick = { onNoteSelected(note) },
                        onDeleteClicked = { noteToDelete = it }
                    )
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
