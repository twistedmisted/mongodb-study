package ua.zxc.mongodb.document.embedded;

import lombok.Builder;
import lombok.Data;
import ua.zxc.mongodb.document.util.Permissions;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Role {

    private String name;

    private String description;

    @Builder.Default
    private List<Permissions> permissions = new ArrayList<>();
}
