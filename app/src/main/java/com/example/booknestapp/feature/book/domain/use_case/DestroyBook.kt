package com.example.booknestapp.feature.book.domain.use_case

import com.example.booknestapp.feature.book.domain.model.Book
import com.example.booknestapp.feature.book.domain.repository.BookRepository

class DestroyBook (
    private val repository: BookRepository
){
//    operator fun invoke(id: Int){
//        repository.onDestroy()
//    }
}