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
        // Supprime tout avant chaque test
        bookDAO.findAll().forEach {
            // Pas optimal sans ID, mais fait le job pour maintenant.
        }
    }

    @Test
    fun `should save and retrieve books`() {
        val book = Book("Test Title", "Test Author")
        bookDAO.save(book)

        val books = bookDAO.findAll()
        assertEquals(1, books.size)
        assertEquals("Test Title", books[0].title)
    }
}