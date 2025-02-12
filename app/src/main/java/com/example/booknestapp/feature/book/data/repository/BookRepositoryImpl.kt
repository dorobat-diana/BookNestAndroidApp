package com.example.booknestapp.feature.book.data.repository

import android.util.Log
import com.example.booknestapp.feature.book.data.source.BookDao
import com.example.booknestapp.feature.book.domain.model.Book
import com.example.booknestapp.feature.book.domain.repository.BookRepository
import com.example.booknestapp.feature.book.server.BookApiService
import com.example.booknestapp.feature.book.server.RetrofitClient
import com.example.booknestapp.feature.book.server.WebSocketClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resumeWithException

class BookRepositoryImpl(
    private val dao: BookDao,
    private val webSocketClient: WebSocketClient
): BookRepository {

    val bookService = RetrofitClient.instance.create(BookApiService::class.java)
    private val retryList = mutableListOf<Book>()
    private var retrying = false // To prevent multiple retry coroutines

    init {
        fetchAndSaveBooks()
    }

    private fun setupWebSocketListener() {
        webSocketClient.startWebSocket()
    }

    // Retry syncing books in the retryList to the server
    private fun startRetrying() {
        if (retrying) return
        retrying = true

        GlobalScope.launch(Dispatchers.IO) {
            while (retryList.isNotEmpty()) {
                val iterator = retryList.iterator()
                while (iterator.hasNext()) {
                    val book = iterator.next()
                    try {
                        val serverBook = book.toServerModel()
                        val call = bookService.addBook(serverBook)
                        val response = call.execute() // Synchronous call for retrying
                        if (response.isSuccessful) {
                            iterator.remove() // Remove successfully synced book
                            Log.d("BookRepository", "Successfully retried adding book: ${book.title}")
                        } else {
                            Log.e("BookRepository", "Failed to retry adding book: ${response.errorBody()?.string()}")
                        }
                    } catch (e: Exception) {
                        Log.e("BookRepository", "Error during retrying: ${e.message}")
                    }
                }
                delay(5000) // Wait 5 seconds before retrying
            }
            retrying = false // Stop retrying when the list is empty
        }
    }

    fun fetchAndSaveBooks() {
        setupWebSocketListener()
        val call = bookService.getBooks()

        call.enqueue(object : Callback<List<com.example.booknestapp.feature.book.server.Book>> {
            override fun onResponse(call: Call<List<com.example.booknestapp.feature.book.server.Book>>, response: Response<List<com.example.booknestapp.feature.book.server.Book>>) {
                if (response.isSuccessful) {
                    response.body()?.let { books ->
                        Log.d("Clicked","Books fetched: $books")
                        GlobalScope.launch(Dispatchers.IO) {
                            // 1. Delete all books in the database
                            deleteAllBooks()
                            // 2. Insert all the fetched books into the database
                            insertFetchedBooks(books)
                        }
                    }
                } else {
                    Log.e("BookRepository", "Failed to fetch books from server")
                }
            }

            override fun onFailure(call: Call<List<com.example.booknestapp.feature.book.server.Book>>, t: Throwable) {
                Log.e("BookRepository", "Error fetching books: ${t.message}")
            }
        })
    }

    private fun deleteAllBooks() {
        // Use DAO to delete all books
        dao.deleteAllBooks()
    }

    private suspend fun insertFetchedBooks(books: List<com.example.booknestapp.feature.book.server.Book>) {
        // Switch to the IO dispatcher to perform DB operations on a background thread
        withContext(Dispatchers.IO) {
            books.forEach { book ->
                val localBook = book.toDomainModel()  // Convert to domain model
                dao.insertBook(localBook) // This is a suspend function
                Log.d("BookRepository","Fetched $books")
            }
        }
    }

    // Map server model to domain model
    private fun com.example.booknestapp.feature.book.server.Book.toDomainModel(): Book {
        return Book(
            id = this.id,
            title = this.title,
            author = this.author,
            genre = this.genre,
            year = this.year,
            isbn = this.isbn,
            availability = this.availability
        )
    }

    // Map domain model to server model
    private fun Book.toServerModel(): com.example.booknestapp.feature.book.server.Book {
        return com.example.booknestapp.feature.book.server.Book(
            id = this.id,
            title = this.title,
            author = this.author,
            genre = this.genre,
            year = this.year,
            isbn = this.isbn,
            availability = this.availability
        )
    }

//    override fun onDestroy() {
//        webSocketClient.closeWebSocket()
//        Log.d("BookRepository", "WebSocket connection closed.")
//    }

    fun checkSizeDaoBooks(): Int {
        var sizeOfBooks = 0
        dao.getBooks().onEach { books ->
            sizeOfBooks=books.size
        }
        return sizeOfBooks
    }

    override fun getBooks(): Flow<List<Book>> {
        val sizee = checkSizeDaoBooks()
        if (sizee == 0) {
            fetchAndSaveBooks()
        }
        return dao.getBooks()
    }

    override suspend fun getBookById(id: Int): Book? {
        return suspendCancellableCoroutine { continuation ->
            val call = bookService.getBookById(id) // Fetching a single book by ID from server
            call.enqueue(object : Callback<com.example.booknestapp.feature.book.server.Book> {
                override fun onResponse(
                    call: Call<com.example.booknestapp.feature.book.server.Book?>,
                    response: Response<com.example.booknestapp.feature.book.server.Book?>
                ) {
                    if (response.isSuccessful) {
                        Log.d("BookRepository", "Successfully retrieved book from the server.")
                        continuation.resume(response.body()?.toDomainModel(), onCancellation = null)  // Return the book if successful
                    } else {
                        Log.e("BookRepository", "Failed to retrieve book from the server.")
                        continuation.resumeWithException(Exception("Failed to retrieve book"))
                    }
                }

                override fun onFailure(call: Call<com.example.booknestapp.feature.book.server.Book>, t: Throwable) {
                    Log.e("BookRepository", "Error retrieving book from server: ${t.message}")
                    continuation.resumeWithException(t)  // Propagate the error
                }
            })
        }
    }

    override suspend fun insertBook(book: Book) {
        if (book.id != null && dao.getBookById(book.id) != null) {
            // If the book exists, update it on the server
            val serverBook = book.toServerModel()  // Convert to server model
            val call = bookService.updateBook(serverBook.id, serverBook)
            call.enqueue(object : Callback<com.example.booknestapp.feature.book.server.Book> {
                override fun onResponse(call: Call<com.example.booknestapp.feature.book.server.Book>, response: Response<com.example.booknestapp.feature.book.server.Book>) {
                    if (response.isSuccessful) {
                        Log.d("BookRepository", "Book updated successfully on the server.")
                    } else {
                        Log.e("BookRepository", "Failed to update book on the server.")
                    }
                }

                override fun onFailure(call: Call<com.example.booknestapp.feature.book.server.Book>, t: Throwable) {
                    Log.e("BookRepository", "Error updating book on server: ${t.message}")
                }
            })
            dao.insertBook(book)
        } else {
            // If the book doesn't exist, add it to the server
            val newId = (dao.getMaxBookId() ?: 0) + 1
            val newBook = book.copy(id = newId)
            dao.insertBook(newBook)

            val serverBook = newBook.toServerModel()  // Convert to server model
            val call = bookService.addBook(serverBook)
            call.enqueue(object : Callback<com.example.booknestapp.feature.book.server.Book> {
                override fun onResponse(call: Call<com.example.booknestapp.feature.book.server.Book>, response: Response<com.example.booknestapp.feature.book.server.Book>) {
                    if (response.isSuccessful) {
                        Log.d("BookRepository", "Book added successfully to the server.")
                    } else {
                        Log.e("BookRepository", "Failed to add book to the server.")
                    }
                }

                override fun onFailure(call: Call<com.example.booknestapp.feature.book.server.Book>, t: Throwable) {
                    Log.e("BookRepository", "Error adding book to server: ${t.message}")
                    retryList.add(newBook) // Add to retry list if the server fails
                    startRetrying()
                }
            })
        }

    }

    override suspend fun deleteBookById(id: Int?) {
        dao.deleteBookById(id)
        // Sync with server after deleting a book
        val call = bookService.deleteBookById(id)  // Assuming there's an endpoint to delete books
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("BookRepository", "Successfully deleted book from the server.")
                } else {
                    Log.e("BookRepository", "Failed to delete book from the server.")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("BookRepository", "Error deleting book from server: ${t.message}")
            }
        })
    }

    override suspend fun getBookByTitleAndAuthor(title: String, author: String): Book? {
        return dao.getBookByTitleAndAuthor(title, author)
    }
}
