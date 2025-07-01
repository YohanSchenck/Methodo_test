package Methodo_test.usecase

import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import Methodo_test.domain.model.Book
import Methodo_test.domain.port.BookRepository
import io.kotest.matchers.shouldBe
import Methodo_test.domain.usecase.BookService




class BookPropertyTest : StringSpec({

    val repository = mockk<BookRepository>(relaxed = true)
    val service = BookService(repository)

    "retrieved list should contain all stored books" {
    val books = listOf(
        Book("Zebra", "Author Z"),
        Book("Animal Farm", "George Orwell"),
        Book("Brave New World", "Aldous Huxley")
    )
    every { repository.findAll() } returns books
    service.listBooks().size shouldBe 3
    }
})