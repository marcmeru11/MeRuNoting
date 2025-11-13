package com.marcmeru.merunoting.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marcmeru.merunoting.data.entity.Item

@Composable
fun FoldersView() {
    val ejemploCarpeta = Item(
        id = 1L,
        parentId = null,
        name = "Mi Carpeta de Notas",
        type = "folder",
        content = null,
        createdAt = System.currentTimeMillis() - 86400000L * 10,
        updatedAt = System.currentTimeMillis() - 86400000L * 2
    )

    val carpetas = List(8) { ejemploCarpeta.copy(id = it.toLong(), name = "Carpeta #$it") }

    Column {
        Text("Contenido de Carpetas", modifier = Modifier.padding(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(carpetas) { carpeta ->
                FolderCard(item = carpeta, modifier = Modifier.padding(8.dp))
            }
        }

    }
}