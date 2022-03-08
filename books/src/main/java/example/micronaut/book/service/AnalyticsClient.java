package example.micronaut.book.service;

import example.micronaut.book.domain.Book;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import reactor.core.publisher.Mono;

@KafkaClient
public interface AnalyticsClient {
    @Topic("${app.topic}")
    Mono<Book> updateAnalytics(@KafkaKey String key, Book book);
}
