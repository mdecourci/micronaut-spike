package example.spring.book.integration;

import example.spring.book.domain.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class AnalyticsListener {
    private final Collection<Book> received = new ConcurrentLinkedDeque();
    private final CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = "${app.topic}", groupId = "${app.group}", containerFactory = "kafkaListenerContainerFactory")
    void updateAnalytics(@Payload Book book, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) {
        System.out.println(key);
        log.info(">>> AnalyticsListener updateAnalytics() book=[{}]", book);
        received.add(book);
        this.latch.countDown();
    }

    public Collection<Book> getReceived() {
        return received;
    }

    public CountDownLatch getLatch() {
        return latch;
    }


}
