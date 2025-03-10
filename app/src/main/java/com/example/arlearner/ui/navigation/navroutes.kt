package com.example.arlearner.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object HOMESCREEN

@Serializable
object QUIZSCREEN

@Serializable
object ALPHABETSCREEN

@Serializable
data class ARSCREEN(val model:String)