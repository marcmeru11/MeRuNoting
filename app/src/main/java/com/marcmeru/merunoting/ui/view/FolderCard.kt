package com.marcmeru.merunoting.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.marcmeru.merunoting.data.entity.Item
import java.util.Locale

@Composable
fun FolderCard(
    item: Item,
    modifier: Modifier = Modifier,
    onDeleteClicked: (Item) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var showConfirmDelete by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp) // más alto para espacio vertical
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.Folder,
                    contentDescription = "Carpeta",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterStart)
                )

                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Opciones carpeta"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        onClick = {
                            expanded = false
                            showConfirmDelete = true
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(0.dp))

            Text(
                text = item.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 0.dp, end = 10.dp, bottom = 0.dp)
            )

        }
    }

    if (showConfirmDelete) {
        AlertDialog(
            onDismissRequest = { showConfirmDelete = false },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Eliminar la carpeta \"${item.name}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClicked(item)
                        showConfirmDelete = false
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmDelete = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
