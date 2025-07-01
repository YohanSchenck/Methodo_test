package Methodo_test.domain.usecase

import Methodo_test.domain.model.Book
import Methodo_test.domain.port.BookRepository

class BookService(private val repository: BookRepository) {

    fun addBook(book: Book) {
        repository.save(book)
    }

    fun listBooks(): List<Book> {
        return repository.findAll().sortedBy { it.title }
    }
}