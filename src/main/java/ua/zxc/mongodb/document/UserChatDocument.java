package ua.zxc.mongodb.document;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@Builder
@Document(collection = "usersChats")
public class UserChatDocument {

    @Id
    private String id;

    @DocumentReference(lazy = true)
    private UserDocument user;

    @DocumentReference(lazy = true)
    private ChatDocument chat;
}
