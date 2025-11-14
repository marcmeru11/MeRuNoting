package com.marcmeru.merunoting.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marcmeru.merunoting.data.entity.Item
import com.marcmeru.merunoting.viewModel.ItemViewModel
import kotlinx.coroutines.launch

@Composable
fun ItemsView(
    viewModel: ItemViewModel,
    selectedFolderId: Long? = null,
    onFolderSelected: (Long?) -> Unit
) {
    val rootItems by viewModel.rootItems.collectAsState()
    val childrenMap by viewModel.children.collectAsState()

    val items = if (selectedFolderId == null) rootItems else childrenMap[selectedFolderId] ?: emptyList()

    LaunchedEffect(selectedFolderId) {
        if (selectedFolderId != null) {
            viewModel.loadChildren(selectedFolderId)
        } else {
            viewModel.loadRootItems()
        }
    }

    // Obtener path para breadcrumb asincrónicamente
    var path by remember { mutableStateOf(emptyList<Item>()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(selectedFolderId) {
        scope.launch {
            val tempPath = mutableListOf<Item>()
            var folderId = selectedFolderId
            while (folderId != null) {
                val item = viewModel.getItemById(folderId)
                tempPath.add(0, item)
                folderId = item.parentId
            }
            path = tempPath
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        BreadcrumbPath(
            folders = path,
            onFolderSelected = onFolderSelected
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(items) { item ->
                if (item.type == "folder") {
                    FolderCard(
                        item = item,
                        modifier = Modifier
                            .padding(8.dp)
                            .height(160.dp)
                            .clickable { onFolderSelected(item.id) }
                    )
                } else if (item.type == "note") {
                    NoteCard(
                        item = item,
                        modifier = Modifier
                            .padding(8.dp)
                            .height(160.dp)
                    )
                }
            }
        }
    }
}

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