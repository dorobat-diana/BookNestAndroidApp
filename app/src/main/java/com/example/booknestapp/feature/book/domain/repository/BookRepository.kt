package com.example.booknestapp.feature.book.domain.repository

import com.example.booknestapp.feature.book.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    fun getBooks(): Flow<List<Book>>

    suspend fun getBookById(id: Int): Book?

    suspend fun insertBook(book: Book)

    suspend fun deleteBookById(id: Int?)

    suspend fun getBookByTitleAndAuthor(title: String, author: String): Book?

//    fun onDestroy()
}