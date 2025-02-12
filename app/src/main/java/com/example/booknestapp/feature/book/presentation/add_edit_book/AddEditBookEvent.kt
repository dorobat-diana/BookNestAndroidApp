package com.example.booknestapp.feature.book.presentation.add_edit_book

import androidx.compose.ui.focus.FocusState

sealed class AddEditBookEvent {
    data class EnteredTitle(val value: String): AddEditBookEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditBookEvent()

    data class EnteredAuthor(val value: String): AddEditBookEvent()
    data class ChangeAuthorFocus(val focusState: FocusState): AddEditBookEvent()

    data class EnteredGenre(val value: String): AddEditBookEvent()
    data class ChangeGenreFocus(val focusState: FocusState): AddEditBookEvent()

    data class EnteredYear(val value: String): AddEditBookEvent()
    data class ChangeYearFocus(val focusState: FocusState): AddEditBookEvent()

    data class EnteredISBN(val value: String): AddEditBookEvent()
    data class ChangeISBNFocus(val focusState: FocusState): AddEditBookEvent()

    data class EnteredAvailability(val value: String): AddEditBookEvent()
    data class ChangeAvailabilityFocus(val focusState: FocusState): AddEditBookEvent()

    object SaveBook: AddEditBookEvent()
}