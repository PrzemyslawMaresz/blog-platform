package pl.pmar.blogplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pmar.blogplatform.model.Post;
import pl.pmar.blogplatform.repository.PostRepository;
import pl.pmar.blogplatform.repository.UserRepository;
import pl.pmar.blogplatform.model.User;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public UserService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    public ResponseEntity<User> getUserById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    public ResponseEntity<User> saveUser(User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    public ResponseEntity<Void> deleteUser(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<Post> userPosts = postRepository.findAllByUserId(id);
            postRepository.deleteAll(userPosts);
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

    }

}
