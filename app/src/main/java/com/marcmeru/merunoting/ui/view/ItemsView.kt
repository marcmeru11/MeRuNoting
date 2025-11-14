package com.marcmeru.merunoting.ui.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marcmeru.merunoting.viewModel.ItemViewModel

@Composable
fun ItemsView(
    viewModel: ItemViewModel,
    selectedFolderId: Long? = null
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

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(items) { item ->
            if (item.type == "folder") {
                FolderCard(
                    item = item,
                    modifier = Modifier
                        .padding(8.dp)
                        .height(160.dp)
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
