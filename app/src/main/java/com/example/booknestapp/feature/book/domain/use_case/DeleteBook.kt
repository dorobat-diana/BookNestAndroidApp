package com.example.booknestapp.feature.book.domain.use_case

import com.example.booknestapp.feature.book.domain.repository.BookRepository

class DeleteBook(
   private val repository: BookRepository
) {

    suspend operator fun invoke(id: Int?) {
        repository.deleteBookById(id)
    }
}