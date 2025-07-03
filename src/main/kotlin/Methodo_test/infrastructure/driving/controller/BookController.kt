package Methodo_test.infrastructure.driving.controller

import org.springframework.web.bind.annotation.*
import Methodo_test.domain.model.Book
import Methodo_test.domain.usecase.BookService
import Methodo_test.infrastructure.driving.controller.dto.BookDTO

@RestController
@RequestMapping("/books")
class BookController(private val bookService: BookService) {

    @GetMapping
    fun listBooks(): List<BookDTO> =
        bookService.listBooks().map { BookDTO(it.title, it.author) }

    @PostMapping
    fun addBook(@RequestBody dto: BookDTO) {
        bookService.addBook(Book(dto.title, dto.author))
    }
}