package example.micronaut.book.service;

import example.micronaut.book.domain.Book;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class BookService {
    public final List<Book> bookStore = new ArrayList<>();

    @Inject
    private AnalyticsClient analyticsClient;

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
