package com.example.booknestapp.feature.book.domain.use_case

import com.example.booknestapp.feature.book.domain.model.Book
import com.example.booknestapp.feature.book.domain.repository.BookRepository

class GetBook(
    private val repository: BookRepository
) {

    suspend operator fun invoke(id: Int): Book? {
        return repository.getBookById(id)
    }
}