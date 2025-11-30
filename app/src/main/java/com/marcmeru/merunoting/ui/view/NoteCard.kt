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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.toArgb
import android.widget.TextView
import android.text.method.LinkMovementMethod
import androidx.compose.ui.text.capitalize
import com.marcmeru.merunoting.data.entity.Item
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import java.util.Locale

@Composable
fun NoteCard(
    item: Item,
    modifier: Modifier = Modifier,
    onDeleteClicked: (Item) -> Unit = {},
    onNoteClick: () -> Unit = {}
) {
    if (item.type == "note") {
        var expanded by remember { mutableStateOf(false) }

        Surface(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(top = 0.dp, start = 10.dp, bottom = 5.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(end = 40.dp)
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

                MarkdownPreview(
                    content = item.content ?: "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 120.dp)
                )
            }
        }
    }
}

@Composable
private fun MarkdownPreview(
    content: String,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val markwon = remember {
        Markwon.builder(context)
            .usePlugin(StrikethroughPlugin())
            .usePlugin(TablePlugin.create(context))
            .usePlugin(TaskListPlugin.create(context))
            .build()
    }

    val textColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()

    AndroidView(
        factory = { context ->
            TextView(context).apply {
                textSize = 12f
                setLineSpacing(4f, 1.2f)
                setPadding(0, 4, 0, 4)
                setTextColor(textColor)
                maxLines = 5
                ellipsize = android.text.TextUtils.TruncateAt.END
                isClickable = false
                isFocusable = false
                isLongClickable = false
            }
        },
        update = { textView ->
            textView.setTextColor(textColor)
            if (content.isNotBlank()) {
                try {
                    markwon.setMarkdown(textView, content)
                } catch (e: Exception) {
                    textView.text = content
                }
            } else {
                textView.text = ""
            }
        },
        modifier = modifier
    )
}