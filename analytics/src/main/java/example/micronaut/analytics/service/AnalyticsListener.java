package example.micronaut.analytics.service;

import example.micronaut.analytics.domain.Book;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
@KafkaListener
public class AnalyticsListener {

    @Inject
    private AnalyticsService analyticsService;

    @Topic("${app.topic}")
    public void updateAnalytics(Book book, @KafkaKey String key) {
        System.out.println("AnalyticsListener.updateAnalytics() key = " + key + ", book = " + book);
        analyticsService.updateBookAnalytics(book);
    }

}
