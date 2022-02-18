package example.micronaut.book.controller;

import example.micronaut.book.domain.Book;
import example.micronaut.book.service.BookService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@Controller("/book")
public class BookController {
    @Inject
    private BookService bookService;

    @Get
    List<Book> listAll() {
        return bookService.listAll();
    }

    @Get("/{isbn}")
    Optional<Book> findBook(String isbn) {
        return bookService.findByIsbn(isbn);
    }
}
