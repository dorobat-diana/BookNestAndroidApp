package com.example.booknestapp.feature.book.presentation.add_edit_book

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.booknestapp.feature.book.presentation.add_edit_book.components.TransparentHintTextField
import com.example.booknestapp.feature.book.presentation.books.BooksEvent
import com.example.booknestapp.feature.book.presentation.util.Screen
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditBookScreen(
    navController: NavController,
    viewModel: AddEditBookViewModel = hiltViewModel()
) {
    val titleState = viewModel.bookTitle.value
    val authorState = viewModel.bookAuthor.value
    val genreState = viewModel.bookGenre.value
    val yearState = viewModel.bookYear.value
    val isbnState = viewModel.bookISBN.value
    val availabilityState = viewModel.bookAvailability.value
    val scaffoldState = rememberScaffoldState()
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddEditBookViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditBookViewModel.UiEvent.SaveBook ->  {
                    navController.navigate(Screen.BooksScreen.route)
                }
            }

        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Add/Edit Book") },
                navigationIcon = {
                    Button(onClick = { navController.popBackStack() }) {
                        Text("Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditBookEvent.SaveBook)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Save Book")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(16.dp)
        ) {
            // Title label and text field
            Text(
                text = "Title",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditBookEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditBookEvent.ChangeTitleFocus(it))
                },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Title label and text field
            Text(
                text = "Author",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TransparentHintTextField(
                text = authorState.text,
                hint = authorState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditBookEvent.EnteredAuthor(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditBookEvent.ChangeAuthorFocus(it))
                },
                isHintVisible = authorState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Title label and text field
            Text(
                text = "Genre",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TransparentHintTextField(
                text = genreState.text,
                hint = genreState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditBookEvent.EnteredGenre(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditBookEvent.ChangeGenreFocus(it))
                },
                isHintVisible = genreState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Title label and text field
            Text(
                text = "Year",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TransparentHintTextField(
                text = yearState.text,
                hint = yearState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditBookEvent.EnteredYear(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditBookEvent.ChangeYearFocus(it))
                },
                isHintVisible = yearState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Title label and text field
            Text(
                text = "ISBN",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TransparentHintTextField(
                text = isbnState.text,
                hint = isbnState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditBookEvent.EnteredISBN(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditBookEvent.ChangeISBNFocus(it))
                },
                isHintVisible = isbnState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Title label and text field
            Text(
                text = "Availability",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TransparentHintTextField(
                text = availabilityState.text,
                hint = availabilityState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditBookEvent.EnteredAvailability(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditBookEvent.ChangeAvailabilityFocus(it))
                },
                isHintVisible = availabilityState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))
            IconButton(
                onClick = {
                    showDialog.value = true
                },
                Modifier.padding(bottom = 8.dp)
            ) {
                androidx.compose.material.Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete book"
                )
            }

        }

    }
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { androidx.compose.material.Text(text = "Confirm Delete") },
            text = { androidx.compose.material.Text(text = "Are you sure you want to delete this book?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.DeleteBook()
                    showDialog.value = false
                    navController.navigate(Screen.BooksScreen.route)
                }) {
                    androidx.compose.material.Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    androidx.compose.material.Text("No")
                }
            }
        )
    }
}
