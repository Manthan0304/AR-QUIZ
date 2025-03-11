package com.example.arlearner

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.arlearner.ui.navigation.ALPHABETSCREEN
import com.example.arlearner.ui.navigation.ARSCREEN
import com.example.arlearner.ui.navigation.HOMESCREEN
import com.example.arlearner.ui.navigation.QUIZSCREEN
import com.example.arlearner.ui.screens.ARscreen
import com.example.arlearner.ui.screens.alphabetscreen
import com.example.arlearner.ui.screens.homscreen
import com.example.arlearner.ui.screens.quizscreen
import com.example.arlearner.ui.theme.ARlearnerTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ARlearnerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = HOMESCREEN,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<HOMESCREEN> {
                            homscreen(navController)
                        }

                        composable<ARSCREEN> {
                            val alphabet = it.toRoute<ARSCREEN>().model
                            ARscreen(navController, alphabet)
                        }

                        composable<QUIZSCREEN> {
                            quizscreen(navController)
                        }

                        composable<ALPHABETSCREEN> {
                            alphabetscreen(navController)
                        }
                    }
                }
            }
        }
    }
}
