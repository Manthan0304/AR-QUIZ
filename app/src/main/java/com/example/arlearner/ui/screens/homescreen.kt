package com.example.arlearner.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.arlearner.ui.navigation.ALPHABETSCREEN
import androidx.navigation.NavController
import com.example.arlearner.ui.navigation.QUIZSCREEN

@Composable
fun homscreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate(ALPHABETSCREEN) }) {
            Text("Alphabtes")
        }

        Button(onClick = { navController.navigate(QUIZSCREEN) }) {
            Text("Quiz")
        }
    }
}