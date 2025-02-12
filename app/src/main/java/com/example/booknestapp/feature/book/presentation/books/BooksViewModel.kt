package com.example.booknestapp.feature.book.presentation.books

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booknestapp.feature.book.domain.use_case.BookUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(
    private val bookUseCases: BookUseCases
): ViewModel() {

    private val _state = mutableStateOf(BooksState())
    val state: State<BooksState> = _state


    private val _hasFetchedBooks = mutableStateOf(false)
    val hasFetchedBooks: State<Boolean> = _hasFetchedBooks

    init {
        getBooks()
    }

    fun OnEvent(event: BooksEvent){
        when(event) {
            is BooksEvent.DeleteBook -> {
                viewModelScope.launch{
                    bookUseCases.deleteBook(event.bookId)
                }
            }
            is BooksEvent.GetBooks -> {
                // Trigger fetching books
                getBooks()
            }
        }
    }

    private fun getBooks() {
        bookUseCases.getBooks()
            .onEach { books ->
                _state.value = state.value.copy(
                    books = books
                )
                if(books.size != 0) {
                    _hasFetchedBooks.value = true
                }
                Log.d("Clicked","I clicked got books $books")
            }
            .launchIn(viewModelScope)
    }

//    override fun onCleared() {
//        super.onCleared()
//        bookUseCases.destroyBook
//    }
}