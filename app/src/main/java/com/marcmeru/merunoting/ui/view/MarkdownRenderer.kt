package com.marcmeru.merunoting.ui.view

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin

@Composable
fun MarkdownRenderer(
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

    val textColor = MaterialTheme.colorScheme.onSurface.toArgb()

    AndroidView(
        factory = { context ->
            TextView(context).apply {
                textSize = 15f
                setLineSpacing(6f, 1.3f)
                setPadding(0, 8, 0, 8)
                setTextColor(textColor)
                setTextIsSelectable(true)
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        update = { textView ->
            textView.setTextColor(textColor)
            if (content.isNotBlank()) {
                try {
                    markwon.setMarkdown(textView, content)
                } catch (e: Exception) {
                    textView.text = "Error: ${e.message}"
                }
            } else {
                textView.text = "Sin contenido"
            }
        },
        modifier = modifier
    )
}
