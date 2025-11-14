package com.marcmeru.merunoting.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationTabs(
    tabTitles: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    openDrawer: () -> Unit
) {
    Column {
        TopAppBar(
            title = { Text("Notas") }
        )
        PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    text = { Text(title) }
                )
            }
        }
    }
}
