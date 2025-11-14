package com.marcmeru.merunoting.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.marcmeru.merunoting.data.entity.Item

@Composable
fun NoteCard(
    item: Item,
    modifier: Modifier = Modifier,
    onDeleteClicked: (Item) -> Unit = {}
) {
    if (item.type == "note") {
        var expanded by remember { mutableStateOf(false) }

        Surface(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    IconButton(
                        onClick = { expanded = true },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Más opciones"
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
                                onDeleteClicked(item)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.content ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
