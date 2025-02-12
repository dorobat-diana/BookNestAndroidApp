package com.example.booknestapp.feature.book.presentation.books

import com.example.booknestapp.feature.book.domain.model.Book

data class BooksState (
    val books: List<Book> = emptyList()
)