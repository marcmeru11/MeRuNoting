package com.marcmeru.merunoting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.marcmeru.merunoting.ui.theme.MeRuNotingTheme
import com.marcmeru.merunoting.ui.view.MainView
import com.marcmeru.merunoting.viewModel.ItemViewModel
import com.marcmeru.merunoting.viewModel.ItemViewModelFactory
import com.marcmeru.merunoting.data.database.AppDatabase
import com.marcmeru.merunoting.data.repository.ItemRepository

class MainActivity : ComponentActivity() {
    private val database by lazy { AppDatabase.getInstance(applicationContext) }
    private val repository by lazy { ItemRepository(database.itemDao()) }
    private val viewModelFactory by lazy { ItemViewModelFactory(repository) }
    private val viewModel: ItemViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeRuNotingTheme {
                MainView(viewModel = viewModel, selectedFolderId = null)
            }
        }
    }
}

