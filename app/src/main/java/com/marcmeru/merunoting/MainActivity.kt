package com.marcmeru.merunoting

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.marcmeru.merunoting.data.database.AppDatabase
import com.marcmeru.merunoting.data.repository.ItemRepository
import com.marcmeru.merunoting.ui.theme.MeRuNotingTheme
import com.marcmeru.merunoting.ui.view.MainView
import com.marcmeru.merunoting.viewModel.ItemViewModel
import com.marcmeru.merunoting.viewModel.ItemViewModelFactory
import com.marcmeru.merunoting.viewModel.ThemeViewModel
import com.marcmeru.merunoting.viewModel.ThemeViewModelFactory

class MainActivity : ComponentActivity() {
    private val database by lazy { AppDatabase.getInstance(applicationContext) }
    private val repository by lazy { ItemRepository(database.itemDao()) }
    private val itemViewModelFactory by lazy { ItemViewModelFactory(repository) }
    private val themeViewModelFactory by lazy { ThemeViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val itemViewModel: ItemViewModel by viewModels { itemViewModelFactory }
        val themeViewModel: ThemeViewModel by viewModels { themeViewModelFactory }

        setContent {
            val themeMode by themeViewModel.themeMode.collectAsState()

            MeRuNotingTheme(themeMode = themeMode) {
                MainView(viewModel = itemViewModel, themeViewModel)
            }
        }
    }
}
