package nyan.ecommerce.spreeze.user;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    public UserDetails findUserByEmail(String email);

    public User findByResetPasswordToken(String token);
}
