package ua.zxc.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import ua.zxc.mongodb.document.UserDocument;

import java.util.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserDocument, String> {

    @Query("{ 'name' : ?0, 'createdAt' :  { $lt :  ?1 } }")
    Optional<UserDocument> findByNameAndCreatedAt(String name, Date createdAt);
}
