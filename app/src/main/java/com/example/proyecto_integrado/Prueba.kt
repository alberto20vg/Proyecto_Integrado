package com.example.proyecto_integrado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel


class Prueba : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScreenDemo()
        }
    }

    @Composable
    fun ScreenDemo(model: ViewModelCreatePost = viewModel()) { // 2.
        val count by model.counterLiveData.observeAsState(0) // 3.
        Demo("This is $count") { model.increaseCounter() }
    }

    // 4.
    @Composable
    fun Demo(text: String, onClick: () -> Unit = {}) {
        Column {
            BasicText(text)
            Button(
                onClick = onClick,
            ) {
                BasicText(text = "Add 1")
            }
        }
    }
}

