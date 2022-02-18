package example.micronaut.analytics.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.kafka.common.serialization.Deserializer;

@Singleton
public class BookDeserializer implements Deserializer<Book> {
    @Inject
    private ObjectMapper objectMapper;

    @Override
    public Book deserialize(String topic, byte[] bytes) {
        return null;
    }
}
