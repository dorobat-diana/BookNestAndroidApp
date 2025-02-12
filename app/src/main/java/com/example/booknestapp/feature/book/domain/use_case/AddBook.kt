package com.example.booknestapp.feature.book.domain.use_case

import com.example.booknestapp.feature.book.domain.model.Book
import com.example.booknestapp.feature.book.domain.model.InvalidBookException
import com.example.booknestapp.feature.book.domain.repository.BookRepository
import kotlin.jvm.Throws

class AddBook (
    private val repository: BookRepository
) {

    @Throws(InvalidBookException::class)
    suspend operator fun invoke(book: Book) {
        if(book.title.isBlank()) {
            throw InvalidBookException("The title of the book can't be empty.")
        }

        if(book.author.isBlank()) {
            throw InvalidBookException("The author of the book can't be empty.")
        }

        if(book.year == null) {
            throw InvalidBookException("The year of the book can't be empty or string.")
        }

        if(book.genre.isBlank()) {
            throw InvalidBookException("The genre of the book can't be empty.")
        }
        if(book.isbn.isBlank()) {
            throw InvalidBookException("The isbn of the book can't be empty.")
        }
        if(book.availability.isBlank()) {
            throw InvalidBookException("The availability of the book can't be empty.")
        }

        repository.insertBook(book)
    }

}