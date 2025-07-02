package Methodo_test.infrastructure.driving.controller



import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.get
import Methodo_test.domain.model.Book
import Methodo_test.domain.usecase.BookService
import Methodo_test.infrastructure.driving.controller.dto.BookController

@WebMvcTest(BookController::class)
class BookControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var bookService: BookService

    @Test
    fun `should return book list`() {
        every { bookService.listBooks() } returns listOf(
            Book("Title A", "Author A"),
            Book("Title B", "Author B")
        )

        mockMvc.get("/books")
            .andExpect {
                status { isOk() }
                jsonPath("$.size()") { value(2) }
            }

        verify { bookService.listBooks() }
    }

    @Test
    fun `should create a book`() {
        every { bookService.addBook(any()) } returns Unit

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"title":"Test","author":"Me"}"""
        }.andExpect {
            status { isOk() }
        }

        verify { bookService.addBook(Book("Test", "Me")) }
    }
}
