package Methodo_test.usecase

import io.kotest.property.checkAll
import io.kotest.property.arbitrary.string
import io.kotest.core.spec.style.StringSpec
import io.kotest.property.arbitrary.arbitrary
import io.mockk.every
import io.mockk.mockk
import Methodo_test.domain.model.Book
import Methodo_test.domain.port.BookRepository
import io.kotest.matchers.shouldBe
import Methodo_test.domain.usecase.BookService

class BookPropertyTest : StringSpec({
    "retrieved list should contain all stored books" {
        val repository = mockk<BookRepository>()
        checkAll<List<Book>> { books ->
            every { repository.findAll() } returns books
            val service = BookService(repository)
            service.listBooks().toSet() shouldBe books.toSet()
        }
    }
})