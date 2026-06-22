package com.example.mycoffeeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mycoffeeapp.di.ServiceLocator
//import com.example.mycoffeeapp.hardware.camera.Permission
import com.example.mycoffeeapp.ui.navigation.NavGraph
import com.example.mycoffeeapp.ui.theme.MyCoffeeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ServiceLocator.init(this)
        enableEdgeToEdge()
        setContent {
            MyCoffeeAppTheme {
                NavGraph()
//                Permission()
                }
            }
        }
    }