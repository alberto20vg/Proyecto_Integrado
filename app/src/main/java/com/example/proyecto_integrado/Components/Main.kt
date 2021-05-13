package com.example.proyecto_integrado.Components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.chrisbanes.accompanist.coil.CoilImage

class Main : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    BookList()
                }

        }
    }
}

@Composable
fun BookList(
    booksViewModel: BooksViewModel = viewModel(factory = BookViewModelFactory(BooksRepo()))) {

    when (val booksList = booksViewModel.getBooksInfo().collectAsState(initial = null).value) {

        is OnError -> {
            Text(text = "Please try after sometime")
        }

        is OnSuccess -> {
            val listOfBooks = booksList.querySnapshot?.toObjects(Book::class.java)
            listOfBooks?.let {
                Column {
                    Text(
                        text = "Bookish",
                        style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.ExtraBold),
                        modifier = Modifier.padding(16.dp)
                    )

                    LazyColumn(modifier = Modifier.fillMaxHeight()) {
                        items(listOfBooks) {
                            Card(modifier = Modifier.fillMaxWidth().padding(16.dp), shape = RoundedCornerShape(16.dp)) {
                                BookDetails(it)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BookDetails(book: Book) {
    var showBookDescription by remember { mutableStateOf(false) }
    val bookCoverImageSize by animateDpAsState(targetValue = if (showBookDescription) 50.dp else 80.dp )

    Column(modifier = Modifier.clickable {
        showBookDescription = showBookDescription.not()
    }) {
        Row(modifier = Modifier.padding(12.dp)) {
            CoilImage(
                data = book.image,
                contentDescription = "Book cover page",
                fadeIn = true,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(bookCoverImageSize)
            )

            Column {
                Text(text = book.name, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp))
                Text(text = book.author, style = TextStyle(fontWeight = FontWeight.Light, fontSize = 12.sp))
            }
        }

        AnimatedVisibility(visible = showBookDescription) {
            Text(text = book.description, style = TextStyle(fontWeight = FontWeight.SemiBold,fontStyle = FontStyle.Italic),
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
            )
        }
    }
}

class BookViewModelFactory(private val booksRepo: BooksRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BooksViewModel::class.java)) {
            return BooksViewModel(booksRepo) as T
        }
        throw IllegalStateException()
    }
}