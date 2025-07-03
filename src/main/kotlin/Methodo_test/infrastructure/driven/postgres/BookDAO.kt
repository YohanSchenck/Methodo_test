package Methodo_test.infrastructure.driven.postgres

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import Methodo_test.domain.model.Book
import Methodo_test.domain.port.BookRepository

@Repository
class BookDAO(
    public val jdbcTemplate: NamedParameterJdbcTemplate
) : BookRepository {

    override fun save(book: Book) {
        val sql = "INSERT INTO books(title, author, reserved) VALUES(:title, :author, :reserved)"
        val params = MapSqlParameterSource()
            .addValue("title", book.title)
            .addValue("author", book.author)
            .addValue("reserved", book.reserved)
        jdbcTemplate.update(sql, params)
    }

    override fun reserve(title: String) {
        val sql = "UPDATE books SET reserved = true WHERE title = :title AND reserved = false"
        val params = MapSqlParameterSource().addValue("title", title)
        val updated = jdbcTemplate.update(sql, params)


        if (updated == 0) {
            throw IllegalStateException("Book is already reserved or does not exist")
        }
    }


    override fun findAll(): List<Book> {
        val sql = "SELECT title, author, reserved FROM books ORDER BY title"
        return jdbcTemplate.query(sql, rowMapper)
    }

    private val rowMapper = RowMapper { rs, _ ->
        Book(
            title = rs.getString("title"),
            author = rs.getString("author"),
            reserved = rs.getBoolean("reserved")
        )
    }
}
