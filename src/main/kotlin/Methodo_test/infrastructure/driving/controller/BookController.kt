package Methodo_test.infrastructure.driving.controller

import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import Methodo_test.domain.model.Book
import Methodo_test.domain.usecase.BookService
import Methodo_test.infrastructure.driving.controller.dto.BookDTO

@RestController
@RequestMapping("/books")
class BookController(private val bookService: BookService) {

    @GetMapping
    fun listBooks(): List<BookDTO> =
        bookService.listBooks().map { BookDTO(it.title, it.author, it.reserved) }

    @PostMapping
    fun addBook(@RequestBody dto: BookDTO) {
        bookService.addBook(Book(dto.title, dto.author))
    }

    @PostMapping("/{title}/reserve")
    fun reserveBook(@PathVariable title: String): ResponseEntity<Any> {
        return try {
            bookService.reserveBook(title)
            ResponseEntity.ok().build()
        } catch (e: IllegalStateException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("message" to e.message))
        }
    }
}