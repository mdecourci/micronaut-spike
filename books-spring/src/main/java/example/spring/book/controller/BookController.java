package example.spring.book.controller;

import example.spring.book.domain.Book;
import example.spring.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    List<Book> listAll() {
        return bookService.listAll();
    }

    @GetMapping("/{isbn}")
    Book findBook(@PathVariable String isbn) {
        return bookService.findByIsbn(isbn).orElse(new Book());
    }
}
