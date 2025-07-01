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
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.*


class BookServiceTest : StringSpec({

    val repository = mockk<BookRepository>(relaxed = true)
    val service = BookService(repository)

    "should add a book" {
        val book = Book("1984", "George Orwell")
        service.addBook(book)
        verify { repository.save(book) }
    }

    "should list all books sorted by title" {
        val books = listOf(
            Book("Zebra", "Author Z"),
            Book("Animal Farm", "George Orwell"),
            Book("Brave New World", "Aldous Huxley")
        )
        every { repository.findAll() } returns books

        val result = service.listBooks()
        result shouldContainExactly books.sortedBy { it.title }
    }
})

//class BookPropertyTest : StringSpec({
//    "retrieved list should contain all stored books" {
//        val repository = mockk<BookRepository>()
//        checkAll<List<Book>> { books ->
//            every { repository.findAll() } returns books
//            val service = BookService(repository)
//            service.listBooks().toSet() shouldBe books.toSet()
//        }
//    }
//})