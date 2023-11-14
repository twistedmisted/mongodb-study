package ua.zxc.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ua.zxc.mongodb.document.UserChatDocument;

@Repository
public interface UserChatRepository extends MongoRepository<UserChatDocument, String> {
}
