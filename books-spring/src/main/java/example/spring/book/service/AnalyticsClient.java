package example.spring.book.service;

import example.spring.book.domain.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnalyticsClient {
    private final KafkaTemplate<String, Book> kafkaTemplate;
    @Value("${app.topic}")
    private String topic;

    public void updateAnalytics(Book book) {
        kafkaTemplate.send(topic, "books-spring", book).addCallback(
                onSuccess -> log.info("Kafka Message = [{}], Send Success!!", book),
                onFailure -> new RuntimeException("Kafka Message = [{}], Failure, Not Sent!!"));
    }
}
