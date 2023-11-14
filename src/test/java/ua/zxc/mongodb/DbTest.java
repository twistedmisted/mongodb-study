package ua.zxc.mongodb;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ua.zxc.mongodb.document.*;
import ua.zxc.mongodb.document.embedded.MessageStatus;
import ua.zxc.mongodb.document.embedded.Role;
import ua.zxc.mongodb.document.util.Permissions;
import ua.zxc.mongodb.repository.ChatRepository;
import ua.zxc.mongodb.repository.MessageRepository;
import ua.zxc.mongodb.repository.UserChatRepository;
import ua.zxc.mongodb.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static ua.zxc.mongodb.document.util.Permissions.*;

@SpringBootTest
@TestInstance(PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DbTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserChatRepository userChatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @BeforeAll
    public void init() {
        cleanCollections();
    }

    @AfterAll
    public void cleanDb() {
        cleanCollections();
    }

    @Test
    @Order(1)
    public void saveUsers() {
        Role role1 = createRole("USER", "This role represents a simple user", List.of(WRITE, READ));
        Role role2 = createRole("ADMIN", "This role represents an administrator", List.of(WRITE, READ, UPDATE, DELETE));
        Role role3 = createRole("SUPPORT", "This role represents a support user", List.of(WRITE, READ, UPDATE));
        Role role4 = createRole("OWNER", "This role represents an owner", List.of(WRITE, READ, UPDATE, DELETE));

        UserDocument user1 = createUser("Andrii", "Mishchenko", "andrii@mail.com", "password", role1);
        UserDocument user2 = createUser("Katia", "Prokopenko", "katia@mail.com", "password", role2);
        UserDocument user3 = createUser("Max", "Hoks", "max@mail.com", "password", role3);
        UserDocument user4 = createUser("Steve", "Tarantino", "steve@mail.com", "password", role4);

        userRepository.saveAll(List.of(user1, user2, user3, user4));

        assertEquals(4, userRepository.findAll().size());
    }

    @Test
    @Order(2)
    public void saveChats() {
        ChatDocument chat1 = createChat("IRIS");
        ChatDocument chat2 = createChat("Java");
        ChatDocument chat3 = createChat("MongoDB");
        ChatDocument chat4 = createChat("NoSQL");

        chatRepository.saveAll(List.of(chat1, chat2, chat3, chat4));

        assertEquals(4, chatRepository.findAll().size());
    }

    @Test
    @Order(3)
    public void saveMembers() {
        List<ChatDocument> chats = chatRepository.findAll();
        List<UserDocument> users = userRepository.findAll();
        List<UserChatDocument> usersChats = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            usersChats.add(createUserChat(users.get(i), chats.get(i)));
            if (i + 1 < 4) {
                usersChats.add(createUserChat(users.get(i), chats.get(i + 1)));
            }
        }

        userChatRepository.saveAll(usersChats);

        assertEquals(7, userChatRepository.findAll().size());
    }

    @Test
    @Order(4)
    public void saveMessages() {
        List<UserChatDocument> usersChats = userChatRepository.findAll();
        List<MessageStatus> messageStatuses = getMessageStatuses();
        List<MessageDocument> messages = new ArrayList<>();

        Random random = new Random();
        for (UserChatDocument usersChat : usersChats) {
            messages.add(createMessage("Simple text",
                    Map.of("parseMode", "HTML"),
                    messageStatuses.get(random.nextInt(4)),
                    usersChat.getUser(),
                    usersChat.getChat())
            );
        }

        messageRepository.saveAll(messages);

        assertEquals(7, messageRepository.findAll().size());
    }

    @Test
    @Order(5)
    public void showAll() {
        List<UserDocument> users = userRepository.findAll();
        List<ChatDocument> chats = chatRepository.findAll();
        List<MessageDocument> messages = messageRepository.findAll();

        System.out.println("\nUSERS");
        System.out.println(users);
        System.out.println("\nCHATS");
        System.out.println(chats);
        System.out.println("\nMESSAGES");
        System.out.println(messages);
    }

    @Test
    @Order(6)
    public void testQueryWithTwoConditions() {
        String existingName = "Andrii";
        String notExistingName = "Mykola";
        Date dateNow = Timestamp.valueOf(LocalDateTime.now());

        Optional<UserDocument> userOpt = userRepository.findByNameAndCreatedAt(existingName, dateNow);

        assertTrue(userOpt.isPresent());
        assertEquals("Andrii", userOpt.get().getName());

        assertFalse(userRepository.findByNameAndCreatedAt(notExistingName, dateNow).isPresent());
    }

    @Test
    @Order(7)
    public void testFourStagesOfAggregation() {
        String name = "Andrii";

        assertEquals(2, messageRepository.getMessageCountByUserName(name));
    }

    private UserDocument createUser(String name, String surname, String email, String password, Role role) {
        return UserDocument.builder()
                .name(name)
                .surname(surname)
                .email(email)
                .password(password)
                .role(role)
                .build();
    }

    private Role createRole(String name, String description, List<Permissions> permissions) {
        return Role.builder()
                .name(name)
                .description(description)
                .permissions(permissions)
                .build();
    }

    private ChatDocument createChat(String name) {
        return ChatDocument.builder()
                .name(name)
                .build();
    }

    private UserChatDocument createUserChat(UserDocument user, ChatDocument chat) {
        return UserChatDocument.builder()
                .user(user)
                .chat(chat)
                .build();
    }

    private MessageStatus createMessageStatus(MessageStatus.Status name, String description) {
        return MessageStatus.builder()
                .name(name)
                .description(description)
                .build();
    }

    private List<MessageStatus> getMessageStatuses() {
        return List.of(
                createMessageStatus(MessageStatus.Status.READ, "This status represents a read message"),
                createMessageStatus(MessageStatus.Status.SENT, "This status represents a sent message"),
                createMessageStatus(MessageStatus.Status.ARCHIVED, "This status represents a archived message"),
                createMessageStatus(MessageStatus.Status.READ, "This status represents a read message")
        );
    }

    private TextMessageDocument createMessage(String text, Map<String, Object> params, MessageStatus status, UserDocument sender, ChatDocument chat) {
        return TextMessageDocument.textMessageBuilder()
                .text(text)
                .params(params)
                .status(status)
                .sender(sender)
                .chat(chat)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    private void cleanCollections() {
        userChatRepository.deleteAll();
        userRepository.deleteAll();
        chatRepository.deleteAll();
        messageRepository.deleteAll();
    }
}
