package ua.zxc.mongodb.document.embedded;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageStatus {

    private Status name;

    private String description;

    public enum Status {
        READ, SENT, ARCHIVED
    }
}
