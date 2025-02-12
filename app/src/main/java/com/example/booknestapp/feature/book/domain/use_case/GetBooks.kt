package com.example.booknestapp.feature.book.domain.use_case

import com.example.booknestapp.feature.book.domain.model.Book
import com.example.booknestapp.feature.book.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow

class GetBooks(
    private val repository: BookRepository
){

    operator fun invoke() : Flow<List<Book>> {
        return repository.getBooks()
    }
}