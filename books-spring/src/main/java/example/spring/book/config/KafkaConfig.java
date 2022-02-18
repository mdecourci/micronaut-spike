package example.spring.book.config;

import example.spring.book.domain.Book;
import example.spring.book.domain.BookSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    public static final String CONSUMER_APP_ID = "BOOK_LOG-SPRING";
    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, Book> producerFactory() {
        var producerFactory = new DefaultKafkaProducerFactory<String, Book>(config());
        return producerFactory;
    }

    @Bean
    public KafkaTemplate<String, Book> kafkaTemplate(ProducerFactory<String, Book> producerFactory) {
        return new KafkaTemplate<String, Book>(producerFactory);
    }

    private Map<String, Object> config() {
        var config = new HashMap<String, Object>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BookSerializer.class.getName());

        return config;
    }
}
