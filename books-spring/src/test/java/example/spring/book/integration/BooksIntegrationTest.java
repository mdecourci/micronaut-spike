package example.spring.book.integration;

import example.spring.book.SpringBookApplication;
import example.spring.book.domain.Book;
import example.spring.book.integration.config.KafkaConsumerTestConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = SpringBookApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = BooksIntegrationTest.TestApplicationContextInitializer.class)
@Import({KafkaConsumerTestConfig.class})
@Testcontainers
class BooksIntegrationTest {

    @Container
    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:latest"));


    @LocalServerPort
    int port;

    @Autowired
    AnalyticsListener analyticsListener;

    @Autowired
    ConsumerFactory consumerFactory;
    @Autowired
    TestRestTemplate testRestTemplate;

//    @Autowired
//    HttpClient client;

    @AfterEach
    void tearDown() {
        analyticsListener.getReceived().clear();
    }

    @Test
    public void givenAMessageIsPublishedThenBookFound() throws Exception {
        String isbn = "1491950358";
        final var result = this.testRestTemplate.getForEntity("/book/{isbn}", Book.class, isbn);

        Awaitility.await().atMost(10, TimeUnit.SECONDS).until(() -> analyticsListener.getReceived().size() > 0);

//        Unreliables.retryUntilTrue(10, TimeUnit.SECONDS, () -> {
//            consumer.poll(Duration.ofMillis(50))
//                    .iterator()
//                    .forEachRemaining(allRecords::add);
//
//            return analyticsListener.getReceived().size() > 0;
//        });

        assertEquals(1, analyticsListener.getReceived().size());
        assertEquals(isbn, analyticsListener.getReceived().stream().collect(Collectors.toList()).get(0).getIsbn());
    }

    @Test
    public void givenAMessageIsNotPublishedThenBookNotFound() {
//        assertThrows(HttpClientResponseException.class, () -> {
//            retrieveGet("/book/INVALID");
//        });

        Awaitility.await().atMost(Duration.ofSeconds(5));

        assertTrue(analyticsListener.getReceived().isEmpty());
    }
//
//    @Test
//    void testItWorks() {
//        assertTrue(application.isRunning());
//    }

//    private Optional<Book> retrieveGet(String url) {
//        return client.toBlocking().retrieve(HttpRequest.GET(url), Argument.of(Optional.class, Book.class));
//    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    protected List<ConsumerRecord<String, Book>> drain(
            Consumer<String, Book> consumer,
            int expectedRecordCount) {

        List<ConsumerRecord<String, Book>> allRecords = new ArrayList<>();

        Unreliables.retryUntilTrue(10, TimeUnit.SECONDS, () -> {
            consumer.poll(Duration.ofMillis(50))
                    .iterator()
                    .forEachRemaining(allRecords::add);

            return allRecords.size() == expectedRecordCount;
        });

        return allRecords;
    }

    @Configuration
    static class TestApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            System.setProperty("kafka.bootstrap.servers", kafka.getBootstrapServers());
        }
    }
}
