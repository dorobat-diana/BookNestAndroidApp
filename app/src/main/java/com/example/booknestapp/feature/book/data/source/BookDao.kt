package com.example.booknestapp.feature.book.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.booknestapp.feature.book.domain.model.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM book")
    fun getBooks(): Flow<List<Book>>

    @Query("SELECT * FROM book WHERE id = :id")
    suspend fun getBookById(id :Int): Book?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    // New method to delete a Book based on its id
    @Query("DELETE FROM book WHERE id = :id")
    suspend fun deleteBookById(id: Int?)

    // Query to get a book by title and author
    @Query("SELECT * FROM book WHERE title = :title AND author = :author LIMIT 1")
    suspend fun getBookByTitleAndAuthor(title: String, author: String): Book?

    @Query("DELETE FROM book")
    fun deleteAllBooks()

    @Query("SELECT MAX(id) FROM book")
    suspend fun getMaxBookId(): Int?
}