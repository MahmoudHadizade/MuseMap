package com.mahmoud.musemap.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mahmoud.musemap.presentation.add_note.AddNoteScreen
import com.mahmoud.musemap.presentation.home.HomeScreen
import com.mahmoud.musemap.ui.theme.MuseMapTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MuseMapTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(
                            onFabClick = { navController.navigate("add_note") },
                            onNoteClick = { note -> navController.navigate("add_note?noteId=${note.id}") },
                            onMapClick = { navController.navigate("map") }
                        )
                    }

                    composable(
                        route = "add_note?noteId={noteId}",
                        arguments = listOf(
                            navArgument(name = "noteId") {
                                type = NavType.LongType
                                defaultValue = -1L
                            }
                        )
                    ) {
                        AddNoteScreen(
                            onSaveSuccess = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("map") {
                        com.mahmoud.musemap.presentation.map.MapScreen(
                            onNoteClick = { noteId ->
                                navController.navigate("add_note?noteId=$noteId")
                            }
                        )
                    }
                }
            }
        }
    }
}