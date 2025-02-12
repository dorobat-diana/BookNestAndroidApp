package com.example.booknestapp.feature.book.presentation.add_edit_book

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booknestapp.feature.book.domain.model.Book
import com.example.booknestapp.feature.book.domain.model.InvalidBookException
import com.example.booknestapp.feature.book.domain.use_case.BookUseCases
import com.example.booknestapp.feature.book.presentation.add_edit_book.AddEditBookViewModel.UiEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditBookViewModel @Inject constructor (
    private val bookUseCases: BookUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _bookTitle = mutableStateOf(
        BookTextFieldState(
            hint = "Enter Title"
        )
    )
    val bookTitle: State<BookTextFieldState> = _bookTitle
    private val _bookAuthor = mutableStateOf(
        BookTextFieldState(
            hint = "Enter Author"
        )
    )
    val bookAuthor: State<BookTextFieldState> = _bookAuthor

    private val _bookGenre = mutableStateOf(
        BookTextFieldState(
            hint = "Enter Genre"
        )
    )
    val bookGenre: State<BookTextFieldState> = _bookGenre

    private val _bookYear = mutableStateOf(
        BookTextFieldState(
            hint = "Enter Year"
        )
    )
    val bookYear: State<BookTextFieldState> = _bookYear

    private val _bookISBN = mutableStateOf(
        BookTextFieldState(
            hint = "Enter ISBN"
        )
    )
    val bookISBN: State<BookTextFieldState> = _bookISBN

    private val _bookAvailability = mutableStateOf(
        BookTextFieldState(
            hint = "Enter Availability"
        )
    )
    val bookAvailability: State<BookTextFieldState> = _bookAvailability

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var _currentBookId: Int? = null

    init {
        savedStateHandle.get<Int>("bookId")?.let { bookId ->
            if(bookId != -1) {
                viewModelScope.launch {
                    bookUseCases.getBook(bookId)?.also {
                        _currentBookId = it.id
                        _bookTitle.value = bookTitle.value.copy(
                            text = it.title,
                            isHintVisible = false
                        )
                        _bookAuthor.value = bookAuthor.value.copy(
                            text = it.author,
                            isHintVisible = false
                        )
                        _bookGenre.value = bookGenre.value.copy(
                            text = it.genre,
                            isHintVisible = false
                        )
                        _bookYear.value = bookYear.value.copy(
                            text = it.year.toString(),
                            isHintVisible = false
                        )
                        _bookISBN.value = bookISBN.value.copy(
                            text = it.isbn,
                            isHintVisible = false
                        )
                        _bookAvailability.value = bookAvailability.value.copy(
                            text = it.availability,
                            isHintVisible = false
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditBookEvent) {
        when(event) {
            is AddEditBookEvent.EnteredTitle -> {
                _bookTitle.value = bookTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditBookEvent.ChangeTitleFocus -> {
                _bookTitle.value = bookTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && bookTitle.value.text.isBlank()
                )
            }
            is AddEditBookEvent.EnteredAuthor -> {
                _bookAuthor.value = bookAuthor.value.copy(
                    text = event.value
                )
            }
            is AddEditBookEvent.ChangeAuthorFocus -> {
                _bookAuthor.value = bookAuthor.value.copy(
                    isHintVisible = !event.focusState.isFocused && bookAuthor.value.text.isBlank()
                )
            }
            is AddEditBookEvent.ChangeGenreFocus -> {
                _bookGenre.value = bookGenre.value.copy(
                    isHintVisible = !event.focusState.isFocused && bookGenre.value.text.isBlank()
                )
            }
            is AddEditBookEvent.ChangeYearFocus -> {
                _bookYear.value = bookYear.value.copy(
                    isHintVisible = !event.focusState.isFocused && bookYear.value.text.isBlank()
                )
            }
            is AddEditBookEvent.ChangeISBNFocus -> {
                _bookISBN.value = bookISBN.value.copy(
                    isHintVisible = !event.focusState.isFocused && bookISBN.value.text.isBlank()
                )
            }
            is AddEditBookEvent.ChangeAvailabilityFocus -> {
                _bookAvailability.value = bookAvailability.value.copy(
                    isHintVisible = !event.focusState.isFocused && bookAvailability.value.text.isBlank()
                )
            }

            is AddEditBookEvent.SaveBook -> {
                viewModelScope.launch{
                    try {
                        val year = bookYear.value.text.toIntOrNull() // Safely convert to Int
                        if (year == null || year < 0 || year > 2100) {
                            _eventFlow.emit(
                                UiEvent.ShowSnackBar(
                                    message = "Please enter a valid year between 0 and 2100."
                                )
                            )
                            return@launch
                        }
                        bookUseCases.addBook(
                            Book(
                                title = bookTitle.value.text,
                                author = bookAuthor.value.text,
                                genre = bookGenre.value.text,
                                year = bookYear.value.text.toInt(),
                                isbn = bookISBN.value.text,
                                availability = bookAvailability.value.text,
                                id = _currentBookId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveBook)
                    } catch(e: InvalidBookException) {
                        _eventFlow.emit(
                            ShowSnackBar(
                                message = e.message ?: "Couldn't save book"
                            )
                        )
                    }
                }
            }

            is AddEditBookEvent.EnteredAvailability -> {
                _bookAvailability.value = bookAvailability.value.copy(
                    text = event.value
                )
            }
            is AddEditBookEvent.EnteredGenre -> {
                _bookGenre.value = bookGenre.value.copy(
                    text = event.value
                )
            }
            is AddEditBookEvent.EnteredISBN -> {
                _bookISBN.value = bookISBN.value.copy(
                    text = event.value
                )
            }
            is AddEditBookEvent.EnteredYear -> {
                _bookYear.value = bookYear.value.copy(
                    text = event.value
                )
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String): UiEvent()
        object SaveBook: UiEvent()
    }

    public fun DeleteBook () {
        viewModelScope.launch{
            bookUseCases.deleteBook(_currentBookId)
        }
    }
}