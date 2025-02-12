package com.example.booknestapp.feature.book.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.booknestapp.feature.book.presentation.add_edit_book.AddEditBookScreen
import com.example.booknestapp.feature.book.presentation.books.BooksScreen
import com.example.booknestapp.feature.book.presentation.util.Screen
import com.example.booknestapp.feature.book.server.WebSocketClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var webSocketClient: WebSocketClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.BooksScreen.route
                    ) {
                        composable(route = Screen.BooksScreen.route) {
                            BooksScreen(navController = navController)
                        }
                        composable(route = Screen.AddEditBookScreen.route +
                                "?bookId={bookId}",
                            arguments = listOf(
                                navArgument(
                                    name = "bookId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            AddEditBookScreen(navController = navController)
                        }
                    }
                }
            }
        }

    }
}