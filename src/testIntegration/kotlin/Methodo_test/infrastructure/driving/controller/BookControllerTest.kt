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
import io.kotest.core.spec.style.FunSpec
import Methodo_test.infrastructure.driving.controller.BookController

@WebMvcTest(BookController::class)
class BookControllerTest (
    @MockkBean private val bookService: BookService,
    private val mockMvc: MockMvc
) : FunSpec({

    test("should return book list") {
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

    test("should create a book") {
        every { bookService.addBook(any()) } returns Unit

        mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"title":"Test","author":"Me"}"""
        }.andExpect {
            status { isOk() }
        }

        verify { bookService.addBook(Book("Test", "Me")) }
    }

    test("should reserve a book successfully") {
        every { bookService.reserveBook("1984") } returns Unit

        mockMvc.post("/books/1984/reserve")
            .andExpect {
                status { isOk() }
            }

        verify { bookService.reserveBook("1984") }
    }

    test("should return error when reserving an already reserved book") {
        every { bookService.reserveBook("1984") } throws IllegalStateException("Book is already reserved")

        mockMvc.post("/books/1984/reserve")
            .andExpect {
                status { is5xxServerError() }
                jsonPath("$.message") { value("Book is already reserved") }
            }

        verify { bookService.reserveBook("1984") }
    }

})