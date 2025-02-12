package com.example.booknestapp.di

import android.app.Application
import androidx.room.Room
import com.example.booknestapp.feature.book.data.repository.BookRepositoryImpl
import com.example.booknestapp.feature.book.data.source.BookDataBase
import com.example.booknestapp.feature.book.domain.repository.BookRepository
import com.example.booknestapp.feature.book.domain.use_case.AddBook
import com.example.booknestapp.feature.book.domain.use_case.BookUseCases
import com.example.booknestapp.feature.book.domain.use_case.DeleteBook
import com.example.booknestapp.feature.book.domain.use_case.DestroyBook
import com.example.booknestapp.feature.book.domain.use_case.GetBook
import com.example.booknestapp.feature.book.domain.use_case.GetBooks
import com.example.booknestapp.feature.book.server.WebSocketClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBookDatabase(app: Application): BookDataBase {
        return Room.databaseBuilder(
            app,
            BookDataBase::class.java,
            BookDataBase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideBookRepository(db: BookDataBase,ws: WebSocketClient): BookRepository {
        return BookRepositoryImpl(db.bookDao,ws)
    }

    @Provides
    @Singleton
    fun provideBookUseCases(repository: BookRepository): BookUseCases {
        return BookUseCases(
            getBooks = GetBooks(repository),
            deleteBook = DeleteBook(repository),
            addBook = AddBook(repository),
            getBook = GetBook(repository),
//            destroyBook = DestroyBook(repository)
        )
    }

    @Provides
    @Singleton
    fun provideWebSocketClient(): WebSocketClient {
        return WebSocketClient()
    }
}