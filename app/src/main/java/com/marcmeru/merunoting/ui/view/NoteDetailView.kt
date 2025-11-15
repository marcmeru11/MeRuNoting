package com.marcmeru.merunoting.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marcmeru.merunoting.data.entity.Item
import com.marcmeru.merunoting.viewModel.ItemViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NoteDetailView(
    viewModel: ItemViewModel,
    note: Item,
    onNoteUpdated: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf(note.name) }
    var content by remember { mutableStateOf(note.content ?: "") }
    var isSaving by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            singleLine = true,
            textStyle = MaterialTheme.typography.headlineMedium,
            label = null
        )
        Spacer(modifier = Modifier.height(8.dp))

        val daysDiff = viewModel.getDaysDifference(note)

        val creationText = when (daysDiff) {
            0 -> "Creado hoy"
            1 -> "Creado ayer"
            in 2..Int.MAX_VALUE -> "Creado: ${formatDate(note.createdAt)}"
            else -> "Fecha desconocida"
        }

        Text(
            text = creationText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            singleLine = false,
            maxLines = Int.MAX_VALUE
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = {
                    if (!isSaving) {
                        onCancel()
                    }
                }
            ) {
                Text("Cancelar")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    isSaving = true
                    val updatedNote = note.copy(
                        name = title.trim(),
                        content = content.trim(),
                        updatedAt = System.currentTimeMillis()
                    )
                    viewModel.updateItem(updatedNote) {
                        isSaving = false
                        onNoteUpdated()
                    }
                },
                enabled = !isSaving && title.isNotBlank(),
            ) {
                Text(if (isSaving) "Guardando..." else "Guardar")
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    return if (timestamp <= 0L) {
        "Fecha desconocida"
    } else {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        sdf.format(Date(timestamp))
    }
}