package com.example.booknestapp.feature.book.presentation.add_edit_book

data class BookTextFieldState (
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)