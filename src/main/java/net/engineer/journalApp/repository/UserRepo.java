package net.engineer.journalApp.repository;

import net.engineer.journalApp.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<User, ObjectId> {
    User findByUserName(String name);
    void deleteByUserName(String name);
}
