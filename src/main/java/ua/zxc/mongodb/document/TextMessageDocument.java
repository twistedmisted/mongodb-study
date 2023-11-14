package ua.zxc.mongodb.document;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import ua.zxc.mongodb.document.embedded.MessageStatus;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Document(collection = "messages")
public class TextMessageDocument extends MessageDocument {

    private String text;

    @Builder(builderMethodName = "textMessageBuilder")
    public TextMessageDocument(String id, Date createdAt, Map<String, Object> params, MessageStatus status, ChatDocument chat, UserDocument sender, String text) {
        super(id, createdAt, params, status, chat, sender);
        this.text = text;
    }

    @Override
    public String toString() {
        return "TextMessageDocument{" +
                "text='" + text + '\'' +
                '}';
    }
}
