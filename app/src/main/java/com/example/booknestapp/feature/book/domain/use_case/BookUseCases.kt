package com.example.booknestapp.feature.book.domain.use_case

data class BookUseCases (
    val getBooks: GetBooks,
    val deleteBook: DeleteBook,
    val addBook: AddBook,
    val getBook: GetBook,
//    val destroyBook: DestroyBook
)