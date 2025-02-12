package com.example.booknestapp.feature.book.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.booknestapp.feature.book.domain.model.Book

@Database(
    entities = [Book::class],
    version = 2,
    exportSchema = false
)

abstract class BookDataBase : RoomDatabase(){

    abstract val bookDao: BookDao

    companion object{
        const val DATABASE_NAME = "book_db"
    }
}