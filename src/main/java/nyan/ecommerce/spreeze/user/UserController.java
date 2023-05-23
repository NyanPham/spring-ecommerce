package nyan.ecommerce.spreeze.user;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<User>(userService.createUser(user), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<User>> getUser(@PathVariable("id") ObjectId id) {
        return new ResponseEntity<Optional<User>>(userService.getUser(id), HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") ObjectId id, @RequestBody User userToUpdate) {
        return new ResponseEntity<User>(userService.updateUser(id, userToUpdate), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") ObjectId id) {
        userService.deleteUser(id);

        return new ResponseEntity<String>("User deleted", HttpStatus.OK);
    }
}
