package Methodo_test.domain.port

import Methodo_test.domain.model.Book
import org.springframework.stereotype.Repository

@Repository
interface BookRepository {
    fun save(book: Book)
    fun findAll(): List<Book>
    fun reserve(title: String)
}