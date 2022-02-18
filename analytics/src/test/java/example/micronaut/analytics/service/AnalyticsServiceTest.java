package example.micronaut.analytics.service;

import example.micronaut.analytics.domain.Book;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class AnalyticsServiceTest {

    @Inject
    AnalyticsService analyticsService;

    @Test
    void updateBookAnalytics() {
        Book b1 = new Book("1491950358", "Building Microservices");
        Book b2 = new Book("1680502395", "Release It!");

        analyticsService.updateBookAnalytics(b1);
        analyticsService.updateBookAnalytics(b1);
        analyticsService.updateBookAnalytics(b1);
        analyticsService.updateBookAnalytics(b2);

        assertThat(analyticsService.listAnalytics(), hasSize(2));

        assertTrue(analyticsService.listAnalytics().stream()
                .filter(a -> a.getIsbn().equals(b1.getIsbn()))
                .allMatch(a -> a.getCount() == 3));

        assertTrue(analyticsService.listAnalytics().stream()
                .filter(a -> a.getIsbn().equals(b2.getIsbn()))
                .allMatch(a -> a.getCount() == 1));
    }
}