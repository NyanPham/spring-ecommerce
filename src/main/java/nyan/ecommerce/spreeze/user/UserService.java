package nyan.ecommerce.spreeze.user;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.insert(user);
    }

    public Optional<User> getUser(ObjectId id) {
        return userRepository.findById(id);
    }

    public User updateUser(ObjectId id, User user) {
        Query userQuery = new Query();
        userQuery.addCriteria(Criteria.where("id").is(id));

        Update userUpdate = new Update();

        if (user.getUsername() != null) {
            userUpdate.set("username", user.getUsername());
        }

        if (user.getEmail() != null) {
            userUpdate.set("email", user.getEmail());
        }

        if (user.getUserRole() != null) {
            userUpdate.set("userRole", user.getUserRole());
        }

        return mongoTemplate.findAndModify(userQuery, userUpdate, new FindAndModifyOptions().returnNew(true),
                User.class);
    }

    public void deleteUser(ObjectId id) {
        userRepository.deleteById(id);
    }
}
