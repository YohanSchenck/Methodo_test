package Methodo_test.usecase

import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import Methodo_test.domain.model.Book
import Methodo_test.domain.port.BookRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import Methodo_test.domain.usecase.BookService
import io.kotest.matchers.shouldBe
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


    "should reserve a book when available" {
        val book = Book("Clean Code", "Robert C. Martin", reserved = false)

        every { repository.findAll() } returns listOf(book)
        every { repository.reserve("Clean Code") } just runs

        service.reserveBook("Clean Code")

        verify { repository.reserve("Clean Code") }
    }

    "should throw exception when reserving already reserved book" {
        val book = Book("Clean Code", "Robert C. Martin", reserved = true)

        every { repository.findAll() } returns listOf(book)

        val exception = shouldThrow<IllegalStateException> {
            service.reserveBook("Clean Code")
        }

        exception.message shouldBe  "Book is already reserved"
    }

    "should throw exception when book does not exist" {
        every { repository.findAll() } returns emptyList()

        val exception = shouldThrow<IllegalArgumentException> {
            service.reserveBook("Nonexistent Book")
        }

        exception.message shouldBe "Book not found"
    }
})

