package ua.zxc.mongodb.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ua.zxc.mongodb.document.MessageDocument;

@Repository
public interface MessageRepository extends MongoRepository<MessageDocument, String> {

    @Aggregation(pipeline = {
            "{ $lookup : { from : 'users', localField : 'sender', foreignField : '_id', as : 'sender_info' } }",
            "{ $unwind : { path : '$sender_info' } }",
            "{ $match : { 'sender_info.name' : ?0 } }",
            "{ $group : { _id : '$sender_info.name', count : { $sum : 1 } } }"
    })
    int getMessageCountByUserName(String name);
}
