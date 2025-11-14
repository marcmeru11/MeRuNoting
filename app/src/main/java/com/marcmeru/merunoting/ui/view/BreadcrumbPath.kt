package com.marcmeru.merunoting.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marcmeru.merunoting.data.entity.Item


@Composable
fun BreadcrumbPath(
    folders: List<Item>,
    onFolderSelected: (Long?) -> Unit,
    maxVisibleItems: Int = 4 // ajustar según diseño
) {
    val displayFolders = remember(folders) {
        if (folders.size > maxVisibleItems && maxVisibleItems > 1) {
            listOf<Item?>(null) + folders.takeLast(maxVisibleItems - 1)
        } else {
            folders.map { it }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Home,
            contentDescription = "Root",
            modifier = Modifier
                .size(24.dp)
                .clickable { onFolderSelected(null) }
        )
        Spacer(Modifier.width(8.dp))
        if (displayFolders.isNotEmpty()) {
            Text(" > ", modifier = Modifier.padding(horizontal = 4.dp))
        }
        LazyRow {
            itemsIndexed(displayFolders) { index, folder ->
                if (folder == null) {
                    Text(
                        text = "...  > ",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable { onFolderSelected(null) }
                            .padding(horizontal = 8.dp)
                    )
                } else {
                    Text(
                        text = folder.name,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { onFolderSelected(folder.id) }
                            .padding(horizontal = 4.dp)
                    )
                    if (index != displayFolders.lastIndex) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(">", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}