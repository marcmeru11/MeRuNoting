package com.marcmeru.merunoting.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.marcmeru.merunoting.data.entity.Item

@Composable
fun RecentNoteCard(
    item: Item,
    modifier: Modifier = Modifier,
    onClick: (Item) -> Unit = {},
    onDeleteClicked: (Item) -> Unit = {}
) {
    if (item.type != "note") return

    var expanded by remember { mutableStateOf(false) } // Estado para el menú

    val now = System.currentTimeMillis()
    val diffMillis = now - item.updatedAt
    val hours = java.util.concurrent.TimeUnit.MILLISECONDS.toHours(diffMillis)
    val days = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(diffMillis)
    val timeText = when {
        hours < 1 -> "Hace menos de una hora"
        hours < 24 -> "Hace $hours ${if (hours == 1L) "hora" else "horas"}"
        else -> "Hace $days ${if (days == 1L) "día" else "días"}"
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        onClick = { onClick(item) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.content ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = timeText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .widthIn(max = 90.dp)
                    .align(Alignment.CenterVertically)
            )

            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Más opciones",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
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
        }
    }
}
