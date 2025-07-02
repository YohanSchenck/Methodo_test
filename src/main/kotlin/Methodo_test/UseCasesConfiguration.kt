package Methodo_test

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import Methodo_test.domain.usecase.BookService
import Methodo_test.domain.port.BookRepository

@Configuration
class UseCasesConfiguration {

    @Bean
    fun bookService(repository: BookRepository): BookService =
        BookService(repository)
}