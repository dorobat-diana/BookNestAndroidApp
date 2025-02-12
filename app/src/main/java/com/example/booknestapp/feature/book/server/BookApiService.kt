package com.example.booknestapp.feature.book.server

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class Book(
    val id: Int?,
    val title: String,
    val author: String,
    val genre: String,        // New field
    val year: Int,            // New field
    val isbn: String,         // New field
    val availability: String, // New field
)

interface BookApiService {
    @GET("/api/books")
    fun getBooks(): Call<List<Book>>
    @DELETE("/api/books/{id}")
    fun deleteBookById(@Path("id") id: Int?): Call<Void>
    // POST request for adding a new book
    @POST("/api/books")
    fun addBook(@Body book: Book): Call<Book>
    // PUT request for updating an existing book
    @PUT("/api/books/{id}")
    fun updateBook(@Path("id") id: Int?, @Body book: Book): Call<Book>
    @GET("/api/books/{id}")
    fun getBookById(@Path("id") id: Int): Call<Book>
}
