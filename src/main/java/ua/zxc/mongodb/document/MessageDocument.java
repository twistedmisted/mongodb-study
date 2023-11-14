package ua.zxc.mongodb.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import ua.zxc.mongodb.document.embedded.MessageStatus;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@Document(collection = "messages")
public class MessageDocument {

    @Id
    private String id;

    @Builder.Default
    private Date createdAt = Timestamp.valueOf(LocalDateTime.now());

    @Builder.Default
    private Map<String, Object> params = new HashMap<>();

    private MessageStatus status;

    @DocumentReference(lazy = true)
    private ChatDocument chat;

    @DocumentReference(lazy = true)
    private UserDocument sender;

    @Override
    public String toString() {
        return "MessageDocument{" +
                "id='" + id + '\'' +
                ", createdAt=" + createdAt +
                ", params=" + params +
                ", status=" + status +
                '}';
    }
}
