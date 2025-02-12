package com.example.booknestapp.feature.book.presentation.util

sealed class Screen(val route: String) {
    object BooksScreen : Screen("books_screen")
    object AddEditBookScreen: Screen("add_edit_book_screen")
}