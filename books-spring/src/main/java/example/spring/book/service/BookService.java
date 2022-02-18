package example.spring.book.service;

import example.spring.book.domain.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    public final List<Book> bookStore = new ArrayList<>();
    private final AnalyticsClient analyticsClient;

    @PostConstruct
    void init() {
        bookStore.add(new Book("1491950358", "Building Microservices"));
        bookStore.add(new Book("1680502395", "Release It!"));
        bookStore.add(new Book("0321601912", "Continuous Delivery"));
    }

    public List<Book> listAll() {
        return bookStore;
    }

    public Optional<Book> findByIsbn(String isbn) {
        return bookStore.stream().filter(book -> book.getIsbn().equals(isbn)).findFirst();
    }
}
