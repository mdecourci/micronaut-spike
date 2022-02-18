package example.spring.book.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.support.serializer.DeserializationException;

@Data
@Slf4j
public class BookDeSerializer implements Deserializer<Book> {
    private final ObjectMapper objectMapper;

    public BookDeSerializer() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Book deserialize(String topic, byte[] bytes) {
        try {
            if (bytes == null) {
                log.info("Null bytes received to deserialize");
                return null;
            }
            log.info("Serializing...");
            return objectMapper.readValue(bytes, Book.class);
        } catch (Exception e) {
            throw new DeserializationException("Error when deserializing byte[] to Book", bytes, false, e);
        }
    }
}