package pl.pmar.blogplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.pmar.blogplatform.model.entity.Post;
import pl.pmar.blogplatform.repository.PostRepository;
import pl.pmar.blogplatform.repository.UserRepository;
import pl.pmar.blogplatform.model.entity.User;
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

    public ResponseEntity<Void> deleteUser(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<Post> userPosts = postRepository.findAllByAuthorId(id);
            postRepository.deleteAll(userPosts);
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

    }

    public ResponseEntity<User> updateUser(User updatedUser) {
        Optional<User> userOptional = userRepository.findById(updatedUser.getId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            User user = userOptional.get();
            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());

            return ResponseEntity.ok(userRepository.save(user));
        }
    }
}
