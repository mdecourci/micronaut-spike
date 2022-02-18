package example.micronaut.analytics.domain;

import io.micronaut.core.annotation.Introspected;

import java.util.Objects;

@Introspected
public class BookAnalytics {
    private String isbn;
    private long count;

    public BookAnalytics(String isbn, long count) {
        this.isbn = isbn;
        this.count = count;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookAnalytics that = (BookAnalytics) o;
        return count == that.count && isbn.equals(that.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, count);
    }

    @Override
    public String toString() {
        return "BookAnalytics{" +
                "isbn='" + isbn + '\'' +
                ", count=" + count +
                '}';
    }
}
