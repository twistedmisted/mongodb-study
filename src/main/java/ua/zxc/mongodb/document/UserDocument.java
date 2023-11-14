package ua.zxc.mongodb.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import ua.zxc.mongodb.document.embedded.Role;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(collection = "users")
public class UserDocument {

    @Id
    private String id;

    private String name;

    private String surname;

    @Indexed(unique = true)
    private String email;

    private String password;

    @Builder.Default
    private Date createdAt = Timestamp.valueOf(LocalDateTime.now());

    private Role role;

    @Builder.Default
    @ReadOnlyProperty
    @DocumentReference(lookup="{'user':?#{#self._id} }")
    private List<UserChatDocument> chats = new ArrayList<>();

    @Builder.Default
    @ReadOnlyProperty
    @DocumentReference(lookup="{'sender':?#{#self._id} }")
    private List<MessageDocument> messages = new ArrayList<>();

    @Override
    public String toString() {
        return "UserDocument{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", createdAt=" + createdAt +
                ", role=" + role +
                '}';
    }
}
