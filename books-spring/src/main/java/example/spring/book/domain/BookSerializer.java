package example.spring.book.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

@Data
@Slf4j
public class BookSerializer implements Serializer<Book> {
    private final ObjectMapper objectMapper;

    public BookSerializer() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public byte[] serialize(String topic, Book book) {
        try {
            if (book == null) {
                log.info("Null received at serializing");
                return null;
            }
            log.info("Serializing...");
            return objectMapper.writeValueAsBytes(book);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing Book to byte[]");
        }
    }
}
