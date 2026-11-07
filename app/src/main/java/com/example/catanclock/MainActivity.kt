package com.example.catanclock

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.catanclock.ui.CatanClockScreen
import com.example.catanclock.viewmodel.CatanClockViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: CatanClockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Keep screen awake for tabletop game play
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            CatanClockScreen(viewModel = viewModel)
        }
    }
}
