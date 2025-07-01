package Methodo_test.domain.port

import Methodo_test.domain.model.Book

interface BookRepository {
    fun save(book: Book)
    fun findAll(): List<Book>
}