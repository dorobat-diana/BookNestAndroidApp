package com.example.booknestapp.feature.book.presentation.books

import com.example.booknestapp.feature.book.domain.use_case.GetBooks

sealed class BooksEvent {
    data class DeleteBook(val bookId: Int?): BooksEvent()
    object GetBooks : BooksEvent()
}