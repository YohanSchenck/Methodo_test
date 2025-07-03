package Methodo_test.infrastructure.driven.postgres

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import Methodo_test.domain.model.Book
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@Testcontainers
@SpringBootTest
class BookDAOTest {

    companion object {
        @Container
        val postgres = PostgreSQLContainer<Nothing>("postgres:17").apply {
            withDatabaseName("booksdb")
            withUsername("PostgreSQL 17")
            withPassword("password")
        }

        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }

    @Autowired
    lateinit var bookDAO: BookDAO

    @BeforeEach
    fun setup() {
        bookDAO.jdbcTemplate.update("DELETE FROM books", emptyMap<String, Any>())
    }

    @Test
    fun `should save and retrieve books`() {
        val book = Book("Test Title", "Test Author")
        bookDAO.save(book)

        val books = bookDAO.findAll()
        assertEquals(1, books.size)
        assertEquals("Test Title", books[0].title)
        assertEquals(false, books[0].reserved)
    }

    @Test
    fun `should reserve a book`() {
        val book = Book("Brave New World", "Aldous Huxley", reserved = false)
        bookDAO.save(book)

        bookDAO.reserve("Brave New World")

        val reservedBook = bookDAO.findAll().first { it.title == "Brave New World" }
        reservedBook.reserved shouldBe true
    }

    @Test
    fun `should not affect other books when reserving one`() {

        val book1 = Book("Book A", "Author A", reserved = false)
        val book2 = Book("Book B", "Author B", reserved = false)

        bookDAO.save(book1)
        bookDAO.save(book2)

        bookDAO.reserve("Book A")

        val all = bookDAO.findAll()
        all.find { it.title == "Book A" }?.reserved shouldBe true
        all.find { it.title == "Book B" }?.reserved shouldBe false
    }

    @Test
    fun `should fail reserving an already reserved book`() {
        val book = Book("DDD", "Eric Evans", reserved = false)
        bookDAO.save(book)

        bookDAO.reserve("DDD")

        val exception = shouldThrow<IllegalStateException> {
            bookDAO.reserve("DDD")
        }
        exception.message shouldBe "Book is already reserved or does not exist"
    }

}