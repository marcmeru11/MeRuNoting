package com.marcmeru.merunoting.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.focus.onFocusEvent
import android.widget.TextView
import android.text.method.LinkMovementMethod
import com.marcmeru.merunoting.data.entity.Item
import com.marcmeru.merunoting.viewModel.ItemViewModel
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import com.marcmeru.merunoting.ui.view.utils.formatDate
import kotlinx.coroutines.delay

@Composable
fun NoteDetailView(
    viewModel: ItemViewModel,
    note: Item,
    onNoteUpdated: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditMode by remember { mutableStateOf(false) }
    var editingTitle by remember { mutableStateOf(note.name) }
    var editingContent by remember { mutableStateOf(note.content ?: "") }
    var contentFocused by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    LaunchedEffect(note) {
        if (!isEditMode) {
            editingTitle = note.name
            editingContent = note.content ?: ""
        }
    }

    LaunchedEffect(editingTitle, editingContent) {
        if (isEditMode) {
            delay(500)
            if (editingTitle.isNotBlank()) {
                val updatedNote = note.copy(
                    name = editingTitle.trim(),
                    content = editingContent.trim(),
                    updatedAt = System.currentTimeMillis()
                )
                viewModel.updateItem(updatedNote) {}
            }
        }
    }

    LaunchedEffect(contentFocused) {
        if (contentFocused) {
            delay(300)
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        NoteHeader(
            title = editingTitle,
            isEditMode = isEditMode,
            onTitleChange = { editingTitle = it },
            onToggleEditMode = { isEditMode = !isEditMode }
        )

        val daysDiff = viewModel.getDaysDifference(note)
        val creationText = when (daysDiff) {
            0 -> "Creado hoy"
            1 -> "Creado ayer"
            in 2..Int.MAX_VALUE -> "Creado: ${formatDate(note.createdAt)}"
            else -> "Fecha desconocida"
        }
        Text(
            text = creationText,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Divider(
            modifier = Modifier.padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        if (isEditMode) {
            TextField(
                value = editingContent,
                onValueChange = { editingContent = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .onFocusEvent { contentFocused = it.isFocused },
                placeholder = { Text("Escribe en markdown...") },
                singleLine = false,
                maxLines = Int.MAX_VALUE,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                )
            )
        } else {
            MarkdownRenderer(
                content = editingContent,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (!isEditMode) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onCancel) {
                    Text("Volver")
                }
            }
        }
    }
}