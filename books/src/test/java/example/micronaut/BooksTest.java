package example.micronaut;

import example.micronaut.book.domain.Book;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
@Testcontainers
class BooksTest implements TestPropertyProvider {

    public final static Collection<Book> received = new ConcurrentLinkedDeque();

    @Container
    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @Inject
    AnalyticsListener analyticsListener;

    @Inject
    @Client("/")
    HttpClient client;

    @AfterEach
    void tearDown() {
        received.clear();
    }

    @Ignore
    public void givenAMessageIsPublishedThenBookFound() {
        String isbn = "1491950358";
        final var result = retrieveGet("/book/" + isbn);

        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> result.isPresent());

        assertEquals(1, received.size());
        assertEquals(isbn, received.stream().collect(Collectors.toList()).get(0).getIsbn());
    }

    @Test
    public void givenAMessageIsNotPublishedThenBookNotFound() {
        assertThrows(HttpClientResponseException.class, () -> {
            retrieveGet("/book/INVALID");
        });

        Awaitility.await().atMost(Duration.ofSeconds(10));

        assertTrue(received.isEmpty());
    }

    private Optional<Book> retrieveGet(String url) {
        return client.toBlocking().retrieve(HttpRequest.GET(url), Argument.of(Optional.class, Book.class));
    }

    @Override
    public Map<String, String> getProperties() {
        kafka.start();
        return Map.of("kafka.bootstrap.servers", kafka.getBootstrapServers());
    }

    @KafkaListener(offsetReset = EARLIEST)
    static class AnalyticsListener {

        @Topic("analytics")
        void updateAnalytics(Book book, @KafkaKey String key) {
            System.out.println(">>> AnalyticsListener updateAnalytics() book =" + book + ", key = " + key);
            received.add(book);
        }
    }
}
