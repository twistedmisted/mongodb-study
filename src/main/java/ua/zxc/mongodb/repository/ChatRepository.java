package ua.zxc.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ua.zxc.mongodb.document.ChatDocument;

@Repository
public interface ChatRepository extends MongoRepository<ChatDocument, String> {
}
