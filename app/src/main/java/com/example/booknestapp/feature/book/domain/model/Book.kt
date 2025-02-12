package com.example.booknestapp.feature.book.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    val title: String,
    val author: String,
    val genre: String,        // New field
    val year: Int,            // New field
    val isbn: String,         // New field
    val availability: String, // New field
    @PrimaryKey val id: Int? = null
)

class InvalidBookException(message: String) : Exception(message)
