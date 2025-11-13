package com.marcmeru.merunoting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.marcmeru.merunoting.ui.theme.MeRuNotingTheme
import com.marcmeru.merunoting.ui.view.MainView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeRuNotingTheme {
                MainView()
            }
        }
    }
}
