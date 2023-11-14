package ua.zxc.mongodb.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document(collection = "chats")
public class ChatDocument {

    @Id
    private String id;

    private String name;

    @Builder.Default
    @ReadOnlyProperty
    @DocumentReference(lookup = "{'chat':?#{#self._id} }")
    private List<UserChatDocument> members = new ArrayList<>();

    @Builder.Default
    @ReadOnlyProperty
    @DocumentReference(lookup = "{'chat':?#{#self._id} }")
    private List<MessageDocument> messages = new ArrayList<>();

    @Override
    public String toString() {
        return "ChatDocument{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
